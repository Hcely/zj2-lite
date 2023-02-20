package org.zj2.lite.util.stream;

import org.zj2.lite.Resettable;
import org.zj2.lite.common.util.CollUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * TaskCollection
 *
 * @author peijie.ye
 * @date 2023/2/17 11:53
 */
public class DataCollectionStream<T> implements DataFixedStream<T>, Resettable {
    private final Collection<T> coll;
    private Iterator<T> iterator;

    public DataCollectionStream(Collection<T> coll) {
        this.coll = coll;
        this.iterator = coll == null ? CollUtil.emptyIterator() : coll.iterator();
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
        return CollUtil.size(coll);
    }

    @Override
    public void reset() {
        this.iterator = coll == null ? CollUtil.emptyIterator() : coll.iterator();
    }
}
