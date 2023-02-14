package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * ReflectUtil
 *
 * @author peijie.ye
 * @date 2023/2/14 23:21
 */
public class ReflectUtil {
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Method method = getMethod0(clazz, methodName, paramTypes);
        if (method != null) { return method; }
        Class<?> type = clazz.getSuperclass();
        while (type != null && type != Object.class) {
            method = getDeclaredMethod0(clazz, methodName, paramTypes);
            if (method != null) { return method; }
            type = type.getSuperclass();
        }
        return null;
    }

    private static Method getMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static Method getDeclaredMethod0(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        if (clazz == null) { return null; }
        Method method = findMethod0(clazz, methodName, paramTypes);
        if (method != null) { return method; }
        Class<?>[] iTypes = clazz.getInterfaces();
        for (Class<?> iType : iTypes) {
            method = findMethod(iType, methodName, paramTypes);
            if (method != null) { return method; }
        }
        return findMethod(clazz.getSuperclass(), methodName, paramTypes);
    }

    private static Method findMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (StringUtils.equals(m.getName(), methodName) && matchParamTypes(m, paramTypes)) {
                return m;
            }
        }
        return null;
    }

    private static boolean matchParamTypes(Method m, Class<?>[] paramTypes) {
        int size = paramTypes == null ? 0 : paramTypes.length;
        Class<?>[] methodParamTypes = m.getParameterTypes();
        if (methodParamTypes.length != size) { return false; }
        for (int i = 0; i < size; ++i) {
            Class<?> methodParamType = methodParamTypes[i];
            Class<?> paramType = paramTypes[i];
            if (methodParamType != paramType && methodParamType.isAssignableFrom(paramType)) {
                return false;
            }
        }
        return true;
    }
}
