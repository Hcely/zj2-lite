package org.zj2.common.uac.auth.service.helper;

import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.util.ZRBuilder;

import java.time.LocalDateTime;

/**
 *  AuthUserCheckHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 7:59
 */
@Component
public class AuthUserCheckHandler implements BizVHandler<AuthContext> {
    @Override
    public void handle(AuthContext context) {
        UserDTO user = context.getUser();
        if (BooleanUtil.isFalse(user.getEnableFlag())) {
            throw ZRBuilder.failureErr("用户账户被禁用");
        }
        LocalDateTime now = DateUtil.now();
        if (user.getForbiddenExpireTime() != null && DateUtil.lt(now, user.getForbiddenExpireTime())) {
            throw ZRBuilder.failureErr("用户账户被停用，截至到{}", user.getForbiddenExpireTime());
        }
        if (user.getValidExpireTime() != null && DateUtil.gt(now, user.getValidExpireTime())) {
            throw ZRBuilder.failureErr("用户账户到期停用");
        }
    }
}
