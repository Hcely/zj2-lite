package org.zj2.common.uac.auth.api;

import org.zj2.lite.service.ApiReference;

/**
 * TokenApi
 *
 * @author peijie.ye
 * @date 2022/12/6 6:18
 */
@ApiReference
public interface JWTokenApi {
    /**
     * @param appCode
     * @param userId
     * @param namespace
     * @param token
     * @return token不合法则返回相关错误信息
     */
    String validToken(String appCode, String userId, String namespace, String token);
}
