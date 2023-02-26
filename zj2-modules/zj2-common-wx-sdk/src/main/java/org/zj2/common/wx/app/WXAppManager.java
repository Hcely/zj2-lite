package org.zj2.common.wx.app;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zj2.common.wx.app.resp.WXAccessTokenResp;
import org.zj2.lite.common.entity.key.FormatKey;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.spring.SpringBeanRef;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * WXAccessTokenManager
 *
 * @author peijie.ye
 * @date 2023/2/21 18:24
 */
public class WXAppManager {
    private static final FormatKey ACCESS_TOKEN_KEY = new FormatKey("WX:{}:ACCESS_TOKEN");
    private static final SpringBeanRef<WXAppBundle[]> BUNDLES_REF = new SpringBeanRef<>(WXAppBundle[].class);
    private static final SpringBeanRef<StringRedisTemplate> REDIS_TEMPLATE_REF = new SpringBeanRef<>(
            StringRedisTemplate.class);

    public static List<WXApp> apps() {
        List<WXApp> apps = new ArrayList<>();
        WXAppBundle[] bundles = BUNDLES_REF.get();
        if (bundles != null) {
            for (WXAppBundle bundle : bundles) {
                CollUtil.addAll(apps, bundle.list(), true);
            }
        }
        return apps;
    }

    public static WXApp getApp(String wxAppId) {
        WXAppBundle[] bundles = BUNDLES_REF.get();
        if (bundles != null) {
            for (WXAppBundle bundle : bundles) {
                WXApp app = bundle.get(wxAppId);
                if (app != null) { return app; }
            }
        }
        return null;
    }


    public static void setWXAccessToken(String wxAppId, WXAccessTokenResp resp) {
        StringRedisTemplate redisTemplate = REDIS_TEMPLATE_REF.get();
        if (redisTemplate == null) { return; }
        final long createTime = System.currentTimeMillis();
        final long expireAt = createTime + resp.getExpiresIn() * 1000 - 5000L;
        WXAccessToken accessToken = new WXAccessToken();
        accessToken.setWxAppId(wxAppId);
        accessToken.setAccessToken(resp.getAccessToken());
        accessToken.setCreateTime(createTime);
        accessToken.setExpireTime(expireAt);
        String key = ACCESS_TOKEN_KEY.get(wxAppId);
        String value = JSON.toJSONString(accessToken);
        long timeout = resp.getExpiresIn() * 1000 + 60_000L;
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public static String getWXAccessToken(String wxAppId) {
        WXAccessToken accessToken = CacheUtil.DEF_CACHE.get(WXAccessToken.class, wxAppId,
                WXAppManager::getWXAccessToken0);
        if (accessToken == null || accessToken.getExpireTime() < System.currentTimeMillis()) {
            accessToken = getWXAccessToken0(wxAppId);
        }
        if (accessToken == null || accessToken.getExpireTime() < System.currentTimeMillis()) {
            return null;
        }
        return accessToken.getAccessToken();
    }

    private static WXAccessToken getWXAccessToken0(String wxAppId) {
        StringRedisTemplate redisTemplate = REDIS_TEMPLATE_REF.get();
        if (redisTemplate == null) { return null; }
        String key = ACCESS_TOKEN_KEY.get(wxAppId);
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(value)) { return null; }
        return JSON.parseObject(value, WXAccessToken.class);
    }
}
