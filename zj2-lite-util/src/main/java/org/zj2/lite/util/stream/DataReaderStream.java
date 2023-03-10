package org.zj2.lite.util.stream;

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
public class DataReaderStream<T> implements DataStream<T>, Concurrent {
    private final ConcurrentLinkedQueue<T> bufferQueue = new ConcurrentLinkedQueue<>();
    private final DataReader<T> reader;
    private volatile boolean hasMore;

    public DataReaderStream(DataReader<T> reader) {
        this.reader = reader;
        this.hasMore = true;
    }

    @Override
    public T next() {//NOSONAR
        while(hasMore || !bufferQueue.isEmpty()) {
            if(bufferQueue.isEmpty()) { readMore(); }
            T value = bufferQueue.poll();
            if(value != null) { return value; }
        }
        return null;
    }

    protected void readMore() {
        synchronized(bufferQueue) {
            if(bufferQueue.isEmpty()) {
                Collection<T> coll;
                if(reader.hasMore() && CollUtil.isNotEmpty(coll = reader.read())) {//NOSONAR
                    hasMore = true;
                    CollUtil.addAll(bufferQueue, coll, true);
                } else {
                    hasMore = false;
                }
            }
        }
    }

}
