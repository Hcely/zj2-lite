package org.zj2.common.uac.auth.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.service.AppOrgService;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.lite.common.Supportable;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.util.ZRBuilder;

/**
 *  AuthUserCheckHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 7:59
 */
@Component
public class AuthAppOrgCheckStatusHandler implements Supportable<AuthContext>, BizVHandler<AuthContext> {
    @Autowired
    private AppOrgService appOrgService;

    @Override
    public boolean supports(AuthContext context) {
        return context.getOrg() != null && context.getApp() != null;
    }

    @Override
    public void handle(AuthContext context) {
        AppDTO app = context.getApp();
        OrgDTO org = context.getOrg();
        AppOrgDTO appOrg = appOrgService.getAppOrg(app.getAppCode(), org.getOrgCode());
        if (appOrg == null) { throw ZRBuilder.failureErr("机构应用不能使用"); }
        if (BooleanUtil.isFalse(appOrg.getEnableFlag())) { throw ZRBuilder.failureErr("机构应用停用"); }
    }
}
