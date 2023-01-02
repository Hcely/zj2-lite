package org.zj2.common.uac.auth;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.service.JWTokenApi;
import org.zj2.common.uac.auth.util.AppUtil;
import org.zj2.common.uac.auth.util.JWTUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZRBuilder;
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
public class AbstractAuthenticationInterceptor {
    private static final long SIGN_TIMEOUT = 60000L * 5;
    private static final SpringBeanRef<JWTokenApi> TOKEN_API_REF = new SpringBeanRef<>(JWTokenApi.class);


    protected void authenticateJWT(ServiceRequestContext context) {
        if (context.getTokenTime() < System.currentTimeMillis() - 1000) {
            throw unAuthenticationErr("Token过期");
        }
        AppDTO app = getApp();
        if (!JWTUtil.valid(app.getAppSecret(), context.getToken())) {
            throw unAuthenticationErr("Token无效");
        }
        if (StringUtils.isNotEmpty(context.getNamespace())) {
            JWTokenApi jwtokenApi = TOKEN_API_REF.get();
            String errMsg = jwtokenApi == null ?
                    null :
                    jwtokenApi.validToken(app.getAppCode(), AuthenticationContext.currentUserId(),
                            context.getNamespace(), context.getToken());
            if (StringUtils.isNotEmpty(errMsg)) {throw unAuthenticationErr(errMsg);}
        }
    }

    protected void authenticateSign(ServiceRequestContext context) {
        long timestamp = context.getTokenTime();
        long now = System.currentTimeMillis();
        if (timestamp > now - 1000 || timestamp < now - SIGN_TIMEOUT) {throw unAuthenticationErr("请求过期");}
        AppDTO app = getApp();
        String sign = ServerSignUtil.buildSign(app.getAppCode(), app.getAppSecret(), context.getTokenTime(),
                context.getMethod(), context.getUri());
        if (!StringUtils.equalsIgnoreCase(sign, context.getToken())) {throw unAuthenticationErr("非法请求");}
    }

    protected AppDTO getApp() {
        AppDTO app = AppUtil.getApp();
        if (app == null || BooleanUtil.isFalse(app.getEnableFlag())) {throw unAuthenticationErr("请求应用无效");}
        return app;
    }

    protected ZError unAuthenticationErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(401);
    }

}

