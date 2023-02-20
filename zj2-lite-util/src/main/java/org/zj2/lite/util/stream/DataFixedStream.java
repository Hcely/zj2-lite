package org.zj2.lite.util.stream;

/**
 * TaskFixedStream
 *
 * @author peijie.ye
 * @date 2023/2/17 16:43
 */
public interface DataFixedStream<T> extends DataStream<T> {
    int size();
}
