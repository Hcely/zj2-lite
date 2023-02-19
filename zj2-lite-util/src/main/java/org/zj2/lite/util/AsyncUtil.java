package org.zj2.lite.util;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.apache.skywalking.apm.toolkit.trace.TraceCrossThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.zj2.lite.common.context.ZContexts;
import org.zj2.lite.common.util.CollUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AsyncUtil 异步执行工具
 * <br>CreateDate 三月 02,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
@Configuration
@EnableAsync
@SuppressWarnings("all")
public class AsyncUtil implements AsyncConfigurer, DisposableBean {
    private static final int MAX_CORE = 1 << 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtil.class);
    private static final ThreadTaskExecutor EXECUTOR;

    static {
        int coreNum = Runtime.getRuntime().availableProcessors() << 2;
        EXECUTOR = createExecutor(Math.max(coreNum, MAX_CORE));
    }

    public static ThreadTaskExecutor createExecutor(int coreNum) {
        return new ThreadTaskExecutor(coreNum);
    }

    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        return EXECUTOR;
    }

    @Override
    public void destroy() {
        EXECUTOR.shutdown();
    }

    public static boolean isAsyncThread() {
        return isAsyncThread(Thread.currentThread());
    }

    private static boolean isAsyncThread(Thread currentThread) {
        return currentThread.getClass() == AsyncTaskThread.class;
    }

    /**
     * 异步执行
     *
     * @param command
     */
    public static void execute(final Runnable command) {
        if (command != null) { EXECUTOR.execute(command); }
    }

    public static void execute(final Object key, final Runnable command) {
        if (command != null) { EXECUTOR.execute(key, command); }
    }

    /**
     * 事务提交后异步执行
     *
     * @param command
     */
    public static void executeAfterCommit(final Runnable command) {
        if (command == null) { return; }
        if (TransactionSyncUtil.isTransactionActive()) {
            TransactionSyncUtil.afterCommit(of(command), AsyncUtil::execute);
        } else {
            execute(command);
        }
    }

    public static void executeAfterCommit(final Object key, final Runnable command) {
        if (command == null) { return; }
        if (TransactionSyncUtil.isTransactionActive()) {
            TransactionSyncUtil.afterCommit(of(command), cmd -> execute(key, command));
        } else {
            execute(command);
        }
    }

    public static void join(Runnable... commands) {
        join0(false, commands);
    }

    public static void joinIgnoreError(Runnable... commands) {
        join0(true, commands);
    }

    @SneakyThrows
    private static void join0(boolean ignoreError, Runnable[] commands) {//NOSONAR
        if (CollUtil.isEmpty(commands)) { return; }
        List<Future<?>> futures = new ArrayList<>(commands.length);
        Throwable ex = null;
        for (Runnable cmd : commands) {
            if (cmd != null) { futures.add(EXECUTOR.submit(cmd)); }
        }
        for (Future<?> f : futures) {
            if (ex != null) {
                f.cancel(false);
            } else {
                try {
                    f.get();
                } catch (InterruptedException | CancellationException ignored) {//NOSONAR
                    // NOTHING
                } catch (ExecutionException e) {
                    if (!ignoreError) {
                        ex = e.getCause();
                    }
                }
            }
        }
        if (ex != null) { throw ex; }
    }

    /**
     * 返回具有上下文runnable
     *
     * @param command
     * @return
     */
    public static Runnable of(Runnable command) {
        return command instanceof AsyncCommand ? command : new AsyncCommand(command);
    }

    @SuppressWarnings("all")
    @TraceCrossThread //skywalking 链路跟踪
    public static class AsyncCommand implements Runnable {
        private final Runnable command;
        private final ZContexts context;
        @Getter
        protected final String tid;

        protected AsyncCommand() {
            this(null);
        }

        public AsyncCommand(Runnable command) {
            this.context = ZContexts.copyContexts();
            this.command = command;
            this.tid = TraceContext.traceId();
        }

        @Override
        @SneakyThrows
        public void run() {
            Throwable error = null;
            ZContexts oldContext = ZContexts.setContexts(context);
            try {
                run0();
            } catch (Throwable e) {// NOSONAR
                error = e;
                LOGGER.error("[异步任务执行异常]", e);
                throw e;
            } finally {
                ZContexts.setContexts(oldContext);
            }
        }

        protected void run0() throws Throwable {
            if (command != null) { command.run(); }
        }
    }


    public static class ThreadTaskExecutor implements AsyncTaskExecutor {
        private final ThreadPoolExecutor commonExecutor;
        private final ThreadPoolExecutor[] workers;
        private final int mask;

        public ThreadTaskExecutor(int coreSize) {
            coreSize = 32 - Integer.numberOfLeadingZeros(coreSize - 1);
            this.workers = new ThreadPoolExecutor[coreSize];
            this.mask = coreSize - 1;
            //
            AsyncThreadFactory threadFactory = new AsyncThreadFactory();
            for (int i = 0; i < coreSize; ++i) {
                workers[i] = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                        threadFactory);
            }
            this.commonExecutor = new ThreadPoolExecutor(coreSize, coreSize << 2, 60_000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(10000_0000), threadFactory);
        }

        @Override
        public void execute(Runnable command, long startTimeout) {
            execute(command);
        }

        @Override
        public Future<?> submit(Runnable command) {
            FutureTask<?> futureTask = new FutureTask<>(command, null);
            execute(futureTask);
            return futureTask;
        }

        @Override
        public <T> Future<T> submit(Callable<T> command) {
            FutureTask<T> futureTask = new FutureTask<>(command);
            execute(futureTask);
            return futureTask;
        }

        @Override
        public void execute(Runnable command) {
            commonExecutor.execute(of(command));
        }

        public void execute(Object key, Runnable command) {
            if (key == null) {
                execute(command);
            } else {
                workers[key.hashCode() & mask].execute(of(command));
            }
        }

        public void shutdown() {
            for (ThreadPoolExecutor e : workers) { e.shutdown(); }
            commonExecutor.shutdown();
        }
    }

    private static class AsyncThreadFactory implements ThreadFactory {
        private static final AtomicLong THREAD_NUMBER = new AtomicLong(1);
        private final ThreadGroup group;// NOSONAR

        AsyncThreadFactory() {
            group = Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(Runnable r) {
            Thread t = new AsyncTaskThread(this, r, "ASYNC-" + THREAD_NUMBER.getAndIncrement());
            if (t.isDaemon()) { t.setDaemon(false); }
            if (t.getPriority() != Thread.NORM_PRIORITY) { t.setPriority(Thread.NORM_PRIORITY); }
            return t;
        }
    }

    private static final class AsyncTaskThread extends Thread {
        public AsyncTaskThread(AsyncThreadFactory threadFactory, Runnable target, String name) {
            super(threadFactory.group, target, name, 0);
        }
    }
}