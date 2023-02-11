package org.zj2.common.uac.auth.server;

import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.context.AuthContext;

/**
 * DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
@Component
public class AuthorizeUriHandler implements AuthorizeHandler<String> {
    @Override
    public void authorize(AuthContext context, String authorityResource) {
        final AuthoritySet authorities = AuthManager.getAuthoritySet(context);
        if (authorities.notContainsAuthority(authorityResource)) {
            throw AuthManager.unAuthenticationErr("没有功能权限");
        }
    }
}
