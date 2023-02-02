package org.zj2.common.uac.user.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.UacReferences;
import org.zj2.common.uac.user.constant.UserConstants;
import org.zj2.common.uac.user.constant.UserEventEnum;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.dto.UserValueDTO;
import org.zj2.common.uac.user.dto.req.UserEditValueReq;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.common.uac.user.service.UserValueService;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.util.ZRBuilder;

/**
 *  UserEditValueHelper
 *
 * @author peijie.ye
 * @date 2022/12/2 18:38
 */
@Component
public class UserEditValueHelper implements BizVHandler<UserEditValueReq> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserValueService userValueService;
    @Autowired
    private UserLogService userLogService;

    @Override
    public void handle(UserEditValueReq req) {
        UserDTO user = userService.getIfAbsentError(req.getUserId());
        //
        fillParams(req);
        //
        checkParams(req);
        // 检查是否需要更新
        if (!needUpdate(user, req)) {
            userService.logger().info("用户值[{}]无需更新", req.getValueType());
            return;
        }
        // 检查唯一性
        if (StringUtils.isNotEmpty(req.getUserValue())) {
            if (userValueService.existUserValue(req.getValueType(), req.getUserValue(), req.getUserExtValue())) {
                throw ZRBuilder.failureErr("{}已存在", req.getValueType().getDesc());
            }
        }
        // 更新
        UserValueDTO userValue = updateUserValue(req);
        // 加日志
        addLog(user, req, userValue);
    }

    private void fillParams(UserEditValueReq req) {
        req.setUserValue(StringUtils.trimToEmpty(req.getUserValue()));
        req.setUserExtValue(StringUtils.trimToEmpty(req.getUserExtValue()));
        if (UserValueTypeEnum.MOBILE.eq(req.getValueType())) {
            if (StringUtils.isEmpty(req.getUserValue())) {
                req.setUserExtValue("");
            } else if (StringUtils.isEmpty(req.getUserExtValue())) {
                req.setUserExtValue(UserConstants.DEF_MOBILE_AREA_CODE);
            }
        }
    }

    private void checkParams(UserEditValueReq req) {
        UserValueTypeEnum valueType = req.getValueType();
        if (valueType == null) { throw ZRBuilder.failureErr("未知用户值类型"); }
        String userValue = req.getUserValue();
        if (StringUtils.isEmpty(userValue)) { return; }
        if (UserValueTypeEnum.MOBILE.eq(valueType)) {
            if (!UserUtil.isMobile(userValue)) { throw ZRBuilder.failureErr("不合法手机号"); }
        } else if (UserValueTypeEnum.EMAIL.eq(valueType)) {
            if (!UserUtil.isEmail(userValue)) { throw ZRBuilder.failureErr("不合法邮箱"); }
        }
    }

    private boolean needUpdate(UserDTO user, UserEditValueReq req) {
        switch (req.getValueType()) {
            case ACCOUNT_NAME:
                return !StringUtils.equalsIgnoreCase(user.getUserAccountName(), req.getUserValue());
            case MOBILE:
                return !StringUtils.equalsIgnoreCase(user.getUserMobile(), req.getUserValue())
                        || !StringUtils.equalsIgnoreCase(user.getUserMobileAreaCode(), req.getUserExtValue());
            case EMAIL:
                return !StringUtils.equalsIgnoreCase(user.getUserEmail(), req.getUserValue());
            default:
                return false;
        }
    }

    private UserValueDTO updateUserValue(UserEditValueReq req) {
        UserDTO update = new UserDTO();
        update.setUserId(req.getUserId());
        UserValueTypeEnum valueType = req.getValueType();
        if (UserValueTypeEnum.ACCOUNT_NAME.eq(valueType)) {
            update.setUserAccountName(req.getUserValue());
        } else if (UserValueTypeEnum.MOBILE.eq(valueType)) {
            update.setUserMobile(req.getUserValue());
            update.setUserMobileAreaCode(req.getUserExtValue());
        } else if (UserValueTypeEnum.EMAIL.eq(valueType)) {
            update.setUserEmail(req.getUserValue());
        }
        userService.updateById(update);
        return userValueService.addUserValue(true, req.getUserId(), req.getValueType(), req.getUserValue(),
                req.getUserExtValue());
    }

    private void addLog(UserDTO user, UserEditValueReq req, UserValueDTO userValue) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        UserValueTypeEnum valueType = req.getValueType();
        if (UserValueTypeEnum.ACCOUNT_NAME.eq(valueType)) {
            log.setLogEvent(UserEventEnum.EDIT_ACCOUNT_NAME.getCode());
        } else if (UserValueTypeEnum.MOBILE.eq(valueType)) {
            log.setLogEvent(UserEventEnum.EDIT_MOBILE.getCode());
        } else if (UserValueTypeEnum.EMAIL.eq(valueType)) {
            log.setLogEvent(UserEventEnum.EDIT_EMAIL.getCode());
        }
        if (userValue == null) {
            log.setLogRemark("设置成空");
        } else {
            log.setLogReferenceType(UacReferences.USER_VALUE.getCode());
            log.setLogReferenceId(userValue.getUserValueId());
            log.setLogRemark("更新");
        }
        userLogService.addLogAfterCommit(log);
    }
}
