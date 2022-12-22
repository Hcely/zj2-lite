package org.zj2.lite.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.zj2.lite.common.CodeEnum;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.NumUtil;

import java.math.BigDecimal;

/**
 *  JSConfigKey
 *
 * @author peijie.ye
 * @date 2022/12/4 0:52
 */
@Getter
public class JSONKey<T> implements CodeEnum<String> {
    public static JSONKey<Integer> intParam(String code) {
        return new JSONKey<>(code, Integer.class);
    }

    public static JSONKey<Integer> intParam(String code, int defaultValue) {
        return new JSONKey<>(code, Integer.class, defaultValue);
    }

    public static JSONKey<Long> longParam(String code) {
        return new JSONKey<>(code, Long.class);
    }

    public static JSONKey<Long> longParam(String code, Long defaultValue) {
        return new JSONKey<>(code, Long.class, defaultValue);
    }

    public static JSONKey<BigDecimal> numParam(String code) {
        return new JSONKey<>(code, BigDecimal.class);
    }

    public static JSONKey<BigDecimal> numParam(String code, Number defaultValue) {
        return new JSONKey<>(code, BigDecimal.class, NumUtil.of(defaultValue, false));
    }

    public static JSONKey<Boolean> booleanParam(String code) {
        return new JSONKey<>(code, Boolean.class);
    }

    public static JSONKey<Boolean> booleanParam(String code, boolean defaultValue) {
        return new JSONKey<>(code, Boolean.class, defaultValue);
    }

    public static JSONKey<String> strParam(String code) {
        return new JSONKey<>(code, String.class);
    }

    public static JSONKey<String> strParam(String code, String defaultValue) {
        return new JSONKey<>(code, String.class, defaultValue);
    }

    private final String code;
    private final Class<T> paramType;
    private final T defaultValue;

    public JSONKey(String code, Class<T> paramType) {
        this(code, paramType, null);
    }

    public JSONKey(String code, Class<T> paramType, T defaultValue) {
        this.code = code;
        this.paramType = paramType;
        this.defaultValue = defaultValue;
    }

    public T getValue(JSONObject jsonConfig) {
        return getValue(jsonConfig, defaultValue);
    }

    public T getValue(JSONObject jsonConfig, T defaultValue) {
        if (jsonConfig == null) {return defaultValue;}
        T value;
        if (paramType == Boolean.class) {
            //noinspection unchecked
            value = (T) BooleanUtil.parse(jsonConfig.get(code));
        } else {
            value = jsonConfig.getObject(code, paramType);
        }
        return value == null ? defaultValue : value;
    }

    public void setValue(JSONObject jsonConfig, T value) {
        if (jsonConfig != null) {
            if (value == null) {
                jsonConfig.remove(code);
            } else if (paramType == Boolean.class) {
                jsonConfig.put(code, value == Boolean.TRUE ? 1 : 0);
            } else {
                jsonConfig.put(code, value);
            }
        }
    }

}
