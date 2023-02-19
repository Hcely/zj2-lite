package org.zj2.lite.service.auth.helper;

import org.zj2.lite.Supportable;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * TokenHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 17:59
 */
public interface AuthorizationFactory extends Supportable<String> {
    @Override
    boolean supports(String authorization);

    AuthContext create(RequestContext requestContext, String authorization);
}
