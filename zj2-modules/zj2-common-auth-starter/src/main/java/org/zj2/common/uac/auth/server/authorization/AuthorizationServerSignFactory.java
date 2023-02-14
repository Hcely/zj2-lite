package org.zj2.common.uac.auth.server.authorization;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.auth.AuthorizationServerSign;
import org.zj2.lite.service.auth.helper.AuthorizationFactory;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * ServerSignFactory
 *
 * @author peijie.ye
 * @date 2023/2/10 18:10
 */
@Component
@Order(1)
public class AuthorizationServerSignFactory implements AuthorizationFactory {
    @Override
    public boolean supports(String authorization) {
        return ServerSignUtil.isDigest(authorization);
    }

    @Override
    public AuthContext create(RequestContext requestContext, String authorization) {
        AuthorizationServerSign sign = ServerSignUtil.parse(authorization);
        if (sign == null) { throw AuthUtil.unAuthorityErr("无效签名格式"); }
        AuthContext context = new AuthContext();
        context.setTokenType(TokenType.SERVER_SIGN);
        context.setToken(sign.getSign());
        context.setTokenTime(sign.getTimestamp());
        context.setTokenId(requestContext.getRequestParamStr(ServiceConstants.JWT_TOKEN_ID));
        context.setUserId(requestContext.getRequestParamStr(ServiceConstants.JWT_USER_ID));
        context.setUserName(requestContext.getRequestParamStr(ServiceConstants.JWT_USERNAME));
        context.setAppCode(sign.getAppCode());
        context.setOrgCode(requestContext.getRequestParamStr(ServiceConstants.JWT_ORG_CODE));
        context.setClientCode(requestContext.getRequestParamStr(ServiceConstants.JWT_CLIENT_CODE));
        context.setServiceName(sign.getServiceName());
        context.setDataAuthority(requestContext.getRequestParamStr(ServiceConstants.DATA_AUTHORITY));
        return context;
    }
}
