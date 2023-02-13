package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.service.AppApi;
import org.zj2.common.uac.auth.service.AuthorityApi;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.spring.SpringBeanRef;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppUtil
 *
 * @author peijie.ye
 * @date 2022/12/9 2:12
 */
public class AuthManager {
    private static final SpringBeanRef<AppApi> appApiRef = new SpringBeanRef<>(AppApi.class);
    private static final SpringBeanRef<AuthorityApi> authorityApiRef = new SpringBeanRef<>(AuthorityApi.class);

    public static AppDTO getApp(String appCode) {
        AppApi api = appApiRef.get();
        if (api == null) { return null; }
        appCode = StringUtils.defaultIfEmpty(appCode, AppDTO.COMMON_APP_CODE);
        String key = AppDTO.getCacheKey(appCode);
        return CacheUtil.DEF_CACHE.getCache(key, appCode, api::getByCode);
    }

    public static AuthoritySet getAuthoritySet(AuthContext context) {
        final String tokenId = context.getTokenId();
        final String userId = context.getUserId();
        final AuthorityApi authorityApi = authorityApiRef.get();
        if (authorityApi == null) { return new AuthoritySet(tokenId, userId); }
        //
        if (StringUtils.isEmpty(tokenId) || !context.isAuthenticated()) {
            return new AuthoritySet(tokenId, userId);
        }
        AuthoritySet authorityResources = CacheUtil.DEF_CACHE.get(AuthoritySet.class, tokenId,
                authorityApi::getAuthorities, 180_000);
        return authorityResources == null ? new AuthoritySet(tokenId, userId) : authorityResources;
    }

    public static ZError unAuthenticationErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(401);
    }

    public static ZError unAuthorityErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(403);
    }
}
