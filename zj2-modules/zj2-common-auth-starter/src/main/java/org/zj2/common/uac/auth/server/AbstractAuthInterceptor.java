package org.zj2.common.uac.auth.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.zj2.common.uac.auth.server.authenticate.AuthenticateHandler;
import org.zj2.common.uac.auth.server.authorization.AuthorizationFactory;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.spring.SpringBeanRef;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * AbstractAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
public abstract class AbstractAuthInterceptor {
    private static final SpringBeanRef<AuthenticateHandler[]> AUTHENTICATE_HANDLERS_REF = new SpringBeanRef<>(
            AuthenticateHandler[].class);
    private static final SpringBeanRef<AuthorizeUriHandler> AUTHORIZE_URI_HANDLER_REF = new SpringBeanRef<>(
            AuthorizeUriHandler.class);
    private static final SpringBeanRef<AuthorizePropertyHandler> AUTHORIZE_PROPERTY_HANDLER_REF = new SpringBeanRef<>(
            AuthorizePropertyHandler.class);

    private static final SpringBeanRef<AuthorizationFactory[]> TOKEN_FACTORIES_REF = new SpringBeanRef<>(
            AuthorizationFactory[].class);

    protected AuthContext initAuthContext(RequestContext requestContext) {
        AuthContext context = AuthContext.get();
        if (context != null) { return null; }
        final String authorization = getAuthorizationValue(requestContext);
        AuthContext authContext = createAuthContext(requestContext, authorization);
        AuthContext.set(authContext);
        return authContext;
    }

    protected String getAuthorizationValue(RequestContext requestContext) {
        return requestContext.getRequestParamStr(ServiceConstants.AUTHORIZATION);
    }

    protected AuthContext createAuthContext(RequestContext requestContext, String token) {
        if (StringUtils.isNotEmpty(token)) {
            AuthorizationFactory[] factories = TOKEN_FACTORIES_REF.get();
            if (factories != null && factories.length > 0) {
                for (AuthorizationFactory factory : factories) {
                    if (factory.supports(token)) { return factory.create(requestContext, token); }
                }
            }
        }
        return buildNoneAuthContext(requestContext);
    }

    protected AuthContext buildNoneAuthContext(RequestContext requestContext) {
        AuthContext context = new AuthContext();
        context.setUserId(requestContext.getRequestParamStr(ServiceConstants.JWT_USER_ID));
        context.setUserName(requestContext.getRequestParamStr(ServiceConstants.JWT_USERNAME));
        context.setAppCode(requestContext.getRequestParamStr(ServiceConstants.JWT_APP_CODE));
        context.setOrgCode(requestContext.getRequestParamStr(ServiceConstants.JWT_ORG_CODE));
        return context;
    }

    protected void authenticate(RequestContext requestContext, AuthContext authContext) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource == null) { return; }
        if (!uriResource.isRequiredAuthentication()) { return; }
        authenticate(requestContext, authContext, uriResource.getRequiredTokenTypes());
    }

    protected void authenticate(RequestContext requestContext, AuthContext authContext,
            TokenType[] requiredTokenTypes) {
        if (StringUtils.isEmpty(authContext.getToken())) {
            throw AuthManager.unAuthenticationErr("缺失认证信息");
        }
        TokenType type = authContext.getTokenType();
        if (!CollUtil.contains(requiredTokenTypes, type)) { throw AuthManager.unAuthenticationErr("非法认证信息"); }
        AuthenticateHandler[] handlers = AUTHENTICATE_HANDLERS_REF.get();
        if (handlers != null && handlers.length > 0) {
            for (AuthenticateHandler handler : handlers) {
                if (handler.supports(type)) {
                    handler.authenticate(requestContext, authContext);
                    authContext.setAuthenticated(true);
                }
            }
        }
        throw AuthManager.unAuthenticationErr("无效签名");
    }

    protected void authoriseUri(RequestContext requestContext, AuthContext authContext) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource == null) { return; }
        // 无需授权
        if (!uriResource.isRequiredUriAuthority()) { return; }
        // 未认证
        if (!authContext.isAuthenticated()) { throw AuthManager.unAuthenticationErr("未认证请求"); }
        // 服务签名 无需授权
        if (authContext.getTokenType() == TokenType.SERVER_SIGN) { return; }
        //
        AuthorizeUriHandler handler = AUTHORIZE_URI_HANDLER_REF.get();
        if (handler != null) {
            final String authority = getUriAuthority(requestContext, uriResource);
            handler.authorize(authContext, authority);
        }
    }

    protected void authoriseProperties(AuthContext authContext, Object data) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource == null) { return; }
        if (uriResource.isRequiredPropertyAuthority() && authContext.getTokenType() == TokenType.JWT) {
            AuthorizePropertyHandler handler = AUTHORIZE_PROPERTY_HANDLER_REF.get();
            if (handler != null) {
                handler.authorize(authContext, data);
            }
        }
    }

    private static String getUriAuthority(RequestContext requestContext, UriResource uriResource) {
        String uriAuthority = uriResource.getUriAuthority();
        Object request = requestContext.request();
        if (request instanceof HttpServletRequest) {
            Object pathParams = ((HttpServletRequest) request).getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (pathParams instanceof Map && CollUtil.isNotEmpty((Map<?, ?>) pathParams)) {
                return StrUtil.formatObj(uriAuthority, pathParams);
            }
        }
        return uriAuthority;
    }
}

