package org.zj2.lite.common.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface BeanPropertyContext {
    <T> T flag();

    Object rootBean();

    Object currentBean();

    String propertyName();

    boolean isSimplePropertyType();

    boolean isPropertyOfMap();

    boolean isPropertyOfBean();

    boolean isPropertyOfArray();

    Class<?> propertyType();

    Type propertyGenericType();

    <T extends Annotation> T propertyAnnotation(Class<T> annotationType);

    Object propertyValue();

    boolean propertyValue(Object value);
}