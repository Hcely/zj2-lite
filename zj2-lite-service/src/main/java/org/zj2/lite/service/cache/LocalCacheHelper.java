package org.zj2.lite.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@Slf4j
class LocalCacheHelper extends AbsCacheHelper implements Runnable {
    private static final int RETRY_COUNT = 3;
    private static final ScheduledThreadPoolExecutor LOCAL_CACHE_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("CACHE_CLEAR");
                thread.setDaemon(true);
                return thread;
            });
    private final ConcurrentHashMap<String, CacheObjRef> cacheObjMap = new ConcurrentHashMap<>(4096);

    public LocalCacheHelper(long expireIn) {
        super(expireIn);
        final long delay = Math.max(10000, expireIn >>> 1);
        LOCAL_CACHE_POOL_EXECUTOR.scheduleAtFixedRate(this, delay, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        prune();
    }

    private void prune() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<String, CacheObjRef>> it = cacheObjMap.entrySet().iterator(); it.hasNext(); ) {
            CacheObjRef ref = it.next().getValue();
            if (ref.isTimeout(currentTime)) {
                it.remove();
                ref.clear();
            }
        }
    }

    public void clear() {
        for (Iterator<CacheObjRef> it = cacheObjMap.values().iterator(); it.hasNext(); ) {
            CacheObjRef ref = it.next();
            it.remove();
            ref.clear();
        }
    }

    private long getTimeout(long timeout) {
        return timeout < 1 ? expireIn : Math.min(timeout, 60000L * 3);
    }

    @Override
    public void removeCache(String cacheKey) {
        CacheObjRef ref = cacheObjMap.remove(cacheKey);
        if (ref != null) {ref.clear();}
    }

    @Override
    public <T> void setCache(String cacheKey, T value, long timeout) {
        cacheObjMap.put(cacheKey, new CacheObjRef(value, timeout));
    }

    @Override
    public <T> T getCache(String cacheKey, String dataKey, Function<String, T> getter, long timeout,
            boolean ignoreErr) {
        CacheObjRef cacheObj = cacheObjMap.get(cacheKey);
        if (cacheObj != null && !cacheObj.isTimeout()) {return (T) cacheObj.get();}
        if (getter == null || StringUtils.isEmpty(dataKey)) {return null;}
        final CacheHandler handler = new CacheHandler(dataKey, getter, getTimeout(timeout), ignoreErr);
        for (int i = 0; i < RETRY_COUNT; ++i) {
            handler.result = null;
            try {
                cacheObjMap.compute(cacheKey, handler);
            } catch (IllegalStateException e) {
                // 对象发生并发，再取一次
            }
        }
        return (T) handler.result;
    }

    @SuppressWarnings("all")
    private static class CacheHandler implements BiFunction<String, CacheObjRef, CacheObjRef> {
        private final String key;
        private final Function getter;
        private final long timeout;
        private final boolean ignoreErr;
        private Object result;

        private CacheHandler(String key, Function getter, long timeout, boolean ignoreErr) {
            this.key = key;
            this.getter = getter;
            this.timeout = timeout;
            this.ignoreErr = ignoreErr;
        }

        @Override
        public CacheObjRef apply(String k, CacheObjRef v) {
            if (v != null && !v.isTimeout()) {
                result = v.get();
                return v;
            }
            try {
                result = getter.apply(key);
                return new CacheObjRef(result, timeout);
            } catch (Throwable e) {//NOSONAR
                log.error("缓存获取异常", e);
                result = null;
                if (ignoreErr) {
                    return new CacheObjRef(null, 10000);
                } else {
                    throw e;
                }
            }
        }
    }

    private static class CacheObjRef extends WeakReference<Object> {
        private final long expireAt;
        private Object hardRef;

        public CacheObjRef(Object referent, long expireIn) {
            super(referent);
            this.hardRef = referent;
            this.expireAt = System.currentTimeMillis() + expireIn;
        }

        public boolean isTimeout() {
            return expireAt < System.currentTimeMillis();
        }

        public boolean isTimeout(long currentTime) {
            return expireAt < currentTime;
        }

        @Override
        public Object get() {
            return hardRef;
        }

        @Override
        public void clear() {
            hardRef = null;
            super.clear();
        }
    }
}