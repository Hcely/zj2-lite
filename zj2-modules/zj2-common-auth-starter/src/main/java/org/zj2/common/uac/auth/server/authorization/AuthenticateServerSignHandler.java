package org.zj2.common.uac.auth.server.authorization;

import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.auth.helper.AuthenticateHandler;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * AuthenticateServerSignHandler
 *
 * @author peijie.ye
 * @date 2023/2/10 12:07
 */
@Component
public class AuthenticateServerSignHandler implements AuthenticateHandler {
    private static final long SIGN_TIMEOUT = 60000L * 5;

    @Override
    public boolean supports(AuthContext context) {
        return context.getTokenType() == TokenType.SERVER_SIGN;
    }

    @Override
    public void authenticate(RequestContext requestContext, AuthContext authContext) {
        long timestamp = authContext.getTokenTime();
        long now = System.currentTimeMillis();
        if (timestamp > now - 1000 || timestamp < now - SIGN_TIMEOUT) {
            throw AuthUtil.unAuthenticationErr("请求过期");
        }
        if (!ServerSignUtil.valid(requestContext, authContext)) {
            throw AuthUtil.unAuthenticationErr("非法请求");
        }
    }
}
