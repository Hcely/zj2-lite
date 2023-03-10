package org.zj2.lite.common.bean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.zj2.lite.common.util.CollUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@SuppressWarnings("all")
public class BeanDescriptor {
    private static final BeanDescriptor NONE_DESCRIPTOR = new BeanDescriptor(null, null, null);
    private static final BeanPropertyDescriptor[] EMPTY_DESCRIPTORS = {};
    private static final Map<String, BeanDescriptor> BEAN_PROPERTY_MAP = new HashMap<>(1024);
    protected final Class<?> type;
    protected final Constructor<?> constructor;
    protected final Map<String, BeanPropertyDescriptor> propertyDescriptorMap;
    protected final BeanPropertyDescriptor[] propertyDescriptors;

    public static BeanDescriptor getBeanDescriptor(Class<?> type) {
        if(type == null) { return BeanDescriptor.NONE_DESCRIPTOR; }
        final String name = type.getName();
        BeanDescriptor descriptors = BEAN_PROPERTY_MAP.get(name);
        if(descriptors == null) {
            synchronized(BEAN_PROPERTY_MAP) {
                descriptors = BEAN_PROPERTY_MAP.computeIfAbsent(name, k -> BeanDescriptor.create(type));
            }
        }
        return descriptors;
    }

    static BeanDescriptor create(Class<?> type) {
        if(type.isArray()) {
            return new BeanDescriptor(type);
        } else if(type == Map.class) {
            return new BeanDescriptor(type, LinkedHashMap.class);
        } else if(type == Iterable.class || type == Collection.class || type == List.class) {
            return new BeanDescriptor(type, ArrayList.class);
        } else if(type == Queue.class || type == Deque.class) {
            return new BeanDescriptor(type, LinkedList.class);
        } else if(type == Set.class) {
            return new BeanDescriptor(type, LinkedHashSet.class);
        } else if(BeanUtils.isSimpleValueType(type) || Map.class.isAssignableFrom(type) || Iterable.class.isAssignableFrom(type)) {
            return new BeanDescriptor(type);
        }
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(type);
        if(descriptors.length == 0) { return new BeanDescriptor(type); }
        List<Field> fields = FieldUtils.getAllFieldsList(type);
        Map<String, Field> fieldMap = new HashMap<>(fields.size());
        for(Field f : fields) {
            if(!Modifier.isStatic(f.getModifiers())) { fieldMap.putIfAbsent(f.getName(), f); }
        }
        Map<String, BeanPropertyDescriptor> map = new LinkedHashMap<>(descriptors.length);
        for(PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if(!StringUtils.equals(name, "class")) {
                map.put(name, new BeanPropertyDescriptor(descriptor, fieldMap.get(name)));
            }
        }
        return new BeanDescriptor(type, map, CollUtil.toArray(map.values(), BeanPropertyDescriptor.class));
    }

    private static Constructor getConstructor(Class<?> type) {
        try {
            Constructor constructor = type == null || type.isArray() || type.isInterface() || Modifier.isAbstract(type.getModifiers()) ?
                    null :
                    BeanUtils.getResolvableConstructor(type);
            constructor.trySetAccessible();
            return constructor;
        } catch(Throwable e) {
            return null;
        }
    }

    private BeanDescriptor(Class<?> type) {
        this(type, type);
    }

    private BeanDescriptor(Class<?> type, Class<?> newInstanceType) {
        this.propertyDescriptors = EMPTY_DESCRIPTORS;
        this.propertyDescriptorMap = Collections.emptyMap();
        this.type = type;
        this.constructor = getConstructor(newInstanceType);
    }

    private BeanDescriptor(Class<?> type, Map<String, BeanPropertyDescriptor> propertyDescriptorMap, BeanPropertyDescriptor[] propertyDescriptors) {
        this.propertyDescriptorMap = propertyDescriptorMap == null ? Collections.emptyMap() : Collections.unmodifiableMap(propertyDescriptorMap);
        this.propertyDescriptors = propertyDescriptors == null ? EMPTY_DESCRIPTORS : propertyDescriptors;
        this.type = type;
        this.constructor = getConstructor(type);
    }

    public Class<?> type() {
        return type;
    }

    public Object newInstance() {
        Class<?> t = type();
        if(t == null) { return null; }
        if(t.isArray()) {
            return Array.newInstance(t.getComponentType(), 0);
        } else {
            Constructor c = this.constructor;
            if(c != null) {
                try {
                    return c.newInstance();
                } catch(Throwable e) {
                    return null;
                }
            }
            return null;
        }
    }

    public Object newInstance(int size) {
        Class<?> t = type();
        return t != null && t.isArray() ? Array.newInstance(t.getComponentType(), size) : newInstance();
    }

    public int propertySize() {
        return propertyDescriptors.length;
    }

    public BeanPropertyDescriptor propertyDescriptor(int propertyIdx) {
        return propertyDescriptors[propertyIdx];// 不做安全校验
    }

    public BeanPropertyDescriptor propertyDescriptor(String propertyName) {
        return propertyDescriptorMap.get(propertyName);
    }

    public Map<String, BeanPropertyDescriptor> propertyDescriptorMap() {
        return propertyDescriptorMap;
    }

    public Object readProperty(Object bean, String propertyName) {
        BeanPropertyDescriptor descriptor = propertyDescriptor(propertyName);
        return descriptor == null ? null : descriptor.read(bean);
    }

    public boolean writeProperty(Object bean, String propertyName, Object value) {
        BeanPropertyDescriptor descriptor = propertyDescriptor(propertyName);
        return descriptor != null && descriptor.write(bean, value);
    }
}