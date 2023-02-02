package org.zj2.common.uac.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.auth.dto.req.AuthNamePwReq;
import org.zj2.common.uac.auth.service.AuthenticationApi;
import org.zj2.common.uac.auth.service.helper.AuthNamePwHelper;
import org.zj2.lite.service.auth.AuthenticationJWT;

/**
 *  AuthenticationServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/3 7:08
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationApi {
    @Autowired
    private AuthNamePwHelper authNamePwHelper;

    @Override
    public AuthenticationJWT authenticateNamePw(AuthNamePwReq req) {
        AuthContext context = new AuthContext(req);
        authNamePwHelper.handle(context);
        return context.getToken();
    }
}
