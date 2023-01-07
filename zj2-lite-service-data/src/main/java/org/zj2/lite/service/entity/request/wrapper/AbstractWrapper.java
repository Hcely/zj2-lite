package org.zj2.lite.service.entity.request.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.CodeEnum;
import org.zj2.lite.common.PropFunc;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  Condition
 *
 * @author peijie.ye
 * @date 2022/11/25 11:29
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Getter
public abstract class AbstractWrapper<T, W extends AbstractWrapper> implements Serializable {
    private static final long serialVersionUID = 4191953336206744062L;
    @JSONField(name = "cds")
    @JsonProperty("cds")
    private List<PropCondition> conditions;
    @JSONField(name = "iemy")
    @JsonProperty("iemy")
    private boolean ignoreEmpty;

    public W ignoreEmpty() {
        return ignoreEmpty(true);
    }

    public W ignoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
        return (W) this;
    }

    protected boolean allowCondition(Object v) {
        if (ignoreEmpty) {
            if (v == null) { return false; }
            if (v instanceof CharSequence) { return ((CharSequence) v).length() > 0; }
            if (v instanceof Collection) { return !((Collection<?>) v).isEmpty(); }
            if (v.getClass().isArray()) { return Array.getLength(v) > 0; }
        }
        return true;
    }

    public W eq(PropFunc<T, ?> prop, Object value) {
        return eq(allowCondition(value), prop, value);
    }

    public W eq(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.EQ, value, null, null);
    }

    public W ne(PropFunc<T, ?> prop, Object value) {
        return ne(allowCondition(value), prop, value);
    }

    public W ne(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.NE, value, null, null);
    }

    public W isNull(PropFunc<T, ?> prop) {
        return isNull(true, prop);
    }

    public W isNull(boolean b, PropFunc<T, ?> prop) {
        return addCondition(b, prop, WhereMode.IS_NULL, null, null, null);
    }

    public W isNotNull(PropFunc<T, ?> prop) {
        return isNotNull(true, prop);
    }

    public W isNotNull(boolean b, PropFunc<T, ?> prop) {
        return addCondition(b, prop, WhereMode.IS_NOT_NULL, null, null, null);
    }

    public W in(PropFunc<T, ?> prop, Object... values) {
        return in(allowCondition(values), prop, values);
    }

    public W in(boolean b, PropFunc<T, ?> prop, Object... values) {
        return in(b, prop, values == null ? null : List.of(values));
    }

    public W in(PropFunc<T, ?> prop, Collection<Object> values) {
        return in(allowCondition(values), prop, values);
    }

    public W in(boolean b, PropFunc<T, ?> prop, Collection<Object> values) {
        return addCondition(b, prop, WhereMode.IN, null, null, values);
    }

    public W notIn(PropFunc<T, ?> prop, Object... values) {
        return notIn(allowCondition(values), prop, values);
    }

    public W notIn(boolean b, PropFunc<T, ?> prop, Object... values) {
        return notIn(b, prop, values == null ? null : List.of(values));
    }

    public W notIn(PropFunc<T, ?> prop, Collection<Object> values) {
        return notIn(allowCondition(values), prop, values);
    }

    public W notIn(boolean b, PropFunc<T, ?> prop, Collection<Object> values) {
        return addCondition(b, prop, WhereMode.NOT_IN, null, null, values);
    }

    public W gt(PropFunc<T, ?> prop, Object value) {
        return gt(allowCondition(value), prop, value);
    }

    public W gt(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.GT, value, null, null);
    }

    public W lt(PropFunc<T, ?> prop, Object value) {
        return lt(allowCondition(value), prop, value);
    }

    public W lt(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.LT, value, null, null);
    }

    public W gte(PropFunc<T, ?> prop, Object value) {
        return gte(allowCondition(value), prop, value);
    }

    public W gte(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.GTE, value, null, null);
    }

    public W lte(PropFunc<T, ?> prop, Object value) {
        return lte(allowCondition(value), prop, value);
    }

    public W lte(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.LTE, value, null, null);
    }

    public W like(PropFunc<T, ?> prop, Object value) {
        return like(allowCondition(value), prop, value);
    }

    public W like(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.LIKE, value, null, null);
    }

    public W likeRight(PropFunc<T, ?> prop, Object value) {
        return likeRight(allowCondition(value), prop, value);
    }

    public W likeRight(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.LIKE_RIGHT, value, null, null);
    }

    public W likeLeft(PropFunc<T, ?> prop, Object value) {
        return likeLeft(allowCondition(value), prop, value);
    }

    public W likeLeft(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.LIKE_LEFT, value, null, null);
    }

    public W notLike(PropFunc<T, ?> prop, Object value) {
        return notLike(allowCondition(value), prop, value);
    }

    public W notLike(boolean b, PropFunc<T, ?> prop, Object value) {
        return addCondition(b, prop, WhereMode.NOT_LIKE, value, null, null);
    }

    public W between(PropFunc<T, ?> prop, Object value1, Object value2) {
        return between(allowCondition(value1) && allowCondition(value2), prop, value1, value2);
    }

    public W between(boolean b, PropFunc<T, ?> prop, Object value1, Object value2) {
        return addCondition(b, prop, WhereMode.BETWEEN, value1, value2, null);
    }

    public W notBetween(PropFunc<T, ?> prop, Object value1, Object value2) {
        return notBetween(allowCondition(value1) && allowCondition(value2), prop, value1, value2);
    }

    public W notBetween(boolean b, PropFunc<T, ?> prop, Object value1, Object value2) {
        return addCondition(b, prop, WhereMode.NOT_BETWEEN, value1, value2, null);
    }

    protected final W addCondition(boolean b, PropFunc<T, ?> prop, WhereMode mode, Object value1, Object value2,
            Collection<Object> values) {
        if (!b) { return (W) this; }
        String name = getFieldName(prop);
        if (conditions == null) { conditions = new ArrayList<>(); }
        if (CollUtil.getFirst(values) instanceof CodeEnum) {
            values = CollUtil.toList(values, AbstractWrapper::convertVal);
        }
        conditions.add(new PropCondition(name, mode, convertVal(value1), convertVal(value2), values));
        return (W) this;
    }


    protected static String getFieldName(PropFunc<?, ?> prop) {
        String fieldName = PropertyUtil.getLambdaFieldName(prop);
        if (StringUtils.isEmpty(fieldName)) { throw new ZError("无法找到参数名称"); }
        return fieldName;
    }

    private static Object convertVal(Object v) {
        return v instanceof CodeEnum ? ((CodeEnum<?>) v).getCode() : v;
    }
}
