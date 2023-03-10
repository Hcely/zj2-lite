package org.zj2.common.uac.user.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.uac.UacReferences;
import org.zj2.common.uac.user.constant.UserEventEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.dto.UserPasswordDTO;
import org.zj2.common.uac.user.dto.req.UserEditPasswordReq;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.common.uac.user.service.UserPasswordService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 * UserEditPasswordHelper
 *
 * @author peijie.ye
 * @date 2022/12/2 17:58
 */
@Component
public class UserEditPasswordHelper implements BizVHandler<UserEditPasswordReq> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPasswordService userPasswordService;
    @Autowired
    private UserLogService userLogService;

    @Override
    @Transactional
    public void handle(UserEditPasswordReq req) {
        UserDTO user = userService.getIfAbsentError(req.getUserId());
        //
        req.setPassword(UserUtil.buildPassword(req.getPassword()));
        // 更新密码
        UserPasswordDTO userPassword = updatePassword(req);
        // 加日志
        addLog(user, userPassword);
    }

    private UserPasswordDTO updatePassword(UserEditPasswordReq req) {
        UserDTO update = new UserDTO();
        update.setUserId(req.getUserId());
        update.setUserPassword(req.getPassword());
        userService.updateById(update);
        // 加密码记录
        return userPasswordService.addPassword(true, req.getUserId(), req.getPassword());
    }

    private void addLog(UserDTO user, UserPasswordDTO userPassword) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.EDIT_PASSWORD.getCode());
        log.setLogReferenceType(UacReferences.USER_PASSWORD.getCode());
        log.setLogReferenceId(userPassword.getUserPasswordId());
        userLogService.addLogAfterCommit(log);
    }
}
