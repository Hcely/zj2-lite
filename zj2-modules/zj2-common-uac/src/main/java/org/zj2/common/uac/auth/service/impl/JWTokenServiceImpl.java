package org.zj2.common.uac.auth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.auth.service.JWTokenService;
import org.zj2.common.uac.auth.util.JWTUtil;

import java.util.concurrent.TimeUnit;

/**
 *  JWTokenServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/6 11:43
 */
@Service
public class JWTokenServiceImpl implements JWTokenService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String validToken(String appCode, String userId, String namespace, String token) {
        final String namespaceKey = getNamespaceKey(appCode, userId, namespace);
        String tokenSign = stringRedisTemplate.opsForValue().get(namespaceKey);
        if (StringUtils.isEmpty(tokenSign) || !StringUtils.endsWith(token, tokenSign)) {
            String tokenKey = getTokenKey(JWTUtil.getSignPart(token));
            String remark = stringRedisTemplate.opsForValue().get(tokenKey);
            return StringUtils.defaultIfEmpty(remark, "Token过期");
        }
        return null;
    }

    @Override
    public void setToken(String appCode, String userId, String namespace, String token, long expireTime) {
        final String namespaceKey = getNamespaceKey(appCode, userId, namespace);
        String oldTokenSign = stringRedisTemplate.opsForValue().getAndSet(namespaceKey, JWTUtil.getSignPart(token));
        stringRedisTemplate.expire(namespaceKey, expireTime - System.currentTimeMillis() + 30000,
                TimeUnit.MILLISECONDS);
        if (StringUtils.isNotEmpty(oldTokenSign)) {
            setTokenMessage(oldTokenSign, "账号在其他地方登录");
        }
    }

    @Override
    public void removeToken(String appCode, String userId, String namespace, String token, String remark) {
        final String namespaceKey = getNamespaceKey(appCode, userId, namespace);
        String tokenSign = stringRedisTemplate.opsForValue().get(namespaceKey);
        if (StringUtils.isNotEmpty(tokenSign) && StringUtils.endsWith(token, tokenSign)) {
            stringRedisTemplate.delete(namespaceKey);
        }
        setTokenMessage(JWTUtil.getSignPart(token), remark);
    }

    private void setTokenMessage(String tokenSign, String remark) {
        String tokenKey = getTokenKey(tokenSign);
        stringRedisTemplate.opsForValue().set(tokenKey, remark, 60000L * 5, TimeUnit.MILLISECONDS);
    }

    private static String getNamespaceKey(String appCode, String userId, String namespace) {
        StringBuilder sb = new StringBuilder(
                StringUtils.length(appCode) + StringUtils.length(userId) + StringUtils.length(namespace) + 20);
        sb.append("TOKEN");
        if (StringUtils.isNotEmpty(appCode)) {sb.append(':').append(appCode);}
        if (StringUtils.isNotEmpty(userId)) {sb.append(':').append(appCode);}
        if (StringUtils.isNotEmpty(namespace)) {sb.append(':').append(appCode);}
        return sb.toString();
    }

    private static String getTokenKey(String tokenSign) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(60);
        sb.append("TOKEN:").append(tokenSign);
        return sb.toString();
    }

}
