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
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  UserEditValueHelper
 *
 * @author peijie.ye
 * @date 2022/12/2 18:38
 */
@Component
public class UserActivateHelper implements BizVHandler<UserOperationReq> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLogService userLogService;

    @Override
    @Transactional
    public void handle(UserOperationReq req) {
        UserDTO user = userService.getIfAbsentError(req.getUserId());
        if (BooleanUtil.isTrue(user.getActivateFlag())) {return;}
        //
        updateActivate(req.getUserId());
        //
        addLog(user, req);
    }

    private void updateActivate(String userId) {
        UserDTO update = new UserDTO();
        update.setUserId(userId);
        update.setActivateFlag(1);
        update.setActivatedTime(DateUtil.now());
        userService.updateById(update);
    }

    private void addLog(UserDTO user, UserOperationReq req) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.ACTIVATED.getCode());
        log.setLogRemark(req.getRemark());
        userLogService.addLogAfterCommit(log);
    }
}
