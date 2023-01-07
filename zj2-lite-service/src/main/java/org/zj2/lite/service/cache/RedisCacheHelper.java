package org.zj2.lite.service.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zj2.lite.common.constant.NoneConstants;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
class RedisCacheHelper extends AbsCacheHelper {
    private final StringRedisTemplate redisTemplate;

    RedisCacheHelper(StringRedisTemplate redisTemplate, long expireIn) {
        super(expireIn);
        this.redisTemplate = redisTemplate;
    }

    private long getTimeout(long timeout) {
        timeout = timeout < 1 ? this.expireIn : Math.min(timeout, 60000L * 110);
        long randomRange = Math.min(timeout >>> 2, 60000L * 10);
        return timeout + (System.currentTimeMillis() % randomRange);
    }

    @Override
    public void removeCache(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    @Override
    public <T> void setCache(String cacheKey, T value, long timeout) {
        String valueStr =
                value == null ? NoneConstants.NONE_STR : JSON.toJSONString(value, SerializerFeature.WriteClassName);
        redisTemplate.opsForValue().set(cacheKey, valueStr, getTimeout(timeout), TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> T getCache(String cacheKey, String dataKey, Function<String, T> getter, long timeout,
            boolean ignoreErr) {
        try {
            String valueStr = redisTemplate.opsForValue().get(cacheKey);
            T value = null;
            if (StringUtils.isNotEmpty(valueStr)) {
                //noinspection unchecked
                value = NoneConstants.NONE_STR.equals(valueStr) ? null : (T) JSON.parseObject(valueStr);
            } else if (getter != null && StringUtils.isNotEmpty(dataKey)) {
                value = getter.apply(dataKey);
                setCache(cacheKey, value, timeout);
            }
            return value;
        } catch (Throwable e) {// NOSONAR
            log.error("缓存获取异常", e);
            if (ignoreErr) { return null; } else { throw e; }
        }
    }
}