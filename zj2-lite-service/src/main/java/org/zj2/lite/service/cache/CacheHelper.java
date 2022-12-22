package org.zj2.lite.service.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public interface CacheHelper {
    long getExpireIn();

    default void remove(final Class<?> type, final String key) {
        if (StringUtils.isEmpty(key)) {return;}
        removeCache(CacheUtil.getFullKey(type, key));
    }

    default void remove(final String prefix, final String key) {
        if (StringUtils.isEmpty(key)) {return;}
        removeCache(CacheUtil.getFullKey(prefix, key));
    }

    default <T> void set(final Class<T> type, final String key, T value) {
        set(type, key, value, 0);
    }

    default <T> void set(final String prefix, final String key, T value) {
        set(prefix, key, value, 0);
    }

    default <T> void set(final Class<T> type, final String key, T value, long timeout) {
        if (StringUtils.isEmpty(key)) {return;}
        setCache(CacheUtil.getFullKey(type, key), value, timeout);
    }

    default <T> void set(final String prefix, final String key, T value, long timeout) {
        if (StringUtils.isEmpty(key)) {return;}
        setCache(CacheUtil.getFullKey(prefix, key), value, timeout);
    }

    default <T> T get(final String prefix, final String key) {
        return get(prefix, key, null, 0);
    }

    default <T> T get(final Class<T> type, final String key) {
        return get(type, key, null, 0);
    }

    default <T> T get(final Class<T> type, final String key, Function<String, T> getter) {
        return get(type, key, getter, 0, false);
    }

    default <T> T get(final String prefix, final String key, Function<String, T> getter) {
        return get(prefix, key, getter, 0, false);
    }

    default <T> T get(final Class<T> type, final String key, Function<String, T> getter, boolean ignoreErr) {
        return get(type, key, getter, 0, false);
    }

    default <T> T get(final String prefix, final String key, Function<String, T> getter, boolean ignoreErr) {
        return get(prefix, key, getter, 0, ignoreErr);
    }

    default <T> T get(final Class<T> type, final String key, Function<String, T> getter, long timeout) {
        return get(type, key, getter, timeout, false);
    }

    default <T> T get(final String prefix, final String key, Function<String, T> getter, long timeout) {
        return get(prefix, key, getter, timeout, false);
    }

    default <T> T get(final Class<T> type, final String key, Function<String, T> getter, long timeout,
            boolean ignoreErr) {
        if (StringUtils.isEmpty(key)) {return null;}
        return getCache(CacheUtil.getFullKey(type, key), key, getter, timeout, ignoreErr);
    }

    default <T> T get(final String prefix, final String key, Function<String, T> getter, long timeout,
            boolean ignoreErr) {
        if (StringUtils.isEmpty(key)) {return null;}
        return getCache(CacheUtil.getFullKey(prefix, key), key, getter, timeout, ignoreErr);
    }

    void removeCache(final String cacheKey);

    default <T> void setCache(final String cacheKey, T value) {
        setCache(cacheKey, value, 0);
    }

    <T> void setCache(final String cacheKey, T value, long timeout);

    default <T> T getCache(final String cacheKey, final String dataKey, Function<String, T> getter) {
        return getCache(cacheKey, dataKey, getter, 0, false);
    }

    default <T> T getCache(final String cacheKey, final String dataKey, Function<String, T> getter, long timeout) {
        return getCache(cacheKey, dataKey, getter, timeout, false);
    }

    default <T> T getCache(final String cacheKey, final String dataKey, Function<String, T> getter,
            boolean ignoreErr) {
        return getCache(cacheKey, dataKey, getter, 0, ignoreErr);
    }

    <T> T getCache(final String cacheKey, final String dataKey, Function<String, T> getter, long timeout,
            boolean ignoreErr);
}