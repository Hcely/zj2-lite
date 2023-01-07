package org.zj2.lite.util;

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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.zj2.lite.common.context.ZContext;
import org.zj2.lite.common.util.CollUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AsyncUtil 异步执行工具
 * <br>CreateDate 三月 02,2022
 * @author peijie.ye
 * @since 1.0
 */
@Configuration
@EnableAsync
public class AsyncUtil implements AsyncConfigurer, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtil.class);
    private static final ThreadPoolTaskExecutor EXECUTOR = createExecutor(64);

    public static ThreadPoolTaskExecutor createExecutor(int coreNum) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreNum);
        executor.setMaxPoolSize(coreNum << 2);
        executor.setKeepAliveSeconds(60 * 5);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setQueueCapacity(1 << 24);
        executor.setThreadFactory(new AsyncThreadFactory());
        executor.setTaskDecorator(AsyncUtil::of);
        executor.initialize();
        return executor;
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
     * @param command
     */
    public static void execute(final Runnable command) {
        if (command != null) {
            EXECUTOR.execute(of(command));
        }
    }

    /**
     * 事务提交后异步执行
     * @param command
     */
    public static void executeAfterCommit(final Runnable command) {
        if (command == null) { return; }
        if (TransactionSyncUtil.isActualTransactionActive()) {
            TransactionSyncUtil.afterCommit(of(command), AsyncUtil::execute);
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
            if (cmd != null) { futures.add(EXECUTOR.submit(of(cmd))); }
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
        private final ZContext context;
        protected final String tid;


        protected AsyncCommand() {
            this(null);
        }

        public AsyncCommand(Runnable command) {
            this.context = ZContext.copyContext();
            this.command = command;
            this.tid = TraceContext.traceId();
        }

        @Override
        @SneakyThrows
        public void run() {
            Throwable error = null;
            ZContext oldContext = ZContext.setContext(context);
            try {
                run0();
            } catch (Throwable e) {// NOSONAR
                error = e;
                LOGGER.error("[异步任务执行异常]", e);
                throw e;
            } finally {
                ZContext.setContext(oldContext);
            }
        }

        protected void run0() throws Throwable {
            if (command != null) { command.run(); }
        }
    }

    public static class AsyncThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;// NOSONAR
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        AsyncThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "ASYNC-TASK-" + poolNumber.getAndIncrement() + "-";// 指定线程名称好定位问题
        }

        public Thread newThread(Runnable r) {
            Thread t = new AsyncTaskThread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) { t.setDaemon(false); }
            if (t.getPriority() != Thread.NORM_PRIORITY) { t.setPriority(Thread.NORM_PRIORITY); }
            return t;
        }
    }

    private static final class AsyncTaskThread extends Thread {
        public AsyncTaskThread(ThreadGroup group, Runnable target, String name, long stackSize) {
            super(group, target, name, stackSize);
        }
    }
}