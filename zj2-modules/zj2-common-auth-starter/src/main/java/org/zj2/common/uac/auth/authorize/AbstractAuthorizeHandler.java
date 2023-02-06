package org.zj2.common.uac.auth.authorize;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.auth.service.AuthorityApi;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.spring.SpringBeanRef;

/**
 * DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
public abstract class AbstractAuthorizeHandler<T> {
    private static final SpringBeanRef<AuthorityApi> authorityApiRef = new SpringBeanRef<>(AuthorityApi.class);

    public abstract void authorize(T value);

    protected AuthoritySet getAuthoritySet() {
        final String tokenId = AuthenticationContext.currentTokenId();
        final String userId = AuthenticationContext.currentUserId();
        if (StringUtils.isEmpty(tokenId)) { return new AuthoritySet(tokenId, userId); }
        AuthorityApi authorityApi = authorityApiRef.get();
        if (authorityApi == null) { return new AuthoritySet(tokenId, userId); }
        AuthoritySet authorityResources = CacheUtil.DEF_CACHE.get(AuthoritySet.class, tokenId,
                authorityApi::getAuthorities, 180_000);
        return authorityResources == null ? new AuthoritySet(tokenId, userId) : authorityResources;
    }

}
