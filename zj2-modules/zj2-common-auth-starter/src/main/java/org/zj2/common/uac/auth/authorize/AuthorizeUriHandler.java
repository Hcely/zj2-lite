package org.zj2.common.uac.auth.authorize;

import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.lite.service.auth.AuthoritySet;

/**
 *  DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
@Component
public class AuthorizeUriHandler extends AbstractAuthorizeHandler<String> {
    @Override
    public void authorize(String authorityResource) {
        final AuthoritySet authorities = getAuthorityResources();
        if (authorities.notContainsAuthority(authorityResource)) {
            throw AuthUtil.unAuthenticationErr("没有功能权限");
        }
    }
}
