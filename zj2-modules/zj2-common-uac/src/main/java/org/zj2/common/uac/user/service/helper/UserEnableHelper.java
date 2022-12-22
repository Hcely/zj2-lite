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
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.common.util.BooleanUtil;

/**
 *  UserEditValueHelper
 *
 * @author peijie.ye
 * @date 2022/12/2 18:38
 */
@Component
public class UserEnableHelper implements BizVHandler<UserOperationReq> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLogService userLogService;

    @Override
    @Transactional
    public void handle(UserOperationReq req) {
        UserDTO user = userService.getIfAbsentError(req.getUserId());
        if (BooleanUtil.isTrue(user.getEnableFlag())) {return;}
        //
        updateStatus(req.getUserId());
        //
        addLog(user, req);
    }

    private void updateStatus(String userId) {
        UserDTO update = new UserDTO();
        update.setUserId(userId);
        update.setEnableFlag(1);
        update.setEnabledTime(DateUtil.now());
        update.setDisabledTime(NoneConstants.NONE_DATE);
        userService.updateById(update);
    }

    private void addLog(UserDTO user, UserOperationReq req) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.ENABLED.getCode());
        log.setLogRemark(req.getRemark());
        userLogService.addLogAfterCommit(log);
    }
}
