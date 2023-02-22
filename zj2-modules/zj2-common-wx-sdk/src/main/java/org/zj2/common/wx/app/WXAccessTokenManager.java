package org.zj2.common.wx.app;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.spring.SpringBeanRef;

import java.util.concurrent.TimeUnit;

/**
 * WXAccessTokenManager
 *
 * @author peijie.ye
 * @date 2023/2/21 18:24
 */
public class WXAccessTokenManager {
    private static final SpringBeanRef<StringRedisTemplate> REDIS_TEMPLATE_REF = new SpringBeanRef<>(
            StringRedisTemplate.class);

    private static String getRedisKey(String wxAppId) {
        return StrUtil.concat("WX:ACCESS_TOKEN:", wxAppId);
    }

    public static void setWXAccessToken(WXAccessToken accessToken) {
        StringRedisTemplate redisTemplate = REDIS_TEMPLATE_REF.get();
        if (redisTemplate == null) { return; }
        String key = getRedisKey(accessToken.getWxAppId());
        String value = JSON.toJSONString(accessToken);
        long timeout = accessToken.getExpireTime() - accessToken.getCreateTime() + 60000;
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public static WXAccessToken getWXAccessToken(String wxAppId) {
        StringRedisTemplate redisTemplate = REDIS_TEMPLATE_REF.get();
        if (redisTemplate == null) { return null; }
        String key = getRedisKey(wxAppId);
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(value)) { return null; }
        WXAccessToken accessToken = JSON.parseObject(value, WXAccessToken.class);
        return accessToken.getExpireTime() < System.currentTimeMillis() ? null : accessToken;
    }
}
