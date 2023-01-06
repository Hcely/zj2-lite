package org.zj2.lite.common.util;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeanUtils;
import org.zj2.lite.common.PropFunc;
import org.zj2.lite.common.bean.BeanDescriptor;
import org.zj2.lite.common.bean.BeanPropertyDescriptor;
import org.zj2.lite.common.bean.BeanPropertyScanHandler;
import org.zj2.lite.common.bean.BeanPropertyScanner;
import org.zj2.lite.common.bean.DefBeanPropertyScanner;
import org.zj2.lite.common.constant.NoneConstants;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取属性UTIL
 * <br>CreateDate 六月 22,2022
 * @author peijie.ye
 */
@SuppressWarnings("all")
public class PropertyUtil {
    private static final ThreadLocal<DefBeanPropertyScanner> SCANNER_THREAD_LOCAL = ThreadLocal.withInitial(
            DefBeanPropertyScanner::new);
    private static final Map<PropFunc, String> LAMBDA_PROP_NAMES = new HashMap<>(1024);

    public static <T> String getLambdaFieldName(PropFunc<T, ?> func) {
        String name = LAMBDA_PROP_NAMES.get(func);
        if (name == null) {
            synchronized (LAMBDA_PROP_NAMES) {
                name = LAMBDA_PROP_NAMES.computeIfAbsent(func, PropertyUtil::getLambdaFieldName0);
            }
        }
        return name == NoneConstants.NONE_STR ? "" : name;
    }

    private static <T> String getLambdaFieldName0(PropFunc<T, ?> func) {
        try {
            SerializedLambda lambda = (SerializedLambda) MethodUtils.invokeMethod(func, true, "writeReplace");
            String methodName = lambda.getImplMethodName();
            if (methodName.length() > 2 && StringUtils.startsWith(methodName, "is")) {
                return buildFieldName(methodName, 2);
            }
            if (methodName.length() > 3 && StringUtils.startsWithAny(methodName, "get", "set")) {
                return buildFieldName(methodName, 3);
            }
        } catch (Throwable e) {
        }
        return NoneConstants.NONE_STR;
    }

    private static String buildFieldName(String methodName, int idx) {
        StringBuilder sb = new StringBuilder(methodName.length() - idx).append(methodName, idx, methodName.length());
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    public static String[] getPathKeys(String key) {
        if (StringUtils.isEmpty(key)) { return NoneConstants.EMPTY_STRINGS; }
        String[] paths = StringUtils.split(key, ".[]");
        return paths == null || paths.length == 0 ? NoneConstants.EMPTY_STRINGS : paths;
    }

    public static BeanPropertyScanner createScanner() {
        return new DefBeanPropertyScanner();
    }

    public static void scanProperties(Object bean, BeanPropertyScanHandler propertyHandler) {
        if (bean == null || propertyHandler == null || BeanUtils.isSimpleValueType(bean.getClass())) { return; }
        DefBeanPropertyScanner scanner = SCANNER_THREAD_LOCAL.get();
        if (scanner.getRootBean() == null) { // 防止环
            scanner.scan(bean, propertyHandler);
        } else {
            createScanner().scan(bean, propertyHandler);
        }
    }

    public static <T> T getProperty(Object bean, String... paths) {
        if (paths == null || paths.length == 0) {
            return null;
        } else {
            Object value = bean;
            for (String pathKey : paths) {
                if (value == null) { break; }
                value = PropertyUtil.getProperty(value, pathKey);
            }
            return (T) value;
        }
    }

    public static <T> T getProperty(Object bean, String name) {
        if (bean == null || StringUtils.isEmpty(name)) { return null; }
        if (bean instanceof Map) {
            return (T) ((Map) bean).get(name);
        } else if (bean instanceof Collection) {
            try {
                int idx = Integer.parseInt(name);
                Collection coll = (Collection) bean;
                return idx > -1 && idx < coll.size() ? (T) IterableUtils.get((Collection) bean, idx) : null;
            } catch (Throwable e) { return null; }
        } else if (bean.getClass().isArray()) {
            try {
                int idx = Integer.parseInt(name);
                int length = Array.getLength(bean);
                return idx > -1 && idx < length ? (T) Array.get(bean, idx) : null;
            } catch (Throwable e) { return null; }
        } else {
            BeanPropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
            return descriptor == null ? null : (T) descriptor.read(bean);
        }
    }

    public static boolean setProperty(Object bean, String name, Object value) {
        if (bean == null || StringUtils.isEmpty(name)) { return false; }
        if (bean instanceof Map) {
            ((Map) bean).put(name, value);
            return true;
        } else {
            BeanPropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
            return descriptor != null && descriptor.write(bean, value);
        }
    }

    public static boolean hasProperyDescriptor(Class<?> type, String name) {
        return getPropertyDescriptor(type, name) != null;
    }

    public static BeanPropertyDescriptor getPropertyDescriptor(Class<?> type, String name) {
        return getBeanDescriptor(type).propertyDescriptor(name);
    }

    public static BeanDescriptor getBeanDescriptor(Class<?> type) {
        return BeanDescriptor.getBeanDescriptor(type);
    }
}
