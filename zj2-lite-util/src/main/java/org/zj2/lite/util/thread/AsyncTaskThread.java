package org.zj2.lite.util.thread;

import org.zj2.lite.common.util.ZThread;

public final class AsyncTaskThread extends ZThread {
    public AsyncTaskThread(AsyncThreadFactory threadFactory, Runnable target, String name) {
        super(threadFactory.group, target, name);
    }
}