package org.zj2.common.uac.auth.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.auth.dto.req.AuthNamePwReq;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.common.uac.user.service.helper.UserUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.common.entity.result.ZRBuilder;

/**
 *  AuthUserNamePwHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 9:20
 */
@Component
public class AuthNamePwHandler implements BizVHandler<AuthContext> {
    @Autowired
    private UserService userService;

    @Override
    public void handle(AuthContext context) {
        AuthNamePwReq req = context.getReq();
        UserDTO user = getUser(req);
        if (user == null) {throw ZRBuilder.failureErr("用户账号或密码错误");}
        if (!StringUtils.equalsIgnoreCase(UserUtil.buildPassword(req.getPassword()), user.getUserPassword())) {
            throw ZRBuilder.failureErr("用户账号或密码错误");
        }
        context.setUser(user);
    }

    private UserDTO getUser(AuthNamePwReq req) {
        UserDTO user = null;
        if (UserUtil.isMobile(req.getName())) {
            user = userService.findUser(UserValueTypeEnum.MOBILE, req.getName(), req.getNameExtValue());
        } else if (UserUtil.isEmail(req.getName())) {
            user = userService.findUser(UserValueTypeEnum.MOBILE, req.getName(), null);
        }
        if (user == null) {
            user = userService.findUser(UserValueTypeEnum.ACCOUNT_NAME, req.getName(), null);
        }
        return user;
    }
}
