package org.zj2.common.uac.auth.api;

import org.zj2.common.uac.auth.dto.req.AuthNamePwReq;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.auth.AuthorizationJWT;

/**
 *  AuthenticationApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:08
 */
@ApiReference
public interface AuthenticationApi {
    AuthorizationJWT authenticateNamePw(AuthNamePwReq req);
}
