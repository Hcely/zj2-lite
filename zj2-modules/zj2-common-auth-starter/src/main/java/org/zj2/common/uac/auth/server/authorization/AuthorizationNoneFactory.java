package org.zj2.common.uac.auth.server.authorization;

import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AunthorizationNoneFactory
 *
 * @author peijie.ye
 * @date 2023/2/13 15:23
 */
public class AuthorizationNoneFactory implements AuthorizationFactory {
    public static final AuthorizationNoneFactory INSTANCE = new AuthorizationNoneFactory();

    @Override
    public boolean supports(String authorization) {
        return false;
    }

    @Override
    public AuthContext create(RequestContext requestContext, String authorization) {
        AuthContext context = new AuthContext();
        context.setUserId(requestContext.getRequestParamStr(ServiceConstants.JWT_USER_ID));
        context.setUserName(requestContext.getRequestParamStr(ServiceConstants.JWT_USERNAME));
        context.setAppCode(requestContext.getRequestParamStr(ServiceConstants.JWT_APP_CODE));
        context.setOrgCode(requestContext.getRequestParamStr(ServiceConstants.JWT_ORG_CODE));
        context.setClientCode(requestContext.getRequestParamStr(ServiceConstants.JWT_CLIENT_CODE));
        return context;
    }
}
