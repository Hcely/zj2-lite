package org.zj2.common.uac.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.service.JWTokenApi;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.JWTValidUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.spring.SpringBeanRef;

/**
 *  AbstractAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
public abstract class AbstractAuthInterceptor {
    private static final long SIGN_TIMEOUT = 60000L * 5;
    private static final SpringBeanRef<JWTokenApi> TOKEN_API_REF = new SpringBeanRef<>(JWTokenApi.class);

    protected final void authenticateJWT(ServiceRequestContext context) {
        if (context.getTokenTime() < System.currentTimeMillis() - 1000) {
            throw AuthUtil.unAuthenticationErr("Token过期");
        }
        AppDTO app = getApp();
        if (!JWTValidUtil.valid(app.getAppSecret(), context.getToken())) {
            throw AuthUtil.unAuthenticationErr("Token无效");
        }
        if (StringUtils.isNotEmpty(context.getNamespace())) {// 单点登录，检查token
            validJWT(app, context);
        }
    }

    private void validJWT(AppDTO app, ServiceRequestContext context) {
        JWTokenApi jwtokenApi = TOKEN_API_REF.get();
        String errMsg = jwtokenApi == null ?
                null :
                jwtokenApi.validToken(app.getAppCode(), AuthenticationContext.currentUserId(), context.getNamespace(),
                        context.getToken());
        if (StringUtils.isNotEmpty(errMsg)) { throw AuthUtil.unAuthenticationErr(errMsg); }
    }

    protected final void authenticateSign(ServiceRequestContext context) {
        long timestamp = context.getTokenTime();
        long now = System.currentTimeMillis();
        if (timestamp > now - 1000 || timestamp < now - SIGN_TIMEOUT) {
            throw AuthUtil.unAuthenticationErr("请求过期");
        }
        AppDTO app = getApp();
        String sign = ServerSignUtil.buildSign(app.getAppCode(), app.getAppSecret(), context.getTokenTime(),
                context.getMethod(), context.getUri());
        if (!StringUtils.equalsIgnoreCase(sign, context.getToken())) { throw AuthUtil.unAuthenticationErr("非法请求"); }
    }

    private AppDTO getApp() {
        AppDTO app = AuthUtil.getApp();
        if (app == null || BooleanUtil.isFalse(app.getEnableFlag())) {
            throw AuthUtil.unAuthenticationErr("请求应用无效");
        }
        return app;
    }
}

