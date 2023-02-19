package org.zj2.lite.batch;

/**
 * TaskCollection
 *
 * @author peijie.ye
 * @date 2023/2/17 11:45
 */
public interface TaskStream<T> {
    /**
     * 返回null表示没有数据
     */
    T next();
}
