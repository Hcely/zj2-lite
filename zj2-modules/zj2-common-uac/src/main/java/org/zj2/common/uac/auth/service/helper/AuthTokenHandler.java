package org.zj2.common.uac.auth.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.auth.service.JWTokenService;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.user.constant.UserEventEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.service.auth.AuthenticationJWT;

import java.time.LocalDateTime;

/**
 *  AuthUserNamePwHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 9:20
 */
@Component
public class AuthTokenHandler implements BizVHandler<AuthContext> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLogService userLogService;
    @Autowired
    private JWTokenService jwtokenService;

    @Override
    public void handle(AuthContext context) {
        AuthenticationJWT token = buildToken(context);
        context.setToken(token);
        if (StringUtils.isNotEmpty(token.getNamespace())) {
            jwtokenService.setToken(token.getAppCode(), token.getUserId(), token.getNamespace(), token.getToken(),
                    token.getExpireAt());
        }
        updateLoginTime(context);
        addLoginLog(context);
    }

    private AuthenticationJWT buildToken(AuthContext context) {
        UserDTO user = context.getUser();
        AppDTO app = context.getApp();
        AppClientDTO client = context.getClient();
        OrgDTO org = context.getOrg();
        AuthenticationJWT token = new AuthenticationJWT();
        token.setUserId(user.getUserId());
        token.setUserName(user.getUserName());
        Long timeout = null;
        String namespace = null;
        if (app != null) {
            token.setAppCode(app.getAppCode());
            timeout = app.getTokenTimeout();
            if (client != null) {
                timeout = client.getTokenTimeout();
                namespace = client.getNamespace();
            }
            if (BooleanUtil.isTrue(app.getSingleSignOn()) && StringUtils.isEmpty(namespace)) { namespace = "1"; }
        }
        if (timeout == null) { timeout = 3600000L * 4; }
        token.setExpireAt(System.currentTimeMillis() + timeout);
        token.setNamespace(namespace);
        if (org != null) { token.setOrgCode(org.getOrgCode()); }
        token.setToken(JWTBuildUtil.build(app == null ? null : app.getAppSecret(), token));
        return token;
    }

    private void updateLoginTime(AuthContext context) {
        UserDTO user = context.getUser();
        LocalDateTime now = DateUtil.now();
        UserDTO update = new UserDTO();
        update.setUserId(user.getUserId());
        if (user.getFirstLoginTime() == null) { update.setFirstLoginTime(now); }
        update.setLastLoginTime(now);
        userService.updateById(update);
    }

    private void addLoginLog(AuthContext context) {
        UserDTO user = context.getUser();
        AppDTO app = context.getApp();
        AppClientDTO client = context.getClient();
        OrgDTO org = context.getOrg();
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.LOGIN.getCode());
        log.setLogTime(DateUtil.now());
        if (client != null) { log.setLogClientCode(client.getClientCode()); }
        if (app != null) { log.setAppCode(app.getAppCode()); }
        if (org != null) { log.setOrgCode(org.getOrgCode()); }
        userLogService.addLogAfterCommit(log);
    }
}
