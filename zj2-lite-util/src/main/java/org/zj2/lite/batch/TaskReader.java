package org.zj2.lite.batch;

import java.util.Collection;

/**
 * TaskReadStream
 *
 * @author peijie.ye
 * @date 2023/2/17 13:13
 */
public interface TaskReader<T> {
    Collection<T> read();

    boolean hasMore();

    default TaskStream<T> stream() {
        return new TaskReaderStream<>(this);
    }
}
