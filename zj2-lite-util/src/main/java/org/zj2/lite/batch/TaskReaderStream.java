package org.zj2.lite.batch;

import lombok.SneakyThrows;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.Concurrent;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TaskReaderStream
 *
 * @author peijie.ye
 * @date 2023/2/17 12:03
 */
public class TaskReaderStream<T> implements TaskStream<T>, Concurrent {
    private final ConcurrentLinkedQueue<T> bufferQueue = new ConcurrentLinkedQueue<>();
    private final TaskReader<T> reader;
    private volatile boolean hasMore;
    private volatile Throwable error;//NOSONAR

    public TaskReaderStream(TaskReader<T> reader) {
        this.reader = reader;
        this.hasMore = true;
    }

    @Override
    public T next() {//NOSONAR
        while (hasMore) {
            if (bufferQueue.isEmpty()) { readMore(); }
            T value = bufferQueue.poll();
            if (value != null) { return value; }
        }
        return null;
    }

    @SneakyThrows
    protected boolean readMore() {
        synchronized (bufferQueue) {
            boolean success = true;
            try {
                if (bufferQueue.isEmpty()) {
                    Collection<T> coll;
                    if (reader.hasMore() && CollUtil.isNotEmpty(coll = reader.read())) {//NOSONAR
                        success = hasMore = true;
                        CollUtil.addAll(bufferQueue, coll, true);
                    } else {
                        success = hasMore = false;
                    }
                }
            } catch (Throwable e) {//NOSONAR
                error = e;
            }
            if (!success && error != null) { throw error; }
            return success;
        }
    }

}
