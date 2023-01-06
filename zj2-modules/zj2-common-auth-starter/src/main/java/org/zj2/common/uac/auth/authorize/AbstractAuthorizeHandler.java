package org.zj2.common.uac.auth.authorize;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.auth.dto.UserAuthorityResources;
import org.zj2.common.uac.auth.service.AuthorityApi;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.spring.SpringBeanRef;

/**
 *  DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
public abstract class AbstractAuthorizeHandler<T> {
    private static final SpringBeanRef<AuthorityApi> authorityApiRef = new SpringBeanRef<>(AuthorityApi.class);

    public abstract void authorize(T value);

    protected UserAuthorityResources getAuthorityResources() {
        AuthenticationContext cxt = AuthenticationContext.current();
        final String userId = AuthenticationContext.currentUserId();
        if (StringUtils.isEmpty(userId)) { return new UserAuthorityResources(userId); }
        AuthorityApi authorityApi = authorityApiRef.get();
        if (authorityApi == null) { return new UserAuthorityResources(userId); }
        final String appCode = cxt.getAppCode();
        final String orgCode = cxt.getOrgCode();
        String key = UserAuthorityResources.getCacheKey(appCode, orgCode, userId);
        UserAuthorityResources authorityResources = CacheUtil.DEF_CACHE.getCache(key, userId,
                e -> authorityApi.getUserAuthorities(appCode, orgCode, userId));
        //
        return authorityResources == null ? new UserAuthorityResources(userId) : authorityResources;
    }

}
