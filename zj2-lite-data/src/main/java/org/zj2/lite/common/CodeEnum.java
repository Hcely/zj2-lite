package org.zj2.lite.common;


import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * CodeDescEnum
 * <br>CreateDate 六月 27,2021
 * @author peijie.ye
 * @since 1.0
 */
public interface CodeEnum<T> {
    /**
     *  编码
     */
    T getCode();

    /**
     *
     */
    default String getDesc() {
        return String.valueOf(getCode());
    }

    default boolean eq(Object code) {
        if (code == null) { return false; }
        if (code == this) { return true; }
        if (code instanceof CodeEnum) {
            code = ((CodeEnum<?>) code).getCode();
        }
        T selfCode = getCode();
        if (selfCode == code) { return true; }
        if (selfCode instanceof Number && code instanceof Number) {
            return ((Number) selfCode).intValue() == ((Number) code).intValue();
        }
        return selfCode.equals(code) || selfCode.toString().equalsIgnoreCase(code.toString());
    }

    @SuppressWarnings("unchecked")
    class EnumUtil {
        private static final Set<Class<?>> enumTypes = new HashSet<>(128);
        private static final Map<String, Object> enums = new HashMap<>(512);

        public static <E extends CodeEnum<?>> E get(Class<?> enumType, Object code) {
            if (code == null) { return null; }
            tryInitialize(enumType);
            return (E) enums.get(getKey(enumType, code));
        }

        public static <E extends CodeEnum<?>> String getDesc(Class<E> enumType, Object code) {
            E e = get(enumType, code);
            return e == null ? "未知(" + code + ')' : e.getDesc();
        }

        private static void tryInitialize(Class<?> enumType) {
            if (enumTypes.contains(enumType)) { return; }
            synchronized (enumTypes) { if (enumTypes.add(enumType)) { initEnums(enumType); } }
        }

        private static String getKey(Class<?> enumType, Object code) {
            StringBuilder sb = new StringBuilder(enumType.getName()).append('.');
            if (code instanceof Number) {
                sb.append(((Number) code).intValue());
            } else {
                sb.append(code);
            }
            return sb.toString();
        }

        private static void initEnums(Class<?> enumType) {
            if (Enum.class.isAssignableFrom(enumType)) {
                if (CodeEnum.class.isAssignableFrom(enumType)) {
                    Object[] values = enumType.getEnumConstants();
                    if (values != null) {
                        for (Object value : values) {
                            addEnum(enumType, (CodeEnum<Object>) value);
                        }
                    }
                }
            } else {
                for (Field f : FieldUtils.getAllFieldsList(enumType)) { addEnum(enumType, f); }
                // 处理interface的
                for (Field f : enumType.getFields()) { addEnum(enumType, f); }
            }
        }

        private static void addEnum(Class<?> enumType, Field f) {
            int m = f.getModifiers();
            if (Modifier.isStatic(m) && Modifier.isFinal(m) && CodeEnum.class.isAssignableFrom(f.getType())) {
                try {
                    Object value = FieldUtils.readStaticField(f, true);
                    if (value != null) {
                        addEnum(enumType, (CodeEnum<Object>) value);
                    }
                } catch (IllegalAccessException ignored) {
                    //NOTHING
                }
            }
        }

        private static void addEnum(Class<?> enumType, CodeEnum<Object> e) {
            if (e != null) { enums.put(getKey(enumType, e.getCode()), e); }
        }
    }

}
