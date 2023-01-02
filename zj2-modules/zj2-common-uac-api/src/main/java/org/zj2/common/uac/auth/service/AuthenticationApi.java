package org.zj2.common.uac.auth.service;

import org.zj2.common.uac.auth.dto.AuthenticationJWT;
import org.zj2.common.uac.auth.dto.req.AuthNamePwReq;
import org.zj2.lite.service.ApiReference;

/**
 *  AuthenticationApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:08
 */
@ApiReference
public interface AuthenticationApi {
    AuthenticationJWT authenticateNamePw(AuthNamePwReq req);
}
