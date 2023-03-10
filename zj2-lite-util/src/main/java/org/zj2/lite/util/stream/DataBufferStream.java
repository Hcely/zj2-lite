package org.zj2.lite.util.stream;

import org.zj2.lite.common.util.Concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TaskReaderStream
 *
 * @author peijie.ye
 * @date 2023/2/17 12:03
 */
public class DataBufferStream<T> implements DataStream<T>, Concurrent {
    private final ConcurrentLinkedQueue<T> bufferQueue = new ConcurrentLinkedQueue<>();
    private final DataStream<T> stream;
    private int bufferSize = 100;
    private volatile boolean hasMore;

    public DataBufferStream(DataStream<T> stream) {
        this.stream = stream;
        this.hasMore = true;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = Math.max(bufferSize, 50);
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
                for(int i = 0; i < bufferSize; ++i) {
                    T data = stream.next();
                    if(data == null) {
                        hasMore = i > 0;
                        break;
                    }
                    bufferQueue.add(data);
                }
            }
        }
    }

}
