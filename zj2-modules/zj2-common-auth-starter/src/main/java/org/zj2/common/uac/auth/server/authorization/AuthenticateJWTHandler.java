package org.zj2.common.uac.auth.server.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.service.JWTokenApi;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.common.uac.auth.util.JWTValidUtil;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.spring.SpringBeanRef;

/**
 * AuthenticateJWTHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 12:06
 */
@Component
public class AuthenticateJWTHandler implements AuthenticateHandler {
    private static final SpringBeanRef<JWTokenApi> TOKEN_API_REF = new SpringBeanRef<>(JWTokenApi.class);

    @Override
    public boolean supports(TokenType type) {
        return type == TokenType.SERVER_SIGN;
    }

    @Override
    public void authenticate(RequestContext requestContext, AuthContext authContext) {
        if (authContext.getTokenTime() < System.currentTimeMillis() - 1000) {
            throw AuthManager.unAuthenticationErr("Token过期");
        }
        AppDTO app = getApp(authContext.getAppCode());
        if (!JWTValidUtil.valid(app.getAppSecret(), authContext.getToken())) {
            throw AuthManager.unAuthenticationErr("Token无效");
        }
        //
        if (StringUtils.isNotEmpty(authContext.getNamespace())) {
            // 单点登录，检查token
            JWTokenApi jwtokenApi = TOKEN_API_REF.get();
            String errMsg = jwtokenApi == null ?
                    null :
                    jwtokenApi.validToken(app.getAppCode(), AuthContext.currentUserId(), authContext.getNamespace(),
                            authContext.getToken());
            if (StringUtils.isNotEmpty(errMsg)) { throw AuthManager.unAuthenticationErr(errMsg); }
        }
    }

    private AppDTO getApp(String appCode) {
        AppDTO app = AuthManager.getApp(appCode);
        if (app == null || BooleanUtil.isFalse(app.getEnableFlag())) {
            throw AuthManager.unAuthenticationErr("请求应用无效");
        }
        return app;
    }
}
