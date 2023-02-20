package org.zj2.lite.util.stream;

import java.util.Collection;

/**
 * TaskPageReader
 *
 * @author peijie.ye
 * @date 2023/2/17 15:16
 */
public class DataPageReader<T> extends DataAbstarctReader<T> {
    private int pageNumber;
    private final PageQuery<T> query;

    public DataPageReader(PageQuery<T> query, int pageSize) {
        this(query, 1, pageSize);
    }

    public DataPageReader(PageQuery<T> query, int pageNumber, int pageSize) {
        super(pageSize);
        this.pageNumber = pageNumber;
        this.query = query;
    }

    @Override
    protected Collection<T> read0(int pageSize) {
        Collection<T> coll = query.query(pageNumber, pageSize);
        ++pageNumber;
        return coll;
    }

    public interface PageQuery<T> {
        Collection<T> query(int pageNumber, int pageSize);
    }
}
