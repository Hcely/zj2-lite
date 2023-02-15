package org.zj2.common.uac.auth.server.authorization;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.JWTValidUtil;
import org.zj2.lite.service.auth.AuthorizationJWT;
import org.zj2.lite.service.auth.helper.AuthorizationFactory;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * JWTFactory
 *
 * @author peijie.ye
 * @date 2023/2/10 18:03
 */
@Component
@Order(0)
public class AuthorizationJWTFactory implements AuthorizationFactory {
    @Override
    public boolean supports(String authorization) {
        return JWTValidUtil.isJWT(authorization);
    }

    @Override
    public AuthContext create(RequestContext requestContext, String authorization) {
        AuthorizationJWT jwt = JWTValidUtil.parse(authorization);
        if (jwt == null) { throw AuthUtil.unAuthorityErr("无效token格式"); }
        AuthContext context = new AuthContext();
        context.setAuthorization(jwt);
        context.setTokenType(TokenType.JWT);
        context.setToken(jwt.getToken());
        context.setTokenTime(jwt.getExpireAt());
        context.setNamespace(jwt.getNamespace());
        context.setTokenId(jwt.getTokenId());
        context.setUserId(jwt.getUserId());
        context.setUserName(jwt.getUserName());
        context.setAppCode(jwt.getAppCode());
        context.setOrgCode(jwt.getOrgCode());
        context.setServiceName(ServiceConstants.serviceName());
        context.setRootService(ServiceConstants.serviceName());
        context.setClientCode(jwt.getClientCode());
        return context;
    }
}
