package org.zj2.lite.sign;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.NumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *  StandardToStringFunc
 *
 * @author peijie.ye
 * @date 2022/11/28 0:36
 */
public class StandardToStringFunc implements Function<SignBuilder, String> {
    public static final StandardToStringFunc INSTANCE = new StandardToStringFunc();

    @Override
    public String apply(SignBuilder signBuilder) {
        TextStringBuilder sb = new TextStringBuilder(128 + 32 * CollUtil.size(signBuilder.getParams()));
        if (StringUtils.isNotEmpty(signBuilder.getPrefix())) {
            sb.append(signBuilder.getPrefix());
        }
        if (CollUtil.isNotEmpty(signBuilder.getParams())) {
            List<Map.Entry<String, Object>> list = new ArrayList<>(signBuilder.getParams().entrySet());
            CollUtil.sort(list, Map.Entry.comparingByKey());
            boolean first = true;
            for (Map.Entry<String, Object> e : list) {
                String value = serializeValue(e.getValue());
                if (StringUtils.isEmpty(value)) {continue;}
                if (!first) {sb.append('&');}
                sb.append(e.getKey()).append('=').append(value);
                first = false;
            }
        }
        if (StringUtils.isNotEmpty(signBuilder.getSuffix())) {
            sb.append(signBuilder.getSuffix());
        }
        return sb.toString();
    }

    protected String serializeValue(Object value) {
        if (value == null) {return null;}
        return value instanceof Number ? NumUtil.toStr((Number) value) : value.toString();
    }
}
