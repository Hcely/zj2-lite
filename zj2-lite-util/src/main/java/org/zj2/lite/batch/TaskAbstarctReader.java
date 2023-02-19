package org.zj2.lite.batch;

import org.zj2.lite.common.util.CollUtil;

import java.util.Collection;

/**
 * TaskAbstarctReader
 *
 * @author peijie.ye
 * @date 2023/2/17 15:31
 */
public abstract class TaskAbstarctReader<T> implements TaskReader<T> {
    private boolean hasMore;
    private final int pageSize;

    public TaskAbstarctReader(int pageSize) {
        this.pageSize = pageSize;
        this.hasMore = true;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Collection<T> read() {
        if (hasMore) {
            Collection<T> coll = read0(pageSize);
            hasMore = CollUtil.size(coll) >= pageSize;
            return coll;
        } else {
            return CollUtil.emptyList();
        }
    }

    protected abstract Collection<T> read0(int pageSize);

    @Override
    public boolean hasMore() {
        return hasMore;
    }
}
