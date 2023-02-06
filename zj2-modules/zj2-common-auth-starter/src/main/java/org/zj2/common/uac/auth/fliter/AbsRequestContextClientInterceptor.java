package org.zj2.common.uac.auth.fliter;

import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;

/**
 * DubboConsumerContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
public abstract class AbsRequestContextClientInterceptor<T> {
    protected void setContext(T request, String method, String uri) {
        setValue(request, ServiceConstants.JWT_TOKEN_ID, AuthenticationContext.currentTokenId());
        setValue(request, ServiceConstants.JWT_USER_ID, AuthenticationContext.currentUserId());
        setValue(request, ServiceConstants.JWT_USERNAME, AuthenticationContext.currentUsername());
        setValue(request, ServiceConstants.JWT_APP_CODE, AuthenticationContext.currentAppCode());
        setValue(request, ServiceConstants.JWT_ORG_CODE, AuthenticationContext.currentOrgCode());
        //
        setValue(request, ServiceConstants.REQUEST_ATTR_IP, ServiceRequestContext.currentAttrIp());
        setValue(request, ServiceConstants.REQUEST_DEVICE, ServiceRequestContext.currentDevice());
        //
        setAuthentication(request, method, uri);
    }

    private void setAuthentication(T request, String method, String uri) {
        AppDTO app = AuthUtil.getApp();
        if (app == null) { return; }
        String authentication = ServerSignUtil.buildAuthentication(app.getAppCode(), app.getAppSecret(), method, uri);
        setValue(request, ServiceConstants.REQUEST_AUTHORIZATION, authentication);
    }

    protected abstract void setValue(T request, String key, String value);


}
