package org.zj2.lite.batch;

/**
 * TaskFixedStream
 *
 * @author peijie.ye
 * @date 2023/2/17 16:43
 */
public interface TaskFixedStream<T> extends TaskStream<T> {
    int size();
}
