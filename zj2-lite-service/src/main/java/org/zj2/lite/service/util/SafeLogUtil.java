package org.zj2.lite.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.common.annotation.CryptProperty;
import org.zj2.lite.common.annotation.SensitiveProperty;
import org.zj2.lite.common.bean.BeanDescriptor;
import org.zj2.lite.common.bean.BeanPropertyDescriptor;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.service.auth.AuthorityResource;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  SafeJSON
 *
 * @author peijie.ye
 * @date 2022/12/1 14:51
 */
public class SafeLogUtil {
    private static final SerializeFilter[] PROPERTY_FILTERS = {
            (ValueFilter) (object, name, value) -> isNotEmpty(value) && isSensitiveProperty(name) ? "****" : value};
    private static final SerializerFeature[] EMPTY_FEATURES = {};

    private static final String[] SENSITIVE_SUFFIX_WORDS = {"mobile", "phone", "email", "password"};

    private static final Set<String> SENSITIVE_PROPERTIES = ConcurrentHashMap.newKeySet(1024);

    private static boolean isNotEmpty(Object value) {
        if (value == null) { return false; }
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() > 0;
        } else {
            return true;
        }
    }

    public static Logger getLogger(Class<?> type) {
        return new SafeLogger(LoggerFactory.getLogger(type));
    }

    public static Logger getLogger(String loggerName) {
        return new SafeLogger(LoggerFactory.getLogger(loggerName));
    }

    public static void addSensitiveProperty(Class<?> type) {
        BeanDescriptor bd = PropertyUtil.getBeanDescriptor(type);
        if (bd == null) { return; }
        for (int idx = 0, size = bd.propertySize(); idx < size; ++idx) {
            BeanPropertyDescriptor pd = bd.propertyDescriptor(idx);
            if (isSensitiveProperty(pd)) { addSensitiveProperty(pd.propertyName()); }
        }
    }

    private static boolean isSensitiveProperty(BeanPropertyDescriptor pd) {
        return pd.annotation(CryptProperty.class) != null || pd.annotation(SensitiveProperty.class) != null
                || pd.annotation(AuthorityResource.class) != null;
    }

    public static void addSensitiveProperty(String name) {
        SENSITIVE_PROPERTIES.add(name);
    }

    public static boolean isSensitiveProperty(String name) {
        if (StringUtils.isEmpty(name)) { return false; }
        for (String suffix : SENSITIVE_SUFFIX_WORDS) {
            if (StringUtils.endsWithIgnoreCase(name, suffix)) { return true; }
        }
        return SENSITIVE_PROPERTIES.contains(name);
    }

    public static String toJSONStr(Object bean) {
        return bean == null ? null : JSON.toJSONString(bean, PROPERTY_FILTERS, EMPTY_FEATURES);
    }
}
