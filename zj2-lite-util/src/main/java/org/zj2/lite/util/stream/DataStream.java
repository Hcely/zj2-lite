package org.zj2.lite.util.stream;

/**
 * 数据流
 *
 * @author peijie.ye
 * @date 2023/2/20 12:42
 */
public interface DataStream<T> {
    /**
     * 返回null表示没有数据
     */
    T next();
}
