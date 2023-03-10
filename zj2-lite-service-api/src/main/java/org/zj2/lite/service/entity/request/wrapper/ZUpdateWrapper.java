package org.zj2.lite.service.entity.request.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.zj2.lite.common.function.PropGetter;

import java.util.LinkedHashMap;

/**
 * UpdateWrapper
 *
 * @author peijie.ye
 * @date 2022/11/27 15:43
 */
@Getter
public class ZUpdateWrapper<T> extends AbstractWrapper<T, ZUpdateWrapper<T>> {
    private static final long serialVersionUID = 7695434712267582650L;
    @JSONField(name = "up")
    @JsonProperty("up")
    private LinkedHashMap<String, UpdateField> updateFields;

    public ZUpdateWrapper<T> set(PropGetter<T, ?> prop, Object value) {
        return set(true, prop, value);
    }

    public ZUpdateWrapper<T> set(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.SET, value);
    }

    public ZUpdateWrapper<T> setIfAbsent(PropGetter<T, ?> prop, Object value) {
        return setIfAbsent(true, prop, value);
    }

    public ZUpdateWrapper<T> setIfAbsent(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.SET_IF_ABSENT, value);
    }

    public ZUpdateWrapper<T> setIfPresent(PropGetter<T, ?> prop, Object value) {
        return setIfPresent(true, prop, value);
    }

    public ZUpdateWrapper<T> setIfPresent(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.SET_IF_PRESENT, value);
    }

    public ZUpdateWrapper<T> min(PropGetter<T, ?> prop, Object value) {
        return min(true, prop, value);
    }

    public ZUpdateWrapper<T> min(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.MIN, value);
    }

    public ZUpdateWrapper<T> max(PropGetter<T, ?> prop, Object value) {
        return max(true, prop, value);
    }

    public ZUpdateWrapper<T> max(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.MAX, value);
    }

    public ZUpdateWrapper<T> increase(PropGetter<T, ?> prop, Object value) {
        return increase(true, prop, value);
    }

    public ZUpdateWrapper<T> increase(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.INCREASE, value);
    }

    public ZUpdateWrapper<T> decrease(PropGetter<T, ?> prop, Object value) {
        return decrease(true, prop, value);
    }

    public ZUpdateWrapper<T> decrease(boolean b, PropGetter<T, ?> prop, Object value) {
        return addUpdateField(b, prop, UpdateMode.DECREASE, value);
    }

    protected final ZUpdateWrapper<T> addUpdateField(boolean b, PropGetter<T, ?> prop, UpdateMode mode, Object value) {
        if(!b) { return this; }
        String name = getFieldName(prop);
        if(updateFields == null) { updateFields = new LinkedHashMap<>(5); }
        updateFields.put(name, new UpdateField(mode, value));
        return this;
    }
}
