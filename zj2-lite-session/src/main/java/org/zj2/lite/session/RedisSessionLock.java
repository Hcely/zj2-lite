package org.zj2.lite.session;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 *
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
public class RedisSessionLock implements SessionLock {
    private final StringRedisTemplate redisTemplate;
    private final String lockKey;
    private final long timeout;

    public RedisSessionLock(StringRedisTemplate redisTemplate, String lockKey) {
        this(redisTemplate, lockKey, 60000L * 3);
    }

    public RedisSessionLock(StringRedisTemplate redisTemplate, String lockKey, long timeout) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.timeout = timeout;
    }


    @Override
    public boolean lock() throws Throwable {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", timeout, TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    @Override
    public void unlock() {
        redisTemplate.delete(lockKey);
    }
}
