package org.zj2.lite.service;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.common.util.NumUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.entity.request.wrapper.PropCondition;
import org.zj2.lite.service.entity.request.wrapper.UpdateField;
import org.zj2.lite.service.entity.request.wrapper.ZQueryWrapper;
import org.zj2.lite.service.entity.request.wrapper.ZUpdateWrapper;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  BaseServiceUtil
 *
 * @author peijie.ye
 * @date 2022/11/27 16:35
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class WrapperUtil {
    private static final List<String> EMPTY_COLL = Collections.singletonList("_$NULL$_");

    public static <DTO, DO> Wrapper<DO>//NOSONAR
    buildCondition(Map<String, String> tableFieldMap, ZQueryWrapper<DTO> wrapper) {
        QueryWrapper<DO> result = Wrappers.query();
        for (PropCondition c : CollUtil.of(wrapper.getConditions())) {
            addCondition(tableFieldMap, c, result);
        }
        return result;
    }

    public static <DTO, DO> QueryWrapper<DO>//NOSONAR
    buildQueryCondition(String keyColumn, Map<String, String> tableFieldMap, ZQueryWrapper<DTO> wrapper) {
        QueryWrapper<DO> result = Wrappers.query();
        if (CollUtil.isNotEmpty(wrapper.getSelectProps())) {
            result.select(getColumns(tableFieldMap, wrapper.getSelectProps()));
        }
        for (PropCondition c : CollUtil.of(wrapper.getConditions())) {
            addCondition(tableFieldMap, c, result);
        }
        if (CollUtil.isNotEmpty(wrapper.getSorts())) {
            result.orderBy(true, wrapper.isSortAsc(), getColumnList(tableFieldMap, wrapper.getSorts()));
        } else {
            result.orderByAsc(keyColumn);
        }
        if (wrapper.getOffset() >= 0 && wrapper.getSize() > 0) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("LIMIT ").append(wrapper.getOffset()).append(',').append(wrapper.getSize());
            if (wrapper.isForUpdate()) { sb.append(" FOR UPDATE"); }
            result.last(sb.toString());
        } else if (wrapper.isForUpdate()) {
            result.last("FOR UPDATE");
        }
        return result;
    }

    public static <DTO, DO> UpdateWrapper<DO>//NOSONAR
    buildUpdate(Map<String, String> tableFieldMap, ZUpdateWrapper<DTO> wrapper) {
        UpdateWrapper<DO> result = Wrappers.update();
        for (PropCondition c : CollUtil.of(wrapper.getConditions())) {
            addCondition(tableFieldMap, c, result);
        }
        Map<String, UpdateField> updateFields = wrapper.getUpdateFields();
        if (updateFields != null) {
            //
            updateFields.remove(ServiceConstants.APP_CODE);
            updateFields.remove(ServiceConstants.ORG_CODE);
            updateFields.remove(ServiceConstants.ORG_GROUP_CODE);
            updateFields.remove(ServiceConstants.CREATE_TIME);
            updateFields.remove(ServiceConstants.CREATE_USER);
            updateFields.remove(ServiceConstants.CREATE_USER_NAME);
            updateFields.remove(ServiceConstants.UPDATE_TIME);
            updateFields.remove(ServiceConstants.UPDATE_USER);
            updateFields.remove(ServiceConstants.UPDATE_USER_NAME);
            //
            for (Map.Entry<String, UpdateField> e : updateFields.entrySet()) {
                addUpdateField(tableFieldMap, e.getKey(), e.getValue(), result);
            }
        }
        String column = tableFieldMap.get(ServiceConstants.UPDATE_TIME);
        if (column != null) { result.set(column, LocalDateTime.now()); }
        //
        column = tableFieldMap.get(ServiceConstants.UPDATE_USER);
        if (column != null) { result.set(column, AuthenticationContext.currentUserId()); }
        //
        column = tableFieldMap.get(ServiceConstants.UPDATE_USER);
        if (column != null) { result.set(column, AuthenticationContext.currentUserName()); }
        return result;
    }

    private static String[] getColumns(Map<String, String> tableFieldMap, Collection<String> props) {
        String[] columns = new String[props.size()];
        int i = 0;
        for (String prop : props) {
            columns[i++] = getColumnName(tableFieldMap, prop);
        }
        return columns;
    }

    private static List<String> getColumnList(Map<String, String> tableFieldMap, Collection<String> props) {
        List<String> list = new ArrayList<>(props.size());
        for (String prop : props) {
            list.add(getColumnName(tableFieldMap, prop));
        }
        return list;
    }

    private static void addCondition(Map<String, String> tableFieldMap, PropCondition condition,
            AbstractWrapper wrapper) {
        if (condition.getMode() == null) { return; }
        String column = getColumnName(tableFieldMap, condition.getName());
        switch (condition.getMode()) {
            case EQ:
                wrapper.eq(column, condition.getValue1());
                break;
            case NE:
                wrapper.ne(column, condition.getValue1());
                break;
            case IS_NULL:
                wrapper.isNull(column);
                break;
            case IS_NOT_NULL:
                wrapper.isNotNull(column);
                break;
            case IN:
                wrapper.in(column, CollUtil.isEmpty(condition.getValues()) ? EMPTY_COLL : condition.getValues());
                break;
            case NOT_IN:
                wrapper.notIn(column, CollUtil.isEmpty(condition.getValues()) ? EMPTY_COLL : condition.getValues());
                break;
            case GT:
                wrapper.gt(column, condition.getValue1());
                break;
            case LT:
                wrapper.lt(column, condition.getValue1());
                break;
            case GTE:
                wrapper.ge(column, condition.getValue1());
                break;
            case LTE:
                wrapper.le(column, condition.getValue1());
                break;
            case LIKE:
                wrapper.like(column, condition.getValue1());
                break;
            case LIKE_RIGHT:
                wrapper.likeRight(column, condition.getValue1());
                break;
            case LIKE_LEFT:
                wrapper.likeLeft(column, condition.getValue1());
                break;
            case NOT_LIKE:
                wrapper.notLike(column, condition.getValue1());
                break;
            case BETWEEN:
                wrapper.between(column, condition.getValue1(), condition.getValue2());
                break;
            case NOT_BETWEEN:
                wrapper.notBetween(column, condition.getValue1(), condition.getValue2());
                break;
        }
    }

    private static void addUpdateField(Map<String, String> tableFieldMap, String name, UpdateField field,
            UpdateWrapper wrapper) {
        if (field.getMode() == null) { return; }
        String column = getColumnName(tableFieldMap, name);
        switch (field.getMode()) {
            case SET:
                wrapper.set(column, field.getValue());
                break;
            case SET_IF_ABSENT:
                wrapper.setSql(StrUtil.format("`{0}`=IF(`{0}` IS NULL OR `{0}`='',{1},`{0}`)", column,
                        getSqlValue(field.getValue())));
                break;
            case SET_IF_PRESENT:
                wrapper.setSql(StrUtil.format("`{0}`=IF(`{0}` IS NOT NULL OR `{0}`!='',{1},`{0}`)", column,
                        getSqlValue(field.getValue())));
                break;
            case MIN:
                wrapper.setSql(StrUtil.format("`{0}`=IF(`{0}` IS NULL OR `{0}`>{1},{1},`{0}`)", column,
                        getSqlValue(field.getValue())));
                break;
            case MAX:
                wrapper.setSql(StrUtil.format("`{0}`=IF(`{0}` IS NULL OR `{0}`<{1},{1},`{0}`)", column,
                        getSqlValue(field.getValue())));
                break;
            case INCREASE:
                wrapper.setSql(
                        StrUtil.format("`{0}`=IF(`{0}` IS NULL,{1},`{0}`+{1})", column, getSqlValue(field.getValue())));
                break;
            case DECREASE:
                wrapper.setSql(
                        StrUtil.format("`{0}`=IF(`{0}` IS NULL,{1},`{0}`-{1})", column, getSqlValue(field.getValue())));
                break;
        }
    }

    private static String getSqlValue(Object value) {
        if (value == null) { return "null"; }
        if (value instanceof Number) {
            return NumUtil.toStr((Number) value);
        } else if (value instanceof Date) {
            return StrUtil.concat("'", DateUtil.format((Date) value), "'");
        } else if (value instanceof TemporalAccessor) {
            return StrUtil.concat("'", DateUtil.format((TemporalAccessor) value), "'");
        } else {
            String valueStr = value.toString();
            TextStringBuilder sb = new TextStringBuilder(8 + valueStr.length());
            sb.append('\'');
            for (int i = 0, len = valueStr.length(); i < len; ++i) {
                char c = valueStr.charAt(i);
                if (c == '\'') { sb.append('\\'); }
                sb.append(c);
            }
            sb.append('\'');
            return sb.toString();
        }
    }

    private static String getColumnName(Map<String, String> tableFieldMap, String propName) {
        String column = tableFieldMap.get(propName);
        if (column == null) { throw new ZError(propName + "-字段不存在!"); }
        return column;
    }
}
