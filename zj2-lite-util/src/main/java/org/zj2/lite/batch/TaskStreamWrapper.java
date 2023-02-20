package org.zj2.lite.batch;

import org.zj2.lite.common.util.Concurrent;
import org.zj2.lite.util.stream.DataStream;

/**
 * TaskStreamWrrapper
 *
 * @author peijie.ye
 * @date 2023/2/17 16:24
 */
class TaskStreamWrapper<T> implements DataStream<T>, Concurrent {
    private final DataStream<T> stream;

    TaskStreamWrapper(DataStream<T> stream) {
        this.stream = stream;
    }

    @Override
    public synchronized T next() {
        return stream.next();
    }
}
