package org.zj2.common.uac.auth.fliter;

import org.zj2.common.uac.auth.util.JWTValidUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.auth.AuthenticationJWT;
import org.zj2.lite.service.auth.AuthenticationSign;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.util.ZRBuilder;

/**
 * AuthenticationWebFilter
 *
 * @author peijie.ye
 * @date 2022/12/4 14:25
 */
public abstract class AbsRequestContextServerFilter<T> {

    protected void setContext(T request, String method, String uri) {
        final String token = getValue(request, ServiceConstants.REQUEST_AUTHORIZATION);
        final String attrIp = getAttrIp(request);
        final String device = getDevice(request);
        if (JWTValidUtil.isJWT(token)) {
            handleJWT(request, token, method, uri, attrIp, device);
        } else if (ServerSignUtil.isDigest(token)) {
            handleSign(request, token, method, uri, attrIp, device);
        } else {
            handleNoToken(request, method, uri, attrIp, device);
        }
    }

    private void handleJWT(T request, String token, String method, String uri, String attrIp, String device) {
        AuthenticationJWT jwt = JWTValidUtil.parse(token);
        if (jwt == null) { throw ZRBuilder.failureErr("无效token格式").setStatus(403); }
        AuthenticationContext.set(jwt);
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setRequest(request);
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.set(requestContext);
    }

    private void handleSign(T request, String token, String method, String uri, String attrIp, String device) {
        AuthenticationSign sign = ServerSignUtil.parse(token);
        if (sign == null) { throw ZRBuilder.failureErr("无效签名格式").setStatus(403); }
        String tokenId = getValue(request, ServiceConstants.JWT_TOKEN_ID);
        String userId = getValue(request, ServiceConstants.JWT_USER_ID);
        String userName = getValue(request, ServiceConstants.JWT_USERNAME);
        String orgCode = getValue(request, ServiceConstants.JWT_ORG_CODE);
        AuthenticationContext.set(sign, tokenId, userId, userName, orgCode);
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setRequest(request);
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.set(requestContext);
    }

    private void handleNoToken(T request, String method, String uri, String attrIp, String device) {
        AuthenticationContext authenticationContext = new AuthenticationContext();
        authenticationContext.setUserId(getValue(request, ServiceConstants.JWT_USER_ID));
        authenticationContext.setUserName(getValue(request, ServiceConstants.JWT_USERNAME));
        authenticationContext.setAppCode(getValue(request, ServiceConstants.JWT_APP_CODE));
        authenticationContext.setOrgCode(getValue(request, ServiceConstants.JWT_ORG_CODE));
        AuthenticationContext.set(authenticationContext);
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.set(requestContext);
    }

    protected abstract String getValue(T request, String key);

    protected abstract String getAttrIp(T request);

    protected abstract String getDevice(T request);

}
