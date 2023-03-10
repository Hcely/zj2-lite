package org.zj2.lite.util.stream;

import org.apache.commons.lang3.ArrayUtils;
import org.zj2.lite.Resettable;

/**
 * TaskArray
 *
 * @author peijie.ye
 * @date 2023/2/17 11:57
 */
public class DataArrayStream<T> implements DataFixedStream<T>, Resettable {
    private final Object[] array;
    private int idx = 0;

    public DataArrayStream(Object[] array) {
        this.array = array == null ? ArrayUtils.EMPTY_OBJECT_ARRAY : array;
    }

    @Override
    public T next() {
        while(idx < array.length) {
            //noinspection unchecked
            T value = (T)array[idx];
            ++idx;
            if(value != null) { return value; }
        }
        return null;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public void reset() {
        idx = 0;
    }
}
