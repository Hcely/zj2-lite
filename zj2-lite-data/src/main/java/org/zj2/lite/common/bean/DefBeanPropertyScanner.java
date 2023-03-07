package org.zj2.lite.common.bean;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class DefBeanPropertyScanner implements BeanPropertyScanner, BeanPropertyContext {
    private static final Object LAZY_READ = new Object();
    private SoftReference<Set<Integer>> beanIdentifySetRef;
    private Set<Integer> beanIdentifySet;
    private Object rootBean;
    private BeanPropertyScanHandler propertyHandler;
    private BeanSimplePropertyScanHandler simplePropertyHandler;

    private Object currentBean;
    //
    private boolean simplePropertyType;
    private BeanPropertyDescriptor propertyDescriptor;
    private Map.Entry propertyMapEntry;
    private Iterator propertyIterator;
    private int propertyIdx = -1;
    private Class<?> propertyArrayComponentType;
    private Object propertyValue;

    public Object getRootBean() {
        return rootBean;
    }

    @Override
    public void scan(Object bean, BeanPropertyScanHandler propertyHandler) {
        if (propertyHandler == null) { return; }
        scan0(bean, propertyHandler, null);
    }

    @Override
    public void scan(Object bean, BeanSimplePropertyScanHandler propertyHandler) {
        if (propertyHandler == null) { return; }
        scan0(bean, null, propertyHandler);
    }

    private void scan0(Object bean, BeanPropertyScanHandler propertyHandler,
            BeanSimplePropertyScanHandler simplePropertyHandler) {
        try {
            if (bean == null || BeanUtils.isSimpleValueType(bean.getClass())) { return; }
            this.beanIdentifySet = loadBeanIdentifySet();
            this.rootBean = bean;
            this.propertyHandler = propertyHandler;
            this.simplePropertyHandler = simplePropertyHandler;
            scanImpl(bean);
        } finally {
            reset();
        }
    }

    private Set<Integer> loadBeanIdentifySet() {
        Set<Integer> result;
        SoftReference<Set<Integer>> setRef = beanIdentifySetRef;
        if (setRef == null || (result = setRef.get()) == null) {
            beanIdentifySetRef = new SoftReference<>(result = new HashSet<>(64));
        }
        return result;
    }

    private void recyleBeanIdentifySet(Set<Integer> set) {
        SoftReference<Set<Integer>> setRef = beanIdentifySetRef;
        if (setRef != null && set != null) {
            if (set.size() < 256) {
                set.clear();
            } else {
                setRef.clear();
                beanIdentifySetRef = null;
            }
        }
    }

    protected void scanImpl(Object bean) {
        if (bean == null || !beanIdentifySet.add(System.identityHashCode(bean))) { return; }
        final Object last = currentBean;
        this.currentBean = bean;
        if (bean.getClass().isArray()) {
            Class<?> type = bean.getClass().getComponentType();
            final int len = Array.getLength(bean);
            for (int idx = 0; idx < len; ++idx) {
                Object value = Array.get(bean, idx);
                if (value != null) { handle(null, null, null, idx, type, value); }
            }
        } else if (bean instanceof Map) {
            for (Map.Entry entry : ((Map<?, ?>) bean).entrySet()) {
                Object value = entry.getValue();
                if (value != null) { handle(null, entry, null, -1, null, value); }
            }
        } else if (bean instanceof Iterable) {
            Iterator it = ((Iterable) bean).iterator();
            for (int idx = 0; it.hasNext(); ++idx) {
                Object value = it.next();
                if (value != null) { handle(null, null, it, idx, null, value); }
            }
        } else {
            BeanDescriptor descriptors = BeanDescriptor.getBeanDescriptor(bean.getClass());
            for (BeanPropertyDescriptor descriptor : descriptors.propertyDescriptors) {
                handle(descriptor, null, null, -1, null, LAZY_READ);
            }
        }
        this.currentBean = last;
    }

    private void handle(BeanPropertyDescriptor descriptor, Map.Entry entry, Iterator it, int idx,
            Class<?> componentType, Object value) {
        this.propertyDescriptor = descriptor;
        this.propertyMapEntry = entry;
        this.propertyIterator = it;
        this.propertyIdx = idx;
        this.propertyArrayComponentType = componentType;
        this.propertyValue = value;
        this.simplePropertyType =
                descriptor != null ? descriptor.isSimplePropertyType() : BeanUtils.isSimpleValueType(value.getClass());
        //
        PropScanMode mode;
        if (propertyHandler != null) {
            mode = propertyHandler.handle(this);
        } else {
            if (simplePropertyType) { simplePropertyHandler.handle(this); }
            mode = PropScanMode.DEEP;
        }
        if (mode == PropScanMode.DEEP && !simplePropertyType) { scanImpl(propertyValue()); }
    }

    private void reset() {
        recyleBeanIdentifySet(beanIdentifySet);
        this.beanIdentifySet = null;
        this.rootBean = null;
        this.propertyHandler = null;
        this.currentBean = null;
        this.propertyDescriptor = null;
        this.propertyMapEntry = null;
        this.propertyIdx = -1;
        this.propertyIterator = null;
        this.propertyArrayComponentType = null;
        this.propertyValue = null;
    }

    @Override
    public Object rootBean() {
        return rootBean;
    }

    @Override
    public Object currentBean() {
        return currentBean;
    }

    @Override
    public String propertyName() {
        if (propertyDescriptor != null) {
            return propertyDescriptor.propertyName();
        } else if (propertyMapEntry != null) {
            Object key = propertyMapEntry.getKey();
            return key == null ? "" : key.toString();
        } else if (propertyIdx > -1) {
            return String.valueOf(propertyIdx);
        } else {
            return "";
        }
    }

    @Override
    public boolean isSimplePropertyType() {
        return simplePropertyType;
    }

    @Override
    public boolean isPropertyOfMap() {
        return propertyMapEntry != null;
    }

    @Override
    public boolean isPropertyOfArray() {
        return propertyIdx > -1;
    }

    @Override
    public boolean isPropertyOfBean() {
        return propertyDescriptor != null;
    }

    @Override
    public Class<?> propertyType() {
        if (propertyDescriptor != null) { return propertyDescriptor.propertyType(); }
        return propertyValue == null ? Void.class : propertyValue.getClass();
    }

    @Override
    public Type propertyGenericType() {
        if (propertyDescriptor != null) { propertyDescriptor.propertyGenericType(); }
        return propertyValue == null ? null : propertyValue.getClass();
    }

    @Override
    public <T extends Annotation> T propertyAnnotation(Class<T> annotationType) {
        return propertyDescriptor == null ? null : propertyDescriptor.annotation(annotationType);
    }

    @Override
    public Object propertyValue() {
        if (propertyValue == LAZY_READ) {
            propertyValue = propertyDescriptor == null ? null : propertyDescriptor.read(currentBean);
        }
        return propertyValue;
    }

    @Override
    public boolean propertyValue(Object value) {
        if (value == propertyValue) { return true; }
        try {
            if (propertyDescriptor != null) {
                if (propertyDescriptor.write(currentBean, value)) {
                    this.propertyValue = value;
                    return true;
                }
            } else if (propertyMapEntry != null) {
                propertyMapEntry.setValue(value);
                this.propertyValue = value;
                return true;
            } else if (propertyIterator != null) {
                if (propertyIterator instanceof ListIterator) {
                    ((ListIterator) propertyIterator).set(value);
                    this.propertyValue = value;
                    return true;
                }
            } else if (propertyArrayComponentType != null) {
                if (ClassUtils.isAssignableValue(propertyArrayComponentType, value)) {
                    Array.set(currentBean, propertyIdx, value);
                    return true;
                }
            }
        } catch (Throwable ignored) { }
        return false;
    }
}