package org.zj2.common.uac.user.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.uac.user.constant.UserEventEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.dto.req.UserOperationReq;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  UserEditValueHelper
 *
 * @author peijie.ye
 * @date 2022/12/2 18:38
 */
@Component
public class UserForbiddenHelper implements BizVHandler<UserOperationReq> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLogService userLogService;

    @Override
    @Transactional
    public void handle(UserOperationReq req) {
        UserDTO user = userService.getIfAbsentError(req.getUserId());
        //
        updateExpire(req);
        //
        addLog(user, req);
    }

    private void updateExpire(UserOperationReq req) {
        UserDTO update = new UserDTO();
        update.setUserId(req.getUserId());
        update.setForbiddenExpireTime(req.getExpireTime() == null ? NoneConstants.NONE_DATE : req.getExpireTime());
        userService.updateById(update);
    }

    private void addLog(UserDTO user, UserOperationReq req) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.SET_FORBIDDEN.getCode());
        log.setLogReferenceTime(req.getExpireTime());
        log.setLogRemark(req.getRemark());
        userLogService.addLogAfterCommit(log);
    }
}
