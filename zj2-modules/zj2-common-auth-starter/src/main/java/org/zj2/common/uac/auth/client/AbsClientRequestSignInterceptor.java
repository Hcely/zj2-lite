package org.zj2.common.uac.auth.client;

import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * DubboConsumerContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
public abstract class AbsClientRequestSignInterceptor<T> {
    protected void setRequestContext(T request, String method, String uri) {
        AuthContext authContext = AuthContext.current();
        setValue(request, ServiceConstants.JWT_TOKEN_ID, authContext.getTokenId());
        setValue(request, ServiceConstants.JWT_USER_ID, authContext.getUserId());
        setValue(request, ServiceConstants.JWT_USERNAME, authContext.getUserName());
        setValue(request, ServiceConstants.JWT_APP_CODE, authContext.getAppCode());
        setValue(request, ServiceConstants.JWT_ORG_CODE, authContext.getOrgCode());
        setValue(request, ServiceConstants.JWT_CLIENT_CODE, authContext.getClientCode());
        //
        setValue(request, ServiceConstants.REQUEST_ROOT_URI, RequestContext.currentRootUri());
        setValue(request, ServiceConstants.REQUEST_ATTR_IP, RequestContext.currentAttrIp());
        setValue(request, ServiceConstants.REQUEST_DEVICE, RequestContext.currentDevice());
        //
        setValue(request, ServiceConstants.DATA_AUTHORITY, authContext.getDataAuthority());
        setValue(request, ServiceConstants.AUTHORIZATION, ServerSignUtil.buildAuthorization(authContext, method, uri));
    }

    protected abstract void setValue(T request, String key, String value);
}
