package org.zj2.lite.util.stream;

import org.zj2.lite.common.util.CollUtil;

import java.util.Collection;

/**
 * TaskOffsetReader
 *
 * @author peijie.ye
 * @date 2023/2/17 15:28
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataOffsetReader<I extends Comparable, T> extends DataAbstarctReader<T> {
    private final OffsetQuery<I, T> query;
    private final OffsetGetter<I, T> offsetGetter;
    private I offset;

    public DataOffsetReader(OffsetQuery<I, T> query, OffsetGetter<I, T> offsetGetter, int pageSize) {
        this(query, offsetGetter, null, pageSize);
    }

    public DataOffsetReader(OffsetQuery<I, T> query, OffsetGetter<I, T> offsetGetter, I offset, int pageSize) {
        super(pageSize);
        this.query = query;
        this.offsetGetter = offsetGetter;
        this.offset = offset;
    }

    @Override
    protected Collection<T> read0(int pageSize) {
        Collection<T> coll = query.query(offset, pageSize);
        for (T data : CollUtil.of(coll)) {
            if (data != null) {
                I newOffset = offsetGetter.get(data);
                if (isLessThen(offset, newOffset)) {
                    offset = newOffset;
                }
            }
        }
        return coll;
    }


    protected boolean isLessThen(I offset, I newOffset) {
        if (offset == null) { return true; }
        if (newOffset == null) { return false; }
        if (offset == newOffset) { return false; }
        return offset.compareTo(newOffset) < 0;
    }

    public interface OffsetQuery<I extends Comparable, T> {
        Collection<T> query(I offset, int size);
    }

    public interface OffsetGetter<I extends Comparable, T> {
        I get(T data);
    }
}
