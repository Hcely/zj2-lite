package org.zj2.lite.batch;

import org.zj2.lite.common.util.CollUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * TaskCollection
 *
 * @author peijie.ye
 * @date 2023/2/17 11:53
 */
public class TaskCollectionStream<T> implements TaskFixedStream<T> {
    private final Iterator<T> iterator;
    private final int size;

    public TaskCollectionStream(Collection<T> coll) {
        this.iterator = coll == null ? CollUtil.emptyIterator() : coll.iterator();
        this.size = CollUtil.size(coll);
    }

    @Override
    public T next() {
        while (iterator.hasNext()) {
            T value = iterator.next();
            if (value != null) { return value; }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }
}
