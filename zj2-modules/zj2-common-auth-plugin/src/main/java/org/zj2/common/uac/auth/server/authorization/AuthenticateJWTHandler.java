package org.zj2.common.uac.auth.server.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.api.AppApi;
import org.zj2.common.uac.auth.api.JWTokenApi;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.JWTValidUtil;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.service.auth.helper.AuthenticateHandler;
import org.zj2.lite.service.cache.CacheUtil;
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
    private static final SpringBeanRef<AppApi> APP_API_REF = new SpringBeanRef<>(AppApi.class);
    private static final SpringBeanRef<JWTokenApi> TOKEN_API_REF = new SpringBeanRef<>(JWTokenApi.class);

    @Override
    public boolean supports(AuthContext context) {
        return context.getTokenType() == TokenType.SERVER_SIGN;
    }

    @Override
    public void authenticate(RequestContext requestContext, AuthContext authContext) {
        if (authContext.getTokenTime() < System.currentTimeMillis() - 1000) {
            throw AuthUtil.unAuthenticationErr("Token过期");
        }
        AppDTO app = getApp(authContext.getAppCode());
        if (!JWTValidUtil.valid(app.getAppSecret(), authContext.getToken())) {
            throw AuthUtil.unAuthenticationErr("Token无效");
        }
        //
        if (StringUtils.isNotEmpty(authContext.getNamespace())) {
            // 单点登录，检查token
            JWTokenApi jwtokenApi = TOKEN_API_REF.get();
            String errMsg = jwtokenApi == null ?
                    null :
                    jwtokenApi.validToken(app.getAppCode(), AuthContext.currentUserId(), authContext.getNamespace(),
                            authContext.getToken());
            if (StringUtils.isNotEmpty(errMsg)) { throw AuthUtil.unAuthenticationErr(errMsg); }
        }
    }

    private AppDTO getApp(String appCode) {
        AppApi api = APP_API_REF.get();
        if (api == null) { throw AuthUtil.unAuthenticationErr("请求应用无效"); }
        appCode = StringUtils.defaultIfEmpty(appCode, AppDTO.COMMON_APP_CODE);
        AppDTO app = CacheUtil.DEF_CACHE.getCache(AppDTO.getCacheKey(appCode), appCode, api::getByCode);
        if (app == null || BooleanUtil.isFalse(app.getEnableFlag())) {
            throw AuthUtil.unAuthenticationErr("请求应用无效");
        }
        return app;
    }
}
