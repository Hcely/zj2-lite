package org.zj2.lite.service.auth.helper;

import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AuthorizeBeforeHandler
 *
 * @author peijie.ye
 * @date 2023/2/13 10:51
 */
public interface AuthAfterCompletedHandler extends AuthHandler {
    void authorize(RequestContext requestContext, AuthContext authContext, UriResource uriResource, Object result);
}
