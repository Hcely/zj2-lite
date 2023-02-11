package org.zj2.common.uac.auth.client;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.util.CollUtil;
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
        AppDTO app = AuthManager.getApp(authContext.getAppCode());
        setValue(request, ServiceConstants.JWT_TOKEN_ID, authContext.getTokenId());
        setValue(request, ServiceConstants.JWT_USER_ID, authContext.getUserId());
        setValue(request, ServiceConstants.JWT_USERNAME, authContext.getUserName());
        setValue(request, ServiceConstants.JWT_APP_CODE, app == null ? authContext.getAppCode() : app.getAppCode());
        setValue(request, ServiceConstants.JWT_ORG_CODE, authContext.getOrgCode());
        //
        setValue(request, ServiceConstants.REQUEST_ROOT_URI, RequestContext.currentRootUri());
        setValue(request, ServiceConstants.REQUEST_ATTR_IP, RequestContext.currentAttrIp());
        setValue(request, ServiceConstants.REQUEST_DEVICE, RequestContext.currentDevice());
        //
        setValue(request, ServiceConstants.DATA_AUTHORITY, authContext.getDataAuthority());
        if (app != null) {
            setValue(request, ServiceConstants.AUTHORIZATION,
                    ServerSignUtil.buildAuthorization(app.getAppCode(), app.getAppSecret(), method, uri));
        }
    }

    protected abstract void setValue(T request, String key, String value);
}
