package org.zj2.lite.common.bean;

public interface BeanPropertyScanner {
    BeanPropertyScanner flag(Object flag);

    void scan(Object bean, BeanPropertyScanHandler propertyHandler);

    void scan(Object bean, BeanSimplePropertyScanHandler propertyHandler);
}
