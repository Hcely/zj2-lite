package org.zj2.common.uac.auth.server.authorization;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.auth.api.TokenAuthorityApi;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.auth.helper.AnonymityAuthorityProvider;
import org.zj2.lite.service.auth.helper.AuthHandler;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.spring.SpringBeanRef;

/**
 * AuthorizeAbstactHandler
 *
 * @author peijie.ye
 * @date 2023/2/13 15:31
 */
public abstract class AuthAbstractHandler implements AuthHandler {
    private static final SpringBeanRef<TokenAuthorityApi> TOKEN_AUTHORITY_REF = new SpringBeanRef<>(
            TokenAuthorityApi.class);
    private static final SpringBeanRef<AnonymityAuthorityProvider[]> ANONYMITY_PROVIDERS_REF = new SpringBeanRef<>(
            AnonymityAuthorityProvider[].class);

    protected AuthoritySet getAuthoritySet(RequestContext requestContext, AuthContext authContext) {
        final String tokenId = authContext.getTokenId();
        final String userId = authContext.getUserId();
        AuthoritySet authoritySet;
        if (StringUtils.isEmpty(tokenId) || authContext.isAuthenticated()) {
            authoritySet = getTokenAuthoritySet(tokenId);
        } else {
            authoritySet = getAnonymityAuthoritySet(requestContext, authContext);
        }
        return authoritySet == null ? new AuthoritySet(tokenId, userId) : authoritySet;
    }

    private AuthoritySet getTokenAuthoritySet(String tokenId) {
        final TokenAuthorityApi tokenAuthorityApi = TOKEN_AUTHORITY_REF.get();
        if (tokenAuthorityApi == null) { return null; }
        return CacheUtil.DEF_CACHE.get(AuthoritySet.class, tokenId, tokenAuthorityApi::getAuthorities, 180_000);
    }

    private AuthoritySet getAnonymityAuthoritySet(RequestContext requestContext, AuthContext authContext) {
        AnonymityAuthorityProvider[] authorityProviders = ANONYMITY_PROVIDERS_REF.get();
        if (CollUtil.isNotEmpty(authorityProviders)) {
            for (AnonymityAuthorityProvider provider : authorityProviders) {
                AuthoritySet authoritySet = provider.get(requestContext, authContext);
                if (authoritySet != null) { return authoritySet; }
            }
        }
        return null;
    }
}
