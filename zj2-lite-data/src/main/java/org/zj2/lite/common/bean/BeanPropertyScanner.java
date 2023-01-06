package org.zj2.lite.common.bean;

public interface BeanPropertyScanner {
    void scan(Object bean, BeanPropertyScanHandler propertyHandler);
}
