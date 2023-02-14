package org.zj2.lite.service.auth.helper;

import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AnonymityAuthorityProvider
 *
 * @author peijie.ye
 * @date 2023/2/13 15:40
 */
public interface AnonymityAuthorityProvider {
    AuthoritySet get(RequestContext requestContext, AuthContext authContext);
}
