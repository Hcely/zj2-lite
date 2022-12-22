package org.zj2.lite.service.cache;

import java.util.function.Function;

class CacheHelperLink implements CacheHelper {
    private final CacheHelper helper;
    private final CacheHelper next;

    CacheHelperLink(CacheHelper helper, CacheHelper next) {
        this.helper = helper;
        this.next = next;
    }

    @Override
    public long getExpireIn() {
        return helper.getExpireIn();
    }

    @Override
    public void removeCache(String cacheKey) {
        if (next != null) {next.removeCache(cacheKey);}
        helper.removeCache(cacheKey);
    }

    @Override
    public <T> void setCache(String cacheKey, T value, long timeout) {
        helper.setCache(cacheKey, value, timeout);
        if (next != null) {next.setCache(cacheKey, value, timeout << 1);}
    }

    @Override
    public <T> T getCache(String cacheKey, String dataKey, Function<String, T> getter, long timeout,
            boolean ignoreErr) {
        final Function<String, T> finalGetter;
        if (next == null) {
            finalGetter = getter;
        } else {
            final long t = Math.max(next.getExpireIn(), timeout + helper.getExpireIn());
            finalGetter = k -> next.get(cacheKey, dataKey, getter, t, ignoreErr);
        }
        return helper.get(cacheKey, dataKey, finalGetter, timeout, ignoreErr);
    }
}