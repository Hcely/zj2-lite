package org.zj2.lite.util.thread;

import lombok.extern.slf4j.Slf4j;
import org.zj2.lite.Destroyable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TaskWorker
 *
 * @author peijie.ye
 * @date 2023/3/7 21:24
 */
@Slf4j
public class TaskWorker implements Executor, Destroyable {
    private static final AtomicIntegerFieldUpdater<TaskWorker> RUNNING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            TaskWorker.class, "running");
    private final ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private volatile int running;
    private volatile int wait;
    private final Thread worker;

    public TaskWorker() {
        this.running = 1;
        this.wait = 0;
        this.worker = AsyncThreadFactory.INSTANCE.newThread(new TaskHandler(this));
        this.worker.start();
    }

    public Thread getWorker() {
        return worker;
    }

    public boolean isRunning() {
        return running == 1;
    }

    @Override
    public void execute(Runnable command) {
        if (isRunning()) {
            taskQueue.add(command);
            if (wait > 0) {
                synchronized (taskQueue) { taskQueue.notify(); }//NOSONAR
            }
        }
    }

    @Override
    public void destroy() {
        if (RUNNING_UPDATER.compareAndSet(this, 1, 0)) {
            if (wait > 0) {
                synchronized (taskQueue) { taskQueue.notify(); }//NOSONAR
            }
        }
    }

    private Runnable poll() {
        Runnable runnable;
        while (isRunning()) {
            if ((runnable = taskQueue.poll()) != null) {
                return runnable;
            }
            synchronized (taskQueue) {
                wait = 1;
                if ((runnable = taskQueue.poll()) != null) {
                    return runnable;
                }
                if (!isRunning()) { break; }
                try {
                    taskQueue.wait(100);
                } catch (Throwable ignored) { }//NOSONAR
                finally {
                    wait = 0;
                }
            }
        }
        return null;

    }

    private static class TaskHandler implements Runnable {
        private final TaskWorker worker;

        private TaskHandler(TaskWorker worker) {
            this.worker = worker;
        }

        @Override
        public void run() {
            Runnable runnable;
            TaskWorker localWorker = this.worker;
            while (localWorker.isRunning()) {
                if ((runnable = localWorker.poll()) != null) {
                    try {
                        runnable.run();
                    } catch (Throwable e) {//NOSONAR
                        TaskWorker.log.info("async task error!", e);
                    }
                }
            }
        }
    }
}
