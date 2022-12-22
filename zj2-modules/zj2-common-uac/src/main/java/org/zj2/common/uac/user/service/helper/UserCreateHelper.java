package org.zj2.common.uac.user.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.uac.user.constant.UserConstants;
import org.zj2.common.uac.user.constant.UserEventEnum;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.common.uac.user.service.UserPasswordService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.common.uac.user.service.UserValueService;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BeanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizRespHandler;

/**
 *  UserCreateHelper
 *
 * @author peijie.ye
 * @date 2022/12/1 9:53
 */
@Component
public class UserCreateHelper implements BizRespHandler<UserCreateReq, UserDTO> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPasswordService userPasswordService;
    @Autowired
    private UserValueService userValueService;
    @Autowired
    private UserLogService userLogService;

    @Override
    @Transactional
    public UserDTO handle(UserCreateReq req) {
        UserDTO user = BeanUtil.toBean(req, UserDTO.class);
        //填充字段
        fillParams(user);
        // 检查用户名唯一
        checkAccountName(user);
        checkUserMobile(user);
        checkUserEmail(user);
        // 保存
        saveUser(user);
        // 加日志
        addLog(user);
        return user;
    }

    private void fillParams(UserDTO user) {
        user.setUserId(null);
        user.setUserAccountName(StringUtils.trimToEmpty(user.getUserAccountName()));
        user.setUserMobile(StringUtils.trimToEmpty(user.getUserMobile()));
        user.setUserMobileAreaCode(StringUtils.trimToEmpty(user.getUserMobileAreaCode()));
        user.setUserEmail(StringUtils.trimToEmpty(user.getUserEmail()));
        user.setUserName(StringUtils.trimToEmpty(user.getUserName()));
        if (StringUtils.isNotEmpty(user.getUserMobile()) && StringUtils.isEmpty(user.getUserMobileAreaCode())) {
            user.setUserMobileAreaCode(UserConstants.CN_AREA_CODE);
        }
        user.setUserPassword(UserUtil.buildPassword(user.getUserPassword()));
        user.setEnableFlag(1);
        user.setEnabledTime(DateUtil.now());
    }

    private void checkAccountName(UserDTO user) {
        String accountName = user.getUserAccountName();
        if (StringUtils.isEmpty(accountName)) {return;}
        if (!UserUtil.isAccountName(accountName)) {
            throw ZRBuilder.failureErr("用户名不合法");
        }
        if (StringUtils.equalsIgnoreCase(accountName, user.getUserMobile())) {
            throw ZRBuilder.failureErr("用户名不能与手机号相同");
        }
        if (StringUtils.equalsIgnoreCase(accountName, user.getUserEmail())) {
            throw ZRBuilder.failureErr("用户名不能与邮箱相同");
        }
        boolean exist = userValueService.existUserValue(UserValueTypeEnum.ACCOUNT_NAME, accountName);
        if (exist) {throw ZRBuilder.failureErr("用户名已存在");}
    }

    private void checkUserMobile(UserDTO user) {
        String mobile = user.getUserMobile();
        if (StringUtils.isEmpty(mobile)) {return;}
        if (!UserUtil.isMobile(mobile)) {throw ZRBuilder.failureErr("手机号不合法");}
        boolean exist = userValueService.existUserValue(UserValueTypeEnum.MOBILE, mobile, user.getUserMobileAreaCode());
        if (exist) {throw ZRBuilder.failureErr("手机号已存在");}
    }

    private void checkUserEmail(UserDTO user) {
        String email = user.getUserEmail();
        if (StringUtils.isEmpty(email)) {return;}
        if (!UserUtil.isEmail(email)) {throw ZRBuilder.failureErr("邮箱不合法");}
        boolean exist = userValueService.existUserValue(UserValueTypeEnum.EMAIL, email);
        if (exist) {throw ZRBuilder.failureErr("邮箱已存在");}
    }


    private void saveUser(UserDTO user) {
        userService.add(user);
        String userId = user.getUserId();
        //
        if (StringUtils.isNotEmpty(user.getUserAccountName())) {
            userValueService.addUserValue(false, userId, UserValueTypeEnum.ACCOUNT_NAME, user.getUserAccountName(),
                    null);
        }
        if (StringUtils.isNotEmpty(user.getUserMobile())) {
            userValueService.addUserValue(false, userId, UserValueTypeEnum.MOBILE, user.getUserMobile(),
                    user.getUserMobileAreaCode());
        }
        if (StringUtils.isNotEmpty(user.getUserEmail())) {
            userValueService.addUserValue(false, userId, UserValueTypeEnum.EMAIL, user.getUserEmail(), null);
        }
        if (StringUtils.isNotEmpty(user.getUserPassword())) {
            userPasswordService.addPassword(false, userId, user.getUserPassword());
        }
    }

    private void addLog(UserDTO user) {
        UserLogDTO log = new UserLogDTO();
        log.setUserId(user.getUserId());
        log.setUserName(user.getUserName());
        log.setLogEvent(UserEventEnum.CREATE_USER.getCode());
        userLogService.addLogAfterCommit(log);
    }
}
