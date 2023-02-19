package org.zj2.lite.batch;

import org.zj2.lite.common.util.Concurrent;

/**
 * TaskStreamWrrapper
 *
 * @author peijie.ye
 * @date 2023/2/17 16:24
 */
class TaskStreamWrapper<T> implements TaskStream<T>, Concurrent {
    private final TaskStream<T> stream;

    TaskStreamWrapper(TaskStream<T> stream) {
        this.stream = stream;
    }

    @Override
    public synchronized T next() {
        return stream.next();
    }
}
