package org.zj2.lite.util.stream;

import java.util.Collection;

/**
 * TaskReadStream
 *
 * @author peijie.ye
 * @date 2023/2/17 13:13
 */
public interface DataReader<T> {
    Collection<T> read();

    boolean hasMore();

    default DataStream<T> stream() {
        return new DataReaderStream<>(this);
    }
}
