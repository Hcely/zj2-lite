package org.zj2.common.uac.auth.server;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.auth.server.authorization.AuthenticateHandler;
import org.zj2.common.uac.auth.server.authorization.AuthorizationFactory;
import org.zj2.common.uac.auth.server.authorization.AuthorizationNoneFactory;
import org.zj2.common.uac.auth.server.authorization.AuthorizeAfterHandler;
import org.zj2.common.uac.auth.server.authorization.AuthorizeBeforeHandler;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.spring.SpringBeanRef;

/**
 * AbstractAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
public abstract class AbstractAuthInterceptor {
    private static final SpringBeanRef<AuthorizationFactory[]> TOKEN_FACTORIES_REF = new SpringBeanRef<>(
            AuthorizationFactory[].class);
    private static final SpringBeanRef<AuthenticateHandler[]> AUTHENTICATE_HANDLERS_REF = new SpringBeanRef<>(
            AuthenticateHandler[].class);
    private static final SpringBeanRef<AuthorizeBeforeHandler[]> AUTHORIZE_BEFORE_HANDLERS_REF = new SpringBeanRef<>(
            AuthorizeBeforeHandler[].class);
    private static final SpringBeanRef<AuthorizeAfterHandler[]> AUTHORIZE_AFTER_HANDLERS_REF = new SpringBeanRef<>(
            AuthorizeAfterHandler[].class);

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
        return AuthorizationNoneFactory.INSTANCE.create(requestContext, token);
    }

    protected void authenticate(RequestContext requestContext, AuthContext authContext) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource != null && isRequiredAuthentication(uriResource)) {
            authenticate(requestContext, authContext, uriResource.getRequiredTokenTypes());
        }
    }

    protected boolean isRequiredAuthentication(UriResource resource) {
        return resource.isRequiredAuthentication();
    }

    protected void authenticate(RequestContext requestContext, AuthContext authContext,
            TokenType[] requiredTokenTypes) {
        if (StringUtils.isEmpty(authContext.getToken())) {
            throw AuthManager.unAuthenticationErr("缺失认证信息");
        }
        TokenType type = authContext.getTokenType();
        if (!CollUtil.contains(requiredTokenTypes, type)) { throw AuthManager.unAuthenticationErr("非法认证信息"); }
        AuthenticateHandler[] handlers = AUTHENTICATE_HANDLERS_REF.get();
        if (CollUtil.isNotEmpty(handlers)) {
            for (AuthenticateHandler handler : handlers) {
                if (handler.supports(type)) {
                    handler.authenticate(requestContext, authContext);
                    authContext.setAuthenticated(true);
                    return;
                }
            }
        }
        throw AuthManager.unAuthenticationErr("无效签名");
    }

    protected void authorizeBefore(RequestContext requestContext, AuthContext authContext) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource == null) { return; }
        AuthorizeBeforeHandler[] handlers = AUTHORIZE_BEFORE_HANDLERS_REF.get();
        if (CollUtil.isEmpty(handlers)) { return; }
        for (AuthorizeBeforeHandler handler : handlers) {
            if (handler.supports(requestContext, authContext, uriResource)) {
                handler.authorize(requestContext, authContext, uriResource);
            }
        }
    }

    protected void authorizeAfter(RequestContext requestContext, AuthContext authContext, Object result) {
        UriResource uriResource = authContext.getUriResource();
        if (uriResource == null) { return; }
        AuthorizeAfterHandler[] handlers = AUTHORIZE_AFTER_HANDLERS_REF.get();
        if (CollUtil.isEmpty(handlers)) { return; }
        for (AuthorizeAfterHandler handler : handlers) {
            if (handler.supports(requestContext, authContext, uriResource)) {
                handler.authorize(requestContext, authContext, uriResource, result);
            }
        }
    }
}

