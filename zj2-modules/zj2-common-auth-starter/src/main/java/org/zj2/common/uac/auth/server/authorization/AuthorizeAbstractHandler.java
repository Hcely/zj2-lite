package org.zj2.common.uac.auth.server.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AuthorizeAbstactHandler
 *
 * @author peijie.ye
 * @date 2023/2/13 15:31
 */
public abstract class AuthorizeAbstractHandler implements AuthorizeHandler {
    protected AuthoritySet getAuthoritySet(RequestContext requestContext, AuthContext authContext) {
        return AuthManager.getAuthoritySet(authContext);
    }
}
