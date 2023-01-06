package org.zj2.lite.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.zj2.lite.common.annotation.JProperty;
import org.zj2.lite.common.bean.BeanDescriptor;
import org.zj2.lite.common.bean.BeanPropertyDescriptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * <br>CreateDate 三月 24,2022
 * @author peijie.ye
 */
public class BeanUtil {

    public static <R, T> List<T> copyToList(Collection<R> coll, Class<T> targetType) {
        return CollUtil.toList(coll, e -> toBean(e, targetType));
    }

    public static <T> T copy(T source) {
        if (source == null) {
            return null;
        } else {
            //noinspection unchecked
            return (T) toBean(source, source.getClass());
        }
    }

    public static <R, T> T toBean(R source, Class<T> targetType) {
        return toBean(source, targetType, null);
    }

    public static <R, T> T toBean(R source, Class<T> targetType, Set<String> ignoreProps) {
        if (source == null) {
            return null;
        } else {
            T result = newInstance(targetType);
            copyProperties(source, result, ignoreProps);
            return result;
        }
    }

    public static <T> T copyProperties(Object source, T target) {
        return copyProperties(source, target, Collections.emptySet());
    }

    public static <T> T copyProperties(Object source, T target, String... ignoredProperties) {
        return copyProperties(source, target, ignoredProperties == null || ignoredProperties.length == 0 ?
                Collections.emptySet() :
                Set.of(ignoredProperties));
    }

    public static <T> T copyProperties(Object source, T target, Set<String> ignoredProperties) {
        if (source == null || target == null) { return target; }
        if (ignoredProperties == null) { ignoredProperties = Collections.emptySet(); }
        BeanDescriptor sd = PropertyUtil.getBeanDescriptor(source.getClass());
        BeanDescriptor td = PropertyUtil.getBeanDescriptor(target.getClass());
        copyProperties(sd, source, td, target, ignoredProperties);
        copyJSONProperties(sd, source, td, target, ignoredProperties);
        return target;
    }

    private static void copyProperties(BeanDescriptor sd, Object source, BeanDescriptor td, Object target,
            Set<String> ignoredProperties) {
        JProperty property;
        for (int idx = 0, size = td.propertySize(); idx < size; ++idx) {
            BeanPropertyDescriptor tpd = td.propertyDescriptor(idx);
            if (tpd.writeMethod() == null || ignoredProperties.contains(tpd.propertyName())) { continue; }
            BeanPropertyDescriptor spd = sd.propertyDescriptor(tpd.propertyName());
            if (spd != null) {
                if (spd.readMethod() != null) {
                    tpd.writeByMethod(target, spd.readByMethod(source));
                }
            } else if ((property = tpd.annotation(JProperty.class)) != null) {
                Object jsonProperty = sd.readProperty(source, property.json());
                Object value = getJsonProperty(jsonProperty, property.property(), tpd);
                tpd.writeByMethod(target, value);
            }
        }
    }

    private static void copyJSONProperties(BeanDescriptor sd, Object source, BeanDescriptor td, Object target,
            Set<String> ignoredProperties) {
        Map<String, JSONObject> jsonMap = readSrcJSONProperties(sd, source, ignoredProperties);
        if (jsonMap != null) {
            for (Map.Entry<String, JSONObject> e : jsonMap.entrySet()) {
                BeanPropertyDescriptor tpd = td.propertyDescriptor(e.getKey());
                if (tpd != null && tpd.propertyType() == JSONObject.class) {
                    JSONObject targetJSON = (JSONObject) tpd.read(target);
                    if (targetJSON == null) {
                        tpd.write(target, e.getValue());
                    } else {
                        targetJSON.putAll(e.getValue());
                    }
                }
            }
        }
    }

    private static Map<String, JSONObject> readSrcJSONProperties(BeanDescriptor sd, Object source,
            Set<String> ignoredProperties) {
        Map<String, JSONObject> jsonMap = null;
        for (int idx = 0, size = sd.propertySize(); idx < size; ++idx) {
            BeanPropertyDescriptor spd = sd.propertyDescriptor(idx);
            if (ignoredProperties.contains(spd.propertyName())) { continue; }
            JProperty property = spd.annotation(JProperty.class);
            if (property != null) {
                String key = StringUtils.defaultIfEmpty(property.property(), spd.propertyName());
                Object value = spd.read(source);
                if (jsonMap == null) { jsonMap = new HashMap<>(8); }
                JSONObject json = jsonMap.computeIfAbsent(property.json(), k -> new JSONObject(true));
                if (value != null) { json.put(key, value); }
            }
        }
        return jsonMap;
    }

    private static Object getJsonProperty(Object jsonProperty, String keyName,//NOSONAR
            BeanPropertyDescriptor pd) {
        if (!(jsonProperty instanceof JSONObject)) { return null; }
        if (StringUtils.isEmpty(keyName)) { keyName = pd.propertyName(); }
        Class<?> type = pd.propertyType();
        if (type == String.class) {
            return ((JSONObject) jsonProperty).getString(keyName);
        } else if (type == BigDecimal.class) {
            return ((JSONObject) jsonProperty).getBigDecimal(keyName);
        } else if (type == LocalDateTime.class) {
            return DateUtil.parse(((JSONObject) jsonProperty).getString(keyName));
        } else if (type == Long.class || type == long.class) {
            return ((JSONObject) jsonProperty).getLong(keyName);
        } else if (type == Integer.class || type == int.class) {
            return ((JSONObject) jsonProperty).getInteger(keyName);
        } else if (type == Boolean.class || type == boolean.class) {
            return ((JSONObject) jsonProperty).getBoolean(keyName);
        } else if (type == Double.class || type == double.class) {
            return ((JSONObject) jsonProperty).getByte(keyName);
        } else if (type == Byte.class || type == byte.class) {
            return ((JSONObject) jsonProperty).getByte(keyName);
        } else if (type == Short.class || type == short.class) {
            return ((JSONObject) jsonProperty).getShort(keyName);
        } else if (type == Float.class || type == float.class) {
            return ((JSONObject) jsonProperty).getFloat(keyName);
        } else if (type == Date.class) {
            return ((JSONObject) jsonProperty).getDate(keyName);
        } else if (type == LocalDate.class) {
            return DateUtil.parseAsLocalDate(((JSONObject) jsonProperty).getString(keyName));
        } else {
            return ((JSONObject) jsonProperty).getObject(keyName, pd.propertyGenericType());
        }
    }

    public static <T> T newInstance(Class<T> type) {
        return BeanUtils.instantiateClass(type);
    }
}
