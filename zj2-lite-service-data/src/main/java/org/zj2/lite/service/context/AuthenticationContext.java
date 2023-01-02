package org.zj2.lite.service.context;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.context.BaseContext;
import org.zj2.lite.service.constant.ServiceConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  UacContext
 *
 * @author peijie.ye
 * @date 2022/11/23 16:16
 */
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationContext extends BaseContext {
    private static final int IDX = nextIdx();


    public static AuthenticationContext current() {
        return getSubContext(IDX, AuthenticationContext::new);
    }

    public static String currentUserId() {
        AuthenticationContext context = getSubContext(IDX, null);
        return context == null ? "" : context.getUserId();
    }

    public static String currentUserName() {
        AuthenticationContext context = getSubContext(IDX, null);
        return context == null ? "" : context.getUserName();
    }

    public static String currentAppCode() {
        AuthenticationContext context = getSubContext(IDX, null);
        return context == null ? "" : context.getAppCode();
    }

    public static String currentOrgCode() {
        AuthenticationContext context = getSubContext(IDX, null);
        return context == null ? "" : context.getOrgCode();
    }

    public static AuthenticationContext setContext(String userId, String userName, String appCode, String orgCode) {
        return setContext(new AuthenticationContext(userId, userName, appCode, orgCode));
    }

    public static AuthenticationContext setContext(AuthenticationContext context) {
        return setSubContext(IDX, context);
    }

    public static AuthenticationContext clearContext() {
        return clearSubContext(IDX);
    }


    protected String userId;
    protected String userName;
    protected String appCode;
    protected String orgCode;

    public AuthenticationContext(AuthenticationContext context) {
        this.userId = context.userId;
        this.userName = context.userName;
        this.appCode = context.appCode;
        this.orgCode = context.orgCode;
    }

    public Map<String, String> buildHeaders() {
        Map<String, String> result = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(userId)) {result.put(ServiceConstants.JWT_USER_ID, userId);}
        if (StringUtils.isNotEmpty(userName)) {result.put(ServiceConstants.JWT_USERNAME, userName);}
        if (StringUtils.isNotEmpty(appCode)) {result.put(ServiceConstants.JWT_APP_CODE, appCode);}
        if (StringUtils.isNotEmpty(orgCode)) {result.put(ServiceConstants.JWT_ORG_CODE, orgCode);}
        return result;
    }

    public AuthenticationContext setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AuthenticationContext setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public AuthenticationContext setAppCode(String appCode) {
        this.appCode = appCode;
        return this;
    }

    public AuthenticationContext setOrgCode(String orgCode) {
        this.orgCode = orgCode;
        return this;
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
