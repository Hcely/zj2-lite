package org.zj2.common.uac.auth.server.authorization;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * AuthorizeUriResourceHandler
 *
 * @author peijie.ye
 * @date 2023/2/13 12:41
 */
@Component
public class AuthorizeUriResourceHandler extends AuthorizeAbstractHandler implements AuthorizeBeforeHandler {
    @Override
    public boolean supports(RequestContext requestContext, AuthContext authContext, UriResource uriResource) {
        if (authContext.getTokenType() == TokenType.SERVER_SIGN) { return false; }
        UriResource resource = authContext.getUriResource();
        return resource != null && resource.isRequiredUriAuthority();
    }

    @Override
    public void authorize(RequestContext requestContext, AuthContext authContext, UriResource uriResource) {
        String authority = getUriAuthority(requestContext, uriResource);
        final AuthoritySet authorities = getAuthoritySet(requestContext, authContext);
        if (authorities.notContainsAuthority(authority)) {
            throw AuthManager.unAuthorityErr("权限不足");
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
