package org.zj2.common.uac.auth.filter;

import org.zj2.common.uac.auth.dto.AuthenticationJWT;
import org.zj2.common.uac.auth.dto.AuthenticationSign;
import org.zj2.common.uac.auth.util.JWTUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 *  AuthenticationWebFilter
 *
 * @author peijie.ye
 * @date 2022/12/4 14:25
 */
public abstract class AbsContextFilter<T> {

    protected void setContext(T request, String method, String uri) {
        final String token = getValue(request, ServiceRequestContext.AUTHORIZATION);
        final String attrIp = getAttrIp(request);
        final String device = getDevice(request);
        if (JWTUtil.isJWT(token)) {
            handleJWT(token, method, uri, attrIp, device);
        } else if (ServerSignUtil.isDigest(token)) {
            handleSign(request, token, method, uri, attrIp, device);
        } else {
            handleNoToken(request, method, uri, attrIp, device);
        }
    }

    private void handleJWT(String token, String method, String uri, String attrIp, String device) {
        AuthenticationJWT jwt = JWTUtil.parse(token);
        if (jwt == null) {throw ZRBuilder.failureErr("无效token格式").setStatus(403);}
        AuthenticationContext.setContext(jwt.getUserId(), jwt.getUserName(), jwt.getAppCode(), jwt.getOrgCode());
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setTokenType(TokenType.CLIENT);
        requestContext.setToken(token);
        requestContext.setNamespace(jwt.getNamespace());
        requestContext.setTokenTime(jwt.getExpireAt());
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.setContext(requestContext);
    }

    private void handleSign(T request, String token, String method, String uri, String attrIp, String device) {
        AuthenticationSign sign = ServerSignUtil.parse(token);
        if (sign == null) {throw ZRBuilder.failureErr("无效签名格式").setStatus(403);}
        String userId = getValue(request, AuthenticationContext.USER_ID);
        String username = getValue(request, AuthenticationContext.USERNAME);
        String orgCode = getValue(request, AuthenticationContext.ORG_CODE);
        AuthenticationContext.setContext(userId, username, sign.getAppCode(), orgCode);
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setTokenType(TokenType.SERVER);
        requestContext.setToken(sign.getSign());
        requestContext.setTokenTime(sign.getTimestamp());
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.setContext(requestContext);
    }

    private void handleNoToken(T request, String method, String uri, String attrIp, String device) {
        String userId = getValue(request, AuthenticationContext.USER_ID);
        String username = getValue(request, AuthenticationContext.USERNAME);
        String appCode = getValue(request, AuthenticationContext.APP_CODE);
        String orgCode = getValue(request, AuthenticationContext.ORG_CODE);
        AuthenticationContext.setContext(userId, username, appCode, orgCode);
        ServiceRequestContext requestContext = new ServiceRequestContext();
        requestContext.setMethod(method);
        requestContext.setUri(uri);
        requestContext.setAttrIp(attrIp);
        requestContext.setDevice(device);
        ServiceRequestContext.setContext(requestContext);
    }

    protected abstract String getValue(T request, String key);

    protected abstract String getAttrIp(T request);

    protected abstract String getDevice(T request);

}
