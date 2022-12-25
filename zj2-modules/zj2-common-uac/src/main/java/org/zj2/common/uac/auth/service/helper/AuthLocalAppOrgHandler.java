package org.zj2.common.uac.auth.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.service.AppClientService;
import org.zj2.common.uac.app.service.AppService;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.auth.dto.req.AuthReq;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;

/**
 *  AuthLocalAppOrgHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 8:35
 */
@Component
public class AuthLocalAppOrgHandler implements BizVHandler<AuthContext> {
    @Autowired
    private AppService appService;
    @Autowired
    private AppClientService appClientService;
    @Autowired
    private OrgService orgService;

    @Override
    public void handle(AuthContext context) {
        AuthReq req = context.getReq();
        loadApp(context, req);
        loadAppClient(context, req);
        loadOrg(context, req);
    }

    private void loadApp(AuthContext context, AuthReq req) {
        if (StringUtils.isEmpty(req.getAppCode())) {return;}
        AppDTO app = appService.getByCode(req.getAppCode());
        if (app == null) {throw ZRBuilder.failureErr("应用不存在");}
        if (BooleanUtil.isFalse(app.getEnableFlag())) {throw ZRBuilder.failureErr("应用被禁用");}
        context.setApp(app);
    }

    private void loadAppClient(AuthContext context, AuthReq req) {
        if (StringUtils.isEmpty(req.getAppCode())) {return;}
        if (StringUtils.isEmpty(req.getClientCode())) {
            if (appClientService.hasClient(req.getAppCode())) {throw ZRBuilder.failureErr("没有客户端");}
            return;
        }
        AppClientDTO client = appClientService.getByCode(req.getAppCode(), req.getClientCode());
        if (client == null) {throw ZRBuilder.failureErr("客户端不存在");}
        if (BooleanUtil.isFalse(client.getEnableFlag())) {throw ZRBuilder.failureErr("客户端被禁用");}
        context.setClient(client);
    }

    private void loadOrg(AuthContext context, AuthReq req) {
        if (StringUtils.isEmpty(req.getOrgCode())) {return;}
        OrgDTO org = orgService.get(req.getOrgCode());
        if (org == null) {throw ZRBuilder.failureErr("机构不存在");}
        if (BooleanUtil.isFalse(org.getEnableFlag())) {throw ZRBuilder.failureErr("机构被禁用");}
        context.setOrg(org);
    }
}
