package org.zj2.common.uac.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 *  UserResources
 *
 * @author peijie.ye
 * @date 2023/1/2 19:22
 */
@Getter
@Setter
@NoArgsConstructor
public class UserAuthorityResources implements Serializable {
    private static final long serialVersionUID = 3292141917083713905L;

    public static String getCacheKey(String appCode, String orgCode, String userId) {
        return StrUtil.concat("USER_AUTHORITY:", appCode, orgCode, userId);
    }

    private String userId;
    private Map<String, UserAuthorityResource> authorityResources;

    public UserAuthorityResources(String userId) {
        this.userId = userId;
    }

    public boolean containsAuthority(String authority) {
        if (StringUtils.isEmpty(authority)) { return false; }
        final Map<String, UserAuthorityResource> localResources = authorityResources;
        return CollUtil.isNotEmpty(localResources) && containsAuthority0(localResources, authority);
    }

    public boolean containsAllAuthorities(Collection<String> authorities) {
        if (CollUtil.isEmpty(authorities)) { return true; }
        final Map<String, UserAuthorityResource> localResources = authorityResources;
        if (CollUtil.isEmpty(localResources)) { return false; }
        for (String e : authorities) {
            if (!containsAuthority0(localResources, e)) { return false; }
        }
        return true;
    }

    public boolean containsAnyAuthorities(Collection<String> authorities) {
        if (CollUtil.isEmpty(authorities)) { return true; }
        final Map<String, UserAuthorityResource> localResources = authorityResources;
        if (CollUtil.isEmpty(localResources)) { return false; }
        for (String e : authorities) {
            if (containsAuthority0(localResources, e)) { return true; }
        }
        return false;
    }

    private static boolean containsAuthority0(Map<String, UserAuthorityResource> authorityResources, String authority) {
        UserAuthorityResource resource = authorityResources.get(authority);
        if (resource == null) { return false; }
        long expireTime = resource.getExpireTime();
        return expireTime < 0 || expireTime > System.currentTimeMillis();
    }
}
