package org.zj2.lite.batch;

import org.zj2.lite.common.util.CollUtil;

import java.util.Collection;

/**
 * TaskPageReader
 *
 * @author peijie.ye
 * @date 2023/2/17 15:16
 */
public class TaskPageReader<T> extends TaskAbstarctReader<T> {
    private int pageNumber;
    private final PageQuery<T> query;

    public TaskPageReader(PageQuery<T> query, int pageSize) {
        this(query, 1, pageSize);
    }

    public TaskPageReader(PageQuery<T> query, int pageNumber, int pageSize) {
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
