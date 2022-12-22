package org.zj2.common.uac.auth.service;

/**
 *  TokenService
 *
 * @author peijie.ye
 * @date 2022/12/6 6:26
 */
public interface JWTokenService extends JWTokenApi {
    void setToken(String appCode, String userId, String namespace, String token, long expireTime);

    void removeToken(String appCode, String userId, String namespace, String token, String remark);
}
