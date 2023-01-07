package org.zj2.common.uac.auth.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.common.uac.app.service.AppUserService;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.common.Supportable;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  AuthAppUserCheckHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 7:59
 */
@Component
public class AuthAppUserCheckHandler implements Supportable<AuthContext>, BizVHandler<AuthContext> {
    @Autowired
    private AppUserService appUserService;

    @Override
    public boolean supports(AuthContext context) {
        return context.getOrg() == null && context.getApp() != null;
    }

    @Override
    public void handle(AuthContext context) {
        AppDTO app = context.getApp();
        UserDTO user = context.getUser();
        AppUserDTO appUser = appUserService.getAppUser(app.getAppCode(), user.getUserId());
        if (appUser == null) {
            if (BooleanUtil.isFalse(app.getAllowAllUser())) { throw ZRBuilder.failureErr("应用账号无法使用"); }
        } else if (BooleanUtil.isFalse(appUser.getEnableFlag())) {
            throw ZRBuilder.failureErr("应用账号无法使用");
        }
    }


}
