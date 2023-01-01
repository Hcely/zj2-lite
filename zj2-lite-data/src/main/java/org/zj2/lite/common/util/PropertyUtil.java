package org.zj2.lite.common.util;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.zj2.lite.common.PropFunc;
import org.zj2.lite.common.constant.NoneConstants;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

/**
 * 获取属性UTIL
 * <br>CreateDate 六月 22,2022
 * @author peijie.ye
 */
@SuppressWarnings("all")
public class PropertyUtil {
    private static final ThreadLocal<DefBeanPropertyScanner> SCANNER_THREAD_LOCAL = ThreadLocal.withInitial(
            DefBeanPropertyScanner::new);
    private static final Map<String, BeanDescriptor> BEAN_PROPERTY_MAP = new HashMap<>(1024);
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
        if (StringUtils.isEmpty(key)) {return NoneConstants.EMPTY_STRINGS;}
        String[] paths = StringUtils.split(key, ".[]");
        return paths == null || paths.length == 0 ? NoneConstants.EMPTY_STRINGS : paths;
    }

    public static BeanPropertyScanner createScanner() {
        return new DefBeanPropertyScanner();
    }

    public static void scanProperties(Object bean, Function<BeanPropertyContext, PropScanMode> propertyHandler) {
        if (bean == null || propertyHandler == null || BeanUtils.isSimpleValueType(bean.getClass())) {return;}
        DefBeanPropertyScanner scanner = SCANNER_THREAD_LOCAL.get();
        if (scanner.rootBean == null) { // 防止环
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
                if (value == null) {break;}
                value = PropertyUtil.getProperty(value, pathKey);
            }
            return (T) value;
        }
    }

    public static <T> T getProperty(Object bean, String name) {
        if (bean == null || StringUtils.isEmpty(name)) {return null;}
        if (bean instanceof Map) {
            return (T) ((Map) bean).get(name);
        } else if (bean instanceof Collection) {
            try {
                int idx = Integer.parseInt(name);
                Collection coll = (Collection) bean;
                return idx > -1 && idx < coll.size() ? (T) IterableUtils.get((Collection) bean, idx) : null;
            } catch (Throwable e) {return null;}
        } else if (bean.getClass().isArray()) {
            try {
                int idx = Integer.parseInt(name);
                int length = Array.getLength(bean);
                return idx > -1 && idx < length ? (T) Array.get(bean, idx) : null;
            } catch (Throwable e) {return null;}
        } else {
            BeanPropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), name);
            return descriptor == null ? null : (T) descriptor.read(bean);
        }
    }

    public static boolean setProperty(Object bean, String name, Object value) {
        if (bean == null || StringUtils.isEmpty(name)) {return false;}
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
        if (type == null) {return BeanDescriptor.NONE_DESCRIPTOR;}
        final String name = type.getName();
        BeanDescriptor descriptors = BEAN_PROPERTY_MAP.get(name);
        if (descriptors == null) {
            synchronized (BEAN_PROPERTY_MAP) {
                descriptors = BEAN_PROPERTY_MAP.computeIfAbsent(name, k -> BeanDescriptor.create(type));
            }
        }
        return descriptors;
    }

    protected static class DefBeanPropertyScanner implements BeanPropertyScanner, BeanPropertyContext {
        private static final Object LAZY_READ = new Object();
        private SoftReference<Set<Integer>> beanIdentifySetRef;
        private Set<Integer> beanIdentifySet;
        private Object rootBean;
        private Function<BeanPropertyContext, PropScanMode> propertyHandler;
        private Object currentBean;
        //
        private boolean simplePropertyType;
        private BeanPropertyDescriptor propertyDescriptor;
        private Map.Entry propertyMapEntry;
        private Iterator propertyIterator;
        private int propertyIdx = -1;
        private Class<?> propertyArrayComponentType;
        private Object propertyValue;

        protected DefBeanPropertyScanner() {
        }

        @Override
        public void scan(Object bean, Function<BeanPropertyContext, PropScanMode> propertyHandler) {
            try {
                if (bean == null || propertyHandler == null || BeanUtils.isSimpleValueType(bean.getClass())) {return;}
                this.beanIdentifySet = loadBeanIdentifySet();
                this.rootBean = bean;
                this.propertyHandler = propertyHandler;
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
            if (bean == null || !beanIdentifySet.add(System.identityHashCode(bean))) {return;}
            final Object last = currentBean;
            this.currentBean = bean;
            if (bean.getClass().isArray()) {
                Class<?> type = bean.getClass().getComponentType();
                final int len = Array.getLength(bean);
                for (int idx = 0; idx < len; ++idx) {
                    Object value = Array.get(bean, idx);
                    if (value != null) {handle(null, null, null, idx, type, value);}
                }
            } else if (bean instanceof Map) {
                for (Map.Entry entry : ((Map<?, ?>) bean).entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {handle(null, entry, null, -1, null, value);}
                }
            } else if (bean instanceof Iterable) {
                Iterator it = ((Iterable) bean).iterator();
                for (int idx = 0; it.hasNext(); ++idx) {
                    Object value = it.next();
                    if (value != null) {handle(null, null, it, idx, null, value);}
                }
            } else {
                BeanDescriptor descriptors = getBeanDescriptor(bean.getClass());
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
            this.simplePropertyType = descriptor != null ?
                    descriptor.isSimplePropertyType() :
                    BeanUtils.isSimpleValueType(value.getClass());
            //
            PropScanMode mode = propertyHandler.apply(this);
            if (mode == PropScanMode.DEEP && !simplePropertyType) {scanImpl(propertyValue());}
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
            if (propertyDescriptor != null) {return propertyDescriptor.propertyType();}
            return propertyValue == null ? Void.class : propertyValue.getClass();
        }

        @Override
        public Type propertyGenericType() {
            if (propertyDescriptor != null) {propertyDescriptor.propertyGenericType();}
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
            if (value == propertyValue) {return true;}
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
            } catch (Throwable ignored) {}
            return false;
        }
    }

    public interface BeanPropertyScanner {
        void scan(Object bean, Function<BeanPropertyContext, PropScanMode> propertyHandler);
    }

    public interface BeanPropertyContext {
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


    public static class BeanDescriptor {
        private static final BeanPropertyDescriptor[] EMPTY_DESCRIPTORS = {};
        private static final BeanDescriptor NONE_DESCRIPTOR = new BeanDescriptor(null, null, null);
        protected final Class<?> type;
        protected final Constructor<?> constructor;
        protected final Map<String, BeanPropertyDescriptor> propertyDescriptorMap;
        protected final BeanPropertyDescriptor[] propertyDescriptors;

        public static BeanDescriptor create(Class<?> type) {
            if (type.isArray()) {
                return new BeanDescriptor(type);
            } else if (type == Map.class) {
                return new BeanDescriptor(type, LinkedHashMap.class);
            } else if (type == Iterable.class || type == Collection.class || type == List.class) {
                return new BeanDescriptor(type, ArrayList.class);
            } else if (type == Queue.class || type == Deque.class) {
                return new BeanDescriptor(type, LinkedList.class);
            } else if (type == Set.class) {
                return new BeanDescriptor(type, LinkedHashSet.class);
            } else if (BeanUtils.isSimpleValueType(type) || Map.class.isAssignableFrom(type)
                    || Iterable.class.isAssignableFrom(type)) {
                return new BeanDescriptor(type);
            }
            PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(type);
            if (descriptors == null || descriptors.length == 0) {return new BeanDescriptor(type);}
            List<Field> fields = FieldUtils.getAllFieldsList(type);
            Map<String, Field> fieldMap = new HashMap<>(fields.size());
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {fieldMap.putIfAbsent(f.getName(), f);}
            }
            Map<String, BeanPropertyDescriptor> map = new LinkedHashMap<>(descriptors.length);
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                map.put(name, new BeanPropertyDescriptor(descriptor, fieldMap.get(name)));
            }
            return new BeanDescriptor(type, map, CollUtil.toArray(map.values(), BeanPropertyDescriptor.class));
        }

        private static Constructor getConstructor(Class<?> type) {
            try {
                Constructor constructor = type == null || type.isArray() || type.isInterface() || Modifier.isAbstract(
                        type.getModifiers()) ? null : BeanUtils.getResolvableConstructor(type);
                constructor.trySetAccessible();
                return constructor;
            } catch (Throwable e) {
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

        private BeanDescriptor(Class<?> type, Map<String, BeanPropertyDescriptor> propertyDescriptorMap,
                BeanPropertyDescriptor[] propertyDescriptors) {
            this.propertyDescriptorMap = propertyDescriptorMap == null ?
                    Collections.emptyMap() :
                    Collections.unmodifiableMap(propertyDescriptorMap);
            this.propertyDescriptors = propertyDescriptors == null ? EMPTY_DESCRIPTORS : propertyDescriptors;
            this.type = type;
            this.constructor = getConstructor(type);
        }

        public Class<?> type() {
            return type;
        }

        public Object newInstance() {
            Class<?> t = type();
            if (t == null) {return null;}
            if (t.isArray()) {
                return Array.newInstance(t.getComponentType(), 0);
            } else {
                Constructor c = this.constructor;
                if (c != null) {
                    try {
                        return c.newInstance();
                    } catch (Throwable e) {
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

    public static class BeanPropertyDescriptor {
        private static final int ANNO_CACHE_SIZE = 8;
        private final boolean simplePropertyType;
        private final PropertyDescriptor propertyDescriptor;
        private final Field propertyField;
        private final Object[] annocationCaches;// 缓存

        private BeanPropertyDescriptor(PropertyDescriptor propertyDescriptor, Field propertyField) {
            this.propertyDescriptor = propertyDescriptor;
            this.propertyField = propertyField;
            this.simplePropertyType = BeanUtils.isSimpleValueType(propertyType());
            this.annocationCaches = new Object[ANNO_CACHE_SIZE];
            if (propertyField != null) {propertyField.trySetAccessible();}
            if (propertyDescriptor.getReadMethod() != null) {propertyDescriptor.getReadMethod().trySetAccessible();}
            if (propertyDescriptor.getWriteMethod() != null) {propertyDescriptor.getWriteMethod().trySetAccessible();}
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
            if (field != null) {return field.getGenericType();}
            Method method = propertyDescriptor.getReadMethod();
            if (method != null) {return method.getGenericReturnType();}
            if ((method = propertyDescriptor.getWriteMethod()) != null) {return method.getGenericParameterTypes()[0];}
            return null;
        }

        private int getCacheIdx(Class<?> annotationType) {
            return annotationType.hashCode() & (ANNO_CACHE_SIZE - 1);
        }

        public <T extends Annotation> T annotation(Class<T> annotationType) {
            final int idx = getCacheIdx(annotationType);
            final Object[] caches = annocationCaches;
            final Object cache = caches[idx];
            if (annotationType == cache) {return null;}
            if (cache != null && cache.getClass() == annotationType) {return (T) cache;}
            //
            T annotation;
            if ((annotation = annotationFromReadMethod(annotationType)) != null) {
                caches[idx] = annotation;
                return annotation;
            } else if ((annotation = annotationFromWriteMethod(annotationType)) != null) {
                caches[idx] = annotation;
                return annotation;
            } else if ((annotation = annotationFromField(annotationType)) != null) {
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
            if (bean == null) {return null;}
            try {
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null) {return readMethod.invoke(bean);}
                Field field = propertyField();
                if (field != null) {return field.get(bean);}
            } catch (Throwable ignored) {
            }
            return null;
        }

        public boolean write(Object bean, Object value) {
            if (bean == null) {return false;}
            if (!ClassUtils.isAssignableValue(propertyType(), value)) {return false;}
            try {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    writeMethod.invoke(bean, value);
                    return true;
                } else {
                    Field field = propertyField();
                    if (field != null) {
                        field.set(bean, value);
                        return true;
                    }
                }
            } catch (Throwable ignored) {
            }
            return false;
        }

        public Object readByMethod(Object bean) {
            if (bean == null) {return null;}
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    return readMethod.invoke(bean);
                } catch (Throwable ignored) {
                }
            }
            return null;
        }

        public boolean writeByMethod(Object bean, Object value) {
            if (bean == null) {return false;}
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null && ClassUtils.isAssignableValue(writeMethod.getParameterTypes()[0], value)) {
                try {
                    writeMethod.invoke(bean, value);
                    return true;
                } catch (Throwable ignored) {
                }
            }
            return false;
        }

        public Object readByField(Object bean) {
            if (bean == null) {return null;}
            Field field = propertyField();
            if (field != null) {
                try {
                    return field.get(bean);
                } catch (Throwable ignored) {
                }
            }
            return null;
        }

        public boolean writeByField(Object bean, Object value) {
            if (bean == null) {return false;}
            Field field = propertyField();
            if (field != null && ClassUtils.isAssignableValue(field.getType(), value)) {
                try {
                    field.set(bean, value);
                    return true;
                } catch (Throwable ignored) {
                }
            }
            return false;
        }
    }

    public enum PropScanMode {
        DEEP, NOT_DEEP;
    }
}
