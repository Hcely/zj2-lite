package org.zj2.lite.util.stream;

import java.util.Collection;

/**
 * DataStreams
 *
 * @author peijie.ye
 * @date 2023/2/21 10:51
 */
@SuppressWarnings("all")
public class DataStreams {
    private static final int DEF_PAGE_SIZE = 100;

    @SafeVarargs
    public static <T> DataStream<T> of(T... array) {
        return new DataArrayStream<>(array);
    }

    public static <T> DataStream<T> of(Collection<T> coll) {
        return new DataCollectionStream<>(coll);
    }

    public static <T> DataStream<T> of(DataPageReader.PageQuery<T> query) {
        return of(query, DEF_PAGE_SIZE);
    }

    public static <T> DataStream<T> of(DataPageReader.PageQuery<T> query, int pageSize) {
        return new DataPageReader<>(query, pageSize).stream();
    }

    public static <I extends Comparable, T> DataStream<T> of(Class<I> offsetType, DataOffsetReader.OffsetQuery<I, T> query,
            DataOffsetReader.OffsetGetter<I, T> offsetGetter) {
        return of(offsetType, query, offsetGetter, DEF_PAGE_SIZE);
    }

    public static <I extends Comparable, T> DataStream<T> of(Class<I> offsetType, DataOffsetReader.OffsetQuery<I, T> query,
            DataOffsetReader.OffsetGetter<I, T> offsetGetter, int pageSize) {
        return new DataOffsetReader<>(query, offsetGetter, pageSize).stream();
    }

    public static <I extends Comparable, T> DataStream<T> of(Class<I> offsetType, DataOffsetReader.OffsetQuery<I, T> query,
            DataOffsetReader.OffsetGetter<I, T> offsetGetter, I offset, int pageSize) {
        return new DataOffsetReader<>(query, offsetGetter, offset, pageSize).stream();
    }
}
