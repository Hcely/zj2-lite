package org.zj2.lite.util.thread;

import org.zj2.lite.common.util.ZThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class AsyncThreadFactory implements ThreadFactory {
    private static final AtomicLong THREAD_NUMBER = new AtomicLong(1);
    public static final ThreadFactory INSTANCE = new AsyncThreadFactory();
    final ThreadGroup group;// NOSONAR

    AsyncThreadFactory() {
        group = Thread.currentThread().getThreadGroup();
    }

    public Thread newThread(Runnable r) {
        Thread t = new AsyncTaskThread(this, r, "ASYNC-" + THREAD_NUMBER.getAndIncrement());
        if (t.isDaemon()) { t.setDaemon(false); }
        if (t.getPriority() != Thread.NORM_PRIORITY) { t.setPriority(Thread.NORM_PRIORITY); }
        return t;
    }

    private static final class AsyncTaskThread extends ZThread {
        public AsyncTaskThread(AsyncThreadFactory threadFactory, Runnable target, String name) {
            super(threadFactory.group, target, name);
        }
    }
}