package org.zj2.common.uac.auth.server.authenticate;

import org.zj2.lite.common.Supportable;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * AuthentacateHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 11:46
 */
public interface AuthenticateHandler extends Supportable<TokenType> {
    void authenticate(RequestContext requestContext, AuthContext authContext);
}
