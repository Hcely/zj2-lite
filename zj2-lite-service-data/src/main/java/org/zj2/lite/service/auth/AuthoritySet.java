package org.zj2.lite.service.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PatternUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UserResources
 *
 * @author peijie.ye
 * @date 2023/1/2 19:22
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthoritySet implements Serializable {
    public static final String SUPPER_AUTHORITY = "_$SUPPER$_";
    private static final long serialVersionUID = 3292141917083713905L;
    private String tokenId;
    private String userId;
    private Map<String, Long> authorityPatterns;
    private Map<String, Long> authorityResources;

    public AuthoritySet(String tokenId, String userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public AuthoritySet addAuthority(String authority, long expireTime) {
        if (authority == null) { return this; }
        if (StringUtils.isEmpty(authority)) { return this; }
        if (expireTime > 0 && expireTime < System.currentTimeMillis()) { return this; }
        if (authority.indexOf('*') != -1) {
            if (authorityPatterns == null) { authorityPatterns = new HashMap<>(); }
            authorityPatterns.put(authority, expireTime);
        } else {
            if (authorityResources == null) { authorityResources = new LinkedHashMap<>(); }
            authorityResources.put(authority, expireTime);
        }
        return this;
    }

    private boolean isEmptyAuthorities() {
        return CollUtil.isEmpty(authorityResources) && CollUtil.isEmpty(authorityPatterns);
    }

    public boolean notContainsAuthority(String authority) {
        return !containsAuthority(authority);
    }

    public boolean containsAuthority(String authority) {
        if (StringUtils.isEmpty(authority)) { return false; }
        if (isEmptyAuthorities()) { return false; }
        long currentTime = System.currentTimeMillis();
        if (containsSupperAuthority(currentTime)) { return true; }
        return containsAuthority0(System.currentTimeMillis(), authority);
    }

    public boolean containsAllAuthorities(Collection<String> authorities) {
        if (CollUtil.isEmpty(authorities)) { return true; }
        if (isEmptyAuthorities()) { return false; }
        long currentTime = System.currentTimeMillis();
        if (containsSupperAuthority(currentTime)) { return true; }
        for (String e : authorities) {
            if (!containsAuthority0(currentTime, e)) { return false; }
        }
        return true;
    }

    public boolean containsAnyAuthorities(Collection<String> authorities) {
        if (CollUtil.isEmpty(authorities)) { return true; }
        if (isEmptyAuthorities()) { return false; }
        long currentTime = System.currentTimeMillis();
        if (containsSupperAuthority(currentTime)) { return true; }
        for (String e : authorities) {
            if (containsAuthority0(currentTime, e)) { return true; }
        }
        return false;
    }

    public boolean containsSupperAuthority() {
        return CollUtil.isNotEmpty(authorityResources) && containsSupperAuthority(System.currentTimeMillis());
    }

    private boolean containsSupperAuthority(long currentTime) {
        final Long expireTime = CollUtil.get(authorityResources, SUPPER_AUTHORITY);
        return expireTime != null && (expireTime <= 0 || expireTime > currentTime);
    }

    private boolean containsAuthority0(long currentTime, String authority) {
        if (StringUtils.isEmpty(authority) || StringUtils.equals(authority, SUPPER_AUTHORITY)) {
            return false;
        }
        Long expireTime = CollUtil.get(authorityResources, authority);
        if (expireTime == null) {
            expireTime = matchAuthority0(authority);
            if (expireTime == null) { return false; }
        }
        return expireTime <= 0 || expireTime > currentTime;
    }

    private Long matchAuthority0(String authority) {
        if (CollUtil.isEmpty(authorityPatterns)) { return null; }
        for (Map.Entry<String, Long> e : authorityPatterns.entrySet()) {
            if (PatternUtil.matchPath(e.getKey(), authority)) { return e.getValue(); }
        }
        return null;
    }
}
