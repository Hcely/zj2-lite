package org.zj2.lite.common.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * ReflectUtil
 *
 * @author peijie.ye
 * @date 2023/2/14 23:21
 */
@SuppressWarnings("all")
public class ReflectUtil {

    public static Field getField(Class<?> type, String fieldName) {
        Field field = FieldUtils.getField(type, fieldName);
        if(field != null && (isPublic(field.getModifiers()) || field.trySetAccessible())) { return field; }
        return null;
    }

    public static Constructor getConstructor(Class<?> type) {
        try {
            Constructor<?> constructor = type == null || type.isArray() || type.isInterface() || Modifier.isAbstract(type.getModifiers()) ?
                    null :
                    BeanUtils.getResolvableConstructor(type);
            if(constructor != null && (isPublic(constructor.getModifiers()) || constructor.trySetAccessible())) {
                return constructor;
            }
        } catch(Throwable e) {
        }
        return null;
    }

    private static boolean isPublic(int modifiers) {
        return Modifier.isPublic(modifiers);
    }


    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        if(clazz == null || StringUtils.isEmpty(methodName)) { return null; }
        return getMethod0(clazz, methodName, paramTypes == null ? ArrayUtils.EMPTY_CLASS_ARRAY : paramTypes);
    }

    private static Method getMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        Method method = getPublicMethod0(clazz, methodName, paramTypes);
        if(method != null) { return method; }
        Class<?> type = clazz.getSuperclass();
        while(type != null && type != Object.class) {
            method = getDeclaredMethod0(clazz, methodName, paramTypes);
            if(method != null) { return method; }
            type = type.getSuperclass();
        }
        return null;
    }

    private static Method getPublicMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch(NoSuchMethodException ignored) {
            return null;
        }
    }

    private static Method getDeclaredMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch(NoSuchMethodException ignored) {
            return null;
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        if(clazz == null || StringUtils.isEmpty(methodName)) { return null; }
        paramTypes = paramTypes == null ? ArrayUtils.EMPTY_CLASS_ARRAY : paramTypes;
        Method method = getMethod0(clazz, methodName, paramTypes);
        return method != null ? method : findMethod0(clazz, methodName, paramTypes);
    }

    private static Method findMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        if(clazz == null) { return null; }
        Method method = matchMethod0(clazz, methodName, paramTypes);
        if(method != null) { return method; }
        Class<?>[] iTypes = clazz.getInterfaces();
        for(Class<?> iType : iTypes) {
            if((method = matchMethod0(iType, methodName, paramTypes)) != null) { return method; }
        }
        if(clazz.isInterface()) { return null; }
        return findMethod0(clazz.getSuperclass(), methodName, paramTypes);
    }

    private static Method matchMethod0(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        for(Method m : clazz.getDeclaredMethods()) {
            if(matchParamTypes(m, methodName, paramTypes)) { return m; }
        }
        return null;
    }

    private static boolean matchParamTypes(Method m, String methodName, Class<?>[] paramTypes) {
        if(!StringUtils.equals(m.getName(), methodName)) { return false; }
        Class<?>[] targetParamTypes = m.getParameterTypes();
        int size = paramTypes.length;
        if(targetParamTypes.length != size) { return false; }
        for(int i = 0; i < size; ++i) {
            Class<?> targetParamType = targetParamTypes[i];
            Class<?> paramType = paramTypes[i];
            if(targetParamType != paramType && targetParamType.isAssignableFrom(paramType)) {
                return false;
            }
        }
        return true;
    }
}
