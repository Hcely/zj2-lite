package org.zj2.lite.service.entity.request.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.zj2.lite.common.function.PropGetter;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.entity.request.PageRequest;

import java.util.LinkedHashSet;
import java.util.List;

/**
 *  Condition
 *
 * @author peijie.ye
 * @date 2022/11/25 11:29
 */
@SuppressWarnings("unchecked")
@Getter
public class ZQueryWrapper<T> extends AbstractWrapper<T, ZQueryWrapper<T>> {
    private static final long serialVersionUID = 4191953336206744062L;
    @JSONField(name = "sl")
    @JsonProperty("sl")
    private LinkedHashSet<String> selectProps;
    @JSONField(name = "asc")
    @JsonProperty("asc")
    private boolean sortAsc;
    @JSONField
    @JsonProperty
    private List<String> sorts;
    @JSONField(name = "off")
    @JsonProperty("off")
    private int offset = 0;
    @JSONField
    @JsonProperty
    private int size = -1;
    @JSONField(name = "fu")
    @JsonProperty("fu")
    boolean forUpdate = false;

    public ZQueryWrapper<T> select(PropGetter<T, ?> prop) {
        if (prop == null) { return this; }
        String fieldName = getFieldName(prop);
        if (selectProps == null) { selectProps = new LinkedHashSet<>(); }
        selectProps.add(fieldName);
        return this;
    }

    public ZQueryWrapper<T> select(PropGetter<T, ?>... props) {
        if (props != null && props.length > 0) {
            for (PropGetter<T, ?> prop : props) {
                select(prop);
            }
        }
        return this;
    }

    public ZQueryWrapper<T> orderByAsc(PropGetter<T, ?>... props) {
        return orderBy(true, true, props);
    }

    public ZQueryWrapper<T> orderByAsc(boolean b, PropGetter<T, ?>... props) {
        return orderBy(b, true, props);
    }

    public ZQueryWrapper<T> orderByDesc(PropGetter<T, ?>... props) {
        return orderBy(true, false, props);
    }

    public ZQueryWrapper<T> orderByDesc(boolean b, PropGetter<T, ?>... props) {
        return orderBy(b, false, props);
    }

    public ZQueryWrapper<T> orderBy(boolean asc, PropGetter<T, ?>... props) {
        return orderBy(true, asc, props);
    }

    public ZQueryWrapper<T> orderBy(boolean b, boolean asc, PropGetter<T, ?>... props) {
        if (!b) { return this; }
        this.sortAsc = asc;
        this.sorts = CollUtil.toList(props, AbstractWrapper::getFieldName);
        return this;
    }

    public ZQueryWrapper<T> orderByAsc(PropGetter<T, ?> prop) {
        return orderBy(true, true, prop);
    }

    public ZQueryWrapper<T> orderByAsc(boolean b, PropGetter<T, ?> prop) {
        return orderBy(b, true, prop);
    }

    public ZQueryWrapper<T> orderByDesc(PropGetter<T, ?> prop) {
        return orderBy(true, false, prop);
    }

    public ZQueryWrapper<T> orderByDesc(boolean b, PropGetter<T, ?> prop) {
        return orderBy(b, false, prop);
    }

    public ZQueryWrapper<T> orderBy(boolean asc, PropGetter<T, ?> prop) {
        return orderBy(true, asc, prop);
    }

    public ZQueryWrapper<T> orderBy(boolean b, boolean asc, PropGetter<T, ?> prop) {
        if (!b) { return this; }
        this.sortAsc = asc;
        this.sorts = CollUtil.singletonList(getFieldName(prop));
        return this;
    }


    public ZQueryWrapper<T> limit(int size) {
        return limit(0, size);
    }

    public ZQueryWrapper<T> limitPage(PageRequest request) {
        if (request != null) {
            limitPage(request.getPageNumber(), request.getPageSize());
        }
        return this;
    }

    public ZQueryWrapper<T> limitPage(Integer pageNumber, Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return limit(0, -1);
        } else {
            int num = pageNumber == null || pageNumber < 1 ? 1 : pageNumber;
            return limit((num - 1) * pageSize, pageSize);
        }
    }

    public ZQueryWrapper<T> limit(int offset, int size) {
        this.offset = offset;
        this.size = size;
        return this;
    }

    public ZQueryWrapper<T> forUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
        return this;
    }
}
