package org.zj2.lite.service.auth.helper;

import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AuthorizeHandler
 *
 * @author peijie.ye
 * @date 2023/2/13 10:58
 */
public interface AuthHandler {
    boolean supports(RequestContext requestContext, AuthContext authContext, UriResource uriResource);
}
