package org.zj2.lite.common.bean;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@SuppressWarnings("all")
public class BeanPropertyDescriptor {
    private static final int ANNO_CACHE_SIZE = 8;
    private final boolean simplePropertyType;
    private final PropertyDescriptor propertyDescriptor;
    private final Field propertyField;
    private final Object[] annotationCaches;// 缓存

    BeanPropertyDescriptor(PropertyDescriptor propertyDescriptor, Field propertyField) {
        this.propertyDescriptor = propertyDescriptor;
        this.propertyField = propertyField;
        this.simplePropertyType = BeanUtils.isSimpleValueType(propertyType());
        this.annotationCaches = new Object[ANNO_CACHE_SIZE];
        if(propertyField != null) { propertyField.trySetAccessible(); }
        if(propertyDescriptor.getReadMethod() != null) { propertyDescriptor.getReadMethod().trySetAccessible(); }
        if(propertyDescriptor.getWriteMethod() != null) { propertyDescriptor.getWriteMethod().trySetAccessible(); }
    }

    public boolean isSimplePropertyType() {
        return simplePropertyType;
    }

    public PropertyDescriptor propertyDescriptor() {
        return propertyDescriptor;
    }

    public Field propertyField() {
        return propertyField;
    }

    public String propertyName() {
        return propertyDescriptor.getName();
    }

    public Method readMethod() {
        return propertyDescriptor.getReadMethod();
    }

    public Method writeMethod() {
        return propertyDescriptor.getWriteMethod();
    }

    public Class<?> propertyType() {
        Field field = propertyField();
        return field == null ? propertyDescriptor.getPropertyType() : field.getType();
    }

    public Type propertyGenericType() {
        Field field = propertyField();
        if(field != null) { return field.getGenericType(); }
        Method method = propertyDescriptor.getReadMethod();
        if(method != null) { return method.getGenericReturnType(); }
        if((method = propertyDescriptor.getWriteMethod()) != null) { return method.getGenericParameterTypes()[0]; }
        return null;
    }

    private int getCacheIdx(Class<?> annotationType) {
        return annotationType.hashCode() & (ANNO_CACHE_SIZE - 1);
    }

    public <T extends Annotation> T annotation(Class<T> annotationType) {
        final int idx = getCacheIdx(annotationType);
        final Object[] caches = annotationCaches;
        final Object cache = caches[idx];
        if(annotationType == cache) { return null; }
        if(cache != null && cache.getClass() == annotationType) { return (T)cache; }
        //
        T annotation;
        if((annotation = annotationFromReadMethod(annotationType)) != null) {
            caches[idx] = annotation;
            return annotation;
        } else if((annotation = annotationFromWriteMethod(annotationType)) != null) {
            caches[idx] = annotation;
            return annotation;
        } else if((annotation = annotationFromField(annotationType)) != null) {
            caches[idx] = annotation;
            return annotation;
        } else {
            caches[idx] = annotationType;
            return null;
        }
    }

    public <T extends Annotation> T annotationFromField(Class<T> annotationType) {
        Field field = propertyField();
        T annotation = field != null ? field.getAnnotation(annotationType) : null;
        return annotation;
    }

    public <T extends Annotation> T annotationFromReadMethod(Class<T> annotationType) {
        Method method = propertyDescriptor.getReadMethod();
        T annotation = method != null ? method.getAnnotation(annotationType) : null;
        return annotation;
    }

    public <T extends Annotation> T annotationFromWriteMethod(Class<T> annotationType) {
        Method method = propertyDescriptor.getWriteMethod();
        T annotation = method != null ? method.getAnnotation(annotationType) : null;
        return annotation;
    }

    public Object read(Object bean) {
        if(bean == null) { return null; }
        try {
            Method readMethod = propertyDescriptor.getReadMethod();
            if(readMethod != null) { return readMethod.invoke(bean); }
            Field field = propertyField();
            if(field != null) { return field.get(bean); }
        } catch(Throwable ignored) {
        }
        return null;
    }

    public boolean write(Object bean, Object value) {
        if(bean == null) { return false; }
        if(!ClassUtils.isAssignableValue(propertyType(), value)) { return false; }
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if(writeMethod != null) {
                writeMethod.invoke(bean, value);
                return true;
            } else {
                Field field = propertyField();
                if(field != null) {
                    field.set(bean, value);
                    return true;
                }
            }
        } catch(Throwable ignored) {
        }
        return false;
    }

    public Object readByMethod(Object bean) {
        if(bean == null) { return null; }
        Method readMethod = propertyDescriptor.getReadMethod();
        if(readMethod != null) {
            try {
                return readMethod.invoke(bean);
            } catch(Throwable ignored) {
            }
        }
        return null;
    }

    public boolean writeByMethod(Object bean, Object value) {
        if(bean == null) { return false; }
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if(writeMethod != null && ClassUtils.isAssignableValue(writeMethod.getParameterTypes()[0], value)) {
            try {
                writeMethod.invoke(bean, value);
                return true;
            } catch(Throwable ignored) {
            }
        }
        return false;
    }

    public Object readByField(Object bean) {
        if(bean == null) { return null; }
        Field field = propertyField();
        if(field != null) {
            try {
                return field.get(bean);
            } catch(Throwable ignored) {
            }
        }
        return null;
    }

    public boolean writeByField(Object bean, Object value) {
        if(bean == null) { return false; }
        Field field = propertyField();
        if(field != null && ClassUtils.isAssignableValue(field.getType(), value)) {
            try {
                field.set(bean, value);
                return true;
            } catch(Throwable ignored) {
            }
        }
        return false;
    }
}