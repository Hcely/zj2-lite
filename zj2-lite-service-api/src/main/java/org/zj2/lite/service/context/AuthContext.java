package org.zj2.lite.service.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.context.ThreadContext;
import org.zj2.lite.common.context.ZContext;
import org.zj2.lite.service.auth.UriResource;

/**
 * UacContext
 *
 * @author peijie.ye
 * @date 2022/11/23 16:16
 */
@Setter
@Getter
@NoArgsConstructor
public class AuthContext extends ZContext {
    private static final ThreadContext<AuthContext> THREAD_CONTEXT = new ThreadContext<>();

    public static AuthContext current() {
        return THREAD_CONTEXT.get(AuthContext::new);
    }

    public static AuthContext get() {
        return THREAD_CONTEXT.get();
    }

    public static String currentTokenId() {
        AuthContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getTokenId();
    }

    public static String currentUserId() {
        AuthContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getUserId();
    }

    public static String currentUsername() {
        AuthContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getUserName();
    }

    public static String currentAppCode() {
        AuthContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getAppCode();
    }

    public static String currentOrgCode() {
        AuthContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getOrgCode();
    }

    public static AuthContext set(AuthContext context) {
        return THREAD_CONTEXT.set(context);
    }

    protected Object authorization;
    protected TokenType tokenType;
    protected String token;
    protected long tokenTime;
    protected String tokenId;
    protected String namespace;
    protected String userId;
    protected String userName;
    protected String appCode;
    protected String orgCode;
    protected String clientCode;
    protected String serviceName;
    protected String rootService;
    protected String dataAuthority;
    protected UriResource uriResource;
    protected boolean authenticated = false;

    public String getDataAuthority() {
        if (StringUtils.isNotEmpty(dataAuthority)) {
            return dataAuthority;
        } else if (uriResource != null) {
            return uriResource.getDataAuthority();
        } else {
            return "";
        }
    }

    public String getTokenId() {
        return tokenId == null ? "" : tokenId;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public String getAppCode() {
        return appCode == null ? "" : appCode;
    }

    public String getClientCode() {
        return clientCode == null ? "" : clientCode;
    }

    public String getOrgCode() {
        return orgCode == null ? "" : orgCode;
    }
}
