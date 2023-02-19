package org.zj2.lite.service.auth.helper;

import org.zj2.lite.Supportable;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AuthentacateHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 11:46
 */
public interface AuthenticateHandler extends Supportable<AuthContext> {
    @Override
    boolean supports(AuthContext context);

    void authenticate(RequestContext requestContext, AuthContext authContext);
}
