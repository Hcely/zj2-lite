package org.zj2.lite.service.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.context.ThreadContext;
import org.zj2.lite.common.context.ZContext;
import org.zj2.lite.service.auth.AuthenticationJWT;
import org.zj2.lite.service.auth.AuthenticationSign;
import org.zj2.lite.service.constant.ServiceConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UacContext
 *
 * @author peijie.ye
 * @date 2022/11/23 16:16
 */
@Setter
@Getter
@NoArgsConstructor
public class AuthenticationContext extends ZContext {
    private static final ThreadContext<AuthenticationContext> THREAD_CONTEXT = new ThreadContext<>();

    public static AuthenticationContext current() {
        return THREAD_CONTEXT.get(AuthenticationContext::new);
    }

    public static String currentTokenId() {
        AuthenticationContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getTokenId();
    }

    public static String currentUserId() {
        AuthenticationContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getUserId();
    }

    public static String currentUsername() {
        AuthenticationContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getUserName();
    }

    public static String currentAppCode() {
        AuthenticationContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getAppCode();
    }

    public static String currentOrgCode() {
        AuthenticationContext context = THREAD_CONTEXT.get();
        return context == null ? "" : context.getOrgCode();
    }

    public static AuthenticationContext set(AuthenticationJWT jwt) {
        AuthenticationContext context = new AuthenticationContext();
        context.tokenType = TokenType.JWT;
        context.token = jwt.getToken();
        context.tokenTime = jwt.getExpireAt();
        context.namespace = jwt.getNamespace();
        context.tokenId = jwt.getTokenId();
        context.userId = jwt.getUserId();
        context.userName = jwt.getUserName();
        context.appCode = jwt.getAppCode();
        context.orgCode = jwt.getOrgCode();
        return set(context);
    }

    public static AuthenticationContext set(AuthenticationSign sign, String tokenId, String userId, String userName,
            String orgCode) {
        AuthenticationContext context = new AuthenticationContext();
        context.tokenType = TokenType.SIGN;
        context.tokenId = tokenId;
        context.token = sign.getSign();
        context.userId = userId;
        context.userName = userName;
        context.appCode = sign.getAppCode();
        context.orgCode = orgCode;
        return set(context);
    }

    public static AuthenticationContext set(AuthenticationContext context) {
        return THREAD_CONTEXT.set(context);
    }

    public static AuthenticationContext clear() {
        return THREAD_CONTEXT.clear();
    }

    protected TokenType tokenType;
    private String token;
    private long tokenTime;
    protected String tokenId;
    private String namespace;
    protected String userId;
    protected String userName;
    protected String appCode;
    protected String orgCode;
    protected boolean authenticated = false;

    public AuthenticationContext(AuthenticationContext context) {
        this.tokenType = context.tokenType;
        this.token = context.token;
        this.tokenTime = context.tokenTime;
        this.tokenId = context.tokenId;
        this.namespace = context.namespace;
        this.userId = context.userId;
        this.userName = context.userName;
        this.appCode = context.appCode;
        this.orgCode = context.orgCode;
        this.authenticated = context.authenticated;
    }

    public Map<String, String> buildHeaders() {
        Map<String, String> result = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(tokenId)) { result.put(ServiceConstants.JWT_TOKEN_ID, tokenId); }
        if (StringUtils.isNotEmpty(userId)) { result.put(ServiceConstants.JWT_USER_ID, userId); }
        if (StringUtils.isNotEmpty(userName)) { result.put(ServiceConstants.JWT_USERNAME, userName); }
        if (StringUtils.isNotEmpty(appCode)) { result.put(ServiceConstants.JWT_APP_CODE, appCode); }
        if (StringUtils.isNotEmpty(orgCode)) { result.put(ServiceConstants.JWT_ORG_CODE, orgCode); }
        return result;
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

    public String getOrgCode() {
        return orgCode == null ? "" : orgCode;
    }
}
