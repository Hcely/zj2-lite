package org.zj2.lite.service.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.service.broadcast.ServerSignalUtil;
import org.zj2.lite.spring.SpringUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * CacheUtil
 * <br>CreateDate February 20,2020
 *
 * @author peijie.ye
 * @since 1.0
 */

public class CacheUtil {
    private static final ConcurrentLinkedQueue<LocalCacheHelper> CACHE_HELPERS = new ConcurrentLinkedQueue<>();
    static final String DEFAULT_CACHE_SIGNAL_TAG = "SERVER:CACHE:SIGNAL";
    public static final CacheHelper DEF_CACHE = createLocal();

    //    public static void sendCacheSign(Class<?> type, String key) {
    //        if (StringUtils.isNotEmpty(key)) {sendCacheSign0(CacheUtil.getFullKey(type, key));}
    //    }
    //
    //    public static void sendCacheSign(String prefix, String key) {
    //        if (StringUtils.isNotEmpty(key)) {sendCacheSign0(CacheUtil.getFullKey(prefix, key));}
    //    }

    public static void sendCacheSign(String fullKey) {
        if (StringUtils.isNotEmpty(fullKey)) { sendCacheSign0(fullKey); }
    }

    private static void sendCacheSign0(String fullKey) {
        ServerSignalUtil.broadcastAfterCommit(DEFAULT_CACHE_SIGNAL_TAG, fullKey);
    }

    public static void clearLocalCaches() {
        for (LocalCacheHelper helper : CACHE_HELPERS) { helper.clear(); }
    }

    public static CacheHelper createLocal() {
        return createLocal(30000);
    }

    public static CacheHelper createLocal(long expireIn) {
        LocalCacheHelper cacheHelper = new LocalCacheHelper(expireIn);
        CACHE_HELPERS.add(cacheHelper);
        return cacheHelper;
    }

    public static CacheHelper createRedis() {
        return createRedis(60000L * 3);
    }

    public static CacheHelper createRedis(long expireIn) {
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        return new RedisCacheHelper(redisTemplate, expireIn);
    }

    public static CacheHelper createRedis(StringRedisTemplate redisTemplate) {
        return createRedis(redisTemplate, 60000L * 3);
    }

    public static CacheHelper createRedis(StringRedisTemplate redisTemplate, long expireIn) {
        return new RedisCacheHelper(redisTemplate, expireIn);
    }

    /**
     * 创建多级缓存，从左到右，一级缓存，二级缓存，三级缓存，...
     */
    public static CacheHelper createMultiLevel(CacheHelper... cacheHelpers) {
        int idx = cacheHelpers.length - 1;
        CacheHelper helper = cacheHelpers[idx];
        for (--idx; idx > -1; --idx) { helper = new CacheHelperLink(cacheHelpers[idx], helper); }
        return helper;
    }

    public static String getFullKey(Class<?> type, String key) {
        int length = type.getSimpleName().length() + StringUtils.length(key) + 40;
        StringBuilder sb = new StringBuilder(length);
        appendClassIdentifyName(sb, type);
        sb.append(':' );
        if (StringUtils.isNotEmpty(key)) { sb.append(key); }
        return sb.toString();
    }

    public static String getFullKey(String prefix, String key) {
        int length = StringUtils.length(prefix) + StringUtils.length(key) + 1;
        StringBuilder sb = new StringBuilder(length);
        if (StringUtils.isNotEmpty(prefix)) { sb.append(prefix); }
        sb.append(':' );
        if (StringUtils.isNotEmpty(key)) { sb.append(key); }
        return sb.toString();
    }

    private static StringBuilder appendClassIdentifyName(StringBuilder sb, Class<?> type) {
        sb.append(type.getSimpleName()).append('&' );
        return CodecUtil.encodeHex(sb, type.getName().hashCode());
    }
}