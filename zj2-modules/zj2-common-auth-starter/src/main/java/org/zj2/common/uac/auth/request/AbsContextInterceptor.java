package org.zj2.common.uac.auth.request;

import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.util.AppUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;

/**
 *  DubboConsumerContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
public abstract class AbsContextInterceptor<T> {
    protected void setContext(T request, String method, String uri) {
        setValue(request, AuthenticationContext.USER_ID, AuthenticationContext.currentUserId());
        setValue(request, AuthenticationContext.USERNAME, AuthenticationContext.currentUserName());
        setValue(request, AuthenticationContext.APP_CODE, AuthenticationContext.currentAppCode());
        setValue(request, AuthenticationContext.ORG_CODE, AuthenticationContext.currentOrgCode());
        //
        setValue(request, ServiceRequestContext.ATTR_IP, ServiceRequestContext.currentAttrIp());
        setValue(request, ServiceRequestContext.DEVICE, ServiceRequestContext.currentDevice());
        //
        setAuthentication(request, method, uri);
    }

    private void setAuthentication(T request, String method, String uri) {
        AppDTO app = AppUtil.getApp();
        if (app == null) {return;}
        String authentication = ServerSignUtil.buildAuthentication(app.getAppCode(), app.getAppSecret(), method, uri);
        setValue(request, ServiceRequestContext.AUTHORIZATION, authentication);
    }

    protected abstract void setValue(T request, String key, String value);


}
