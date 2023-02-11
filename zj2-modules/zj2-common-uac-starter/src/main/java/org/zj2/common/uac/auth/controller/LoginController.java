package org.zj2.common.uac.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.auth.dto.req.AuthNamePwReq;
import org.zj2.common.uac.auth.dto.req.LoginReq;
import org.zj2.common.uac.auth.service.AuthenticationApi;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.service.auth.AuthenticationIgnored;
import org.zj2.lite.service.auth.AuthorizationJWT;
import org.zj2.lite.util.ZRBuilder;

/**
 * LoginController
 *
 * @author peijie.ye
 * @date 2023/2/7 10:28
 */
@Api(tags = "uac-登录模块")
@RestController
@RequestMapping("/api/uac/auth")
@AuthenticationIgnored
public class LoginController {
    @Value("${zj2.uac.appCode:common}")
    private String appCode;
    @Autowired
    private AuthenticationApi authenticationApi;

    @ApiOperation("登录接口")
    @PostMapping("login")
    public ZResp<AuthorizationJWT> login(LoginReq req) {
        AuthNamePwReq authReq = new AuthNamePwReq();
        authReq.setName(req.getName());
        authReq.setNameExtValue(req.getNameHeader());
        authReq.setPassword(req.getPassword());
        authReq.setAppCode(appCode);
        authReq.setOrgCode(req.getOrgCode());
        authReq.setClientCode(req.getClientCode());
        AuthorizationJWT jwt = authenticationApi.authenticateNamePw(authReq);
        return ZRBuilder.successResp(jwt);
    }
}
