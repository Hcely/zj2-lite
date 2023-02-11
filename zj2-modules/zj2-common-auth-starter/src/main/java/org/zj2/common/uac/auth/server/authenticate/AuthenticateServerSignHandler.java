package org.zj2.common.uac.auth.server.authenticate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * AuthenticateServerSignHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 12:07
 */
@Component
public class AuthenticateServerSignHandler implements AuthenticateHandler {
    private static final long SIGN_TIMEOUT = 60000L * 5;

    @Override
    public boolean supports(TokenType obj) {
        return false;
    }

    @Override
    public void authenticate(RequestContext requestContext, AuthContext authContext) {
        long timestamp = authContext.getTokenTime();
        long now = System.currentTimeMillis();
        if (timestamp > now - 1000 || timestamp < now - SIGN_TIMEOUT) {
            throw AuthManager.unAuthenticationErr("请求过期");
        }
        AppDTO app = getApp(authContext.getAppCode());
        String sign = ServerSignUtil.buildSign(app.getAppCode(), app.getAppSecret(), authContext.getTokenTime(),
                requestContext.getMethod(), requestContext.getUri());
        if (!StringUtils.equalsIgnoreCase(sign, authContext.getToken())) {
            throw AuthManager.unAuthenticationErr("非法请求");
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
