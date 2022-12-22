package org.zj2.common.uac.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.user.constant.UserConstants;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.dto.req.UserEditPasswordReq;
import org.zj2.common.uac.user.dto.req.UserEditValueReq;
import org.zj2.common.uac.user.dto.req.UserOperationReq;
import org.zj2.common.uac.user.entity.User;
import org.zj2.common.uac.user.mapper.UserMapper;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.common.uac.user.service.UserValueService;
import org.zj2.common.uac.user.service.helper.UserActivateHelper;
import org.zj2.common.uac.user.service.helper.UserCreateHelper;
import org.zj2.common.uac.user.service.helper.UserDisableHelper;
import org.zj2.common.uac.user.service.helper.UserEditPasswordHelper;
import org.zj2.common.uac.user.service.helper.UserEditValueHelper;
import org.zj2.common.uac.user.service.helper.UserEnableHelper;
import org.zj2.common.uac.user.service.helper.UserForbiddenHelper;
import org.zj2.common.uac.user.service.helper.UserValidHelper;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.service.BaseServiceImpl;

/**
 *  UserServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User, UserDTO> implements UserService {
    @Autowired
    private UserValueService userValueService;
    @Autowired
    private UserCreateHelper userCreateHelper;
    @Autowired
    private UserEditPasswordHelper userEditPasswordHelper;
    @Autowired
    private UserEditValueHelper userEditValueHelper;
    @Autowired
    private UserEnableHelper userEnableHelper;
    @Autowired
    private UserDisableHelper userDisableHelper;
    @Autowired
    private UserForbiddenHelper userForbiddenHelper;
    @Autowired
    private UserValidHelper userValidHelper;
    @Autowired
    private UserActivateHelper userActivateHelper;

    @Override
    public UserDTO getByAccountName(String accountName) {
        return getUser(UserValueTypeEnum.ACCOUNT_NAME, accountName, null);
    }

    @Override
    public UserDTO getByEmail(String email) {
        return getUser(UserValueTypeEnum.EMAIL, email, null);
    }

    @Override
    public UserDTO getByMobile(String mobile) {
        return getUser(UserValueTypeEnum.MOBILE, mobile, UserConstants.CN_AREA_CODE);
    }

    @Override
    public UserDTO getByMobile(String mobileAreaCode, String mobile) {
        return getUser(UserValueTypeEnum.MOBILE, mobile, mobileAreaCode);
    }

    @Override
    public UserDTO getUser(UserValueTypeEnum valueType, String userValue, String userExtValue) {
        String userId = userValueService.findUserId(valueType, userValue, userExtValue);
        return get(userId);
    }

    @Override
    public UserDTO getIfAbsentError(String userId) {
        UserDTO user = get(userId);
        if (user == null) {throw ZRBuilder.failureErr("用户账户不存在");}
        return user;
    }

    @Override
    public UserDTO create(UserCreateReq user) {
        log.info("createUser-user:{}", user);
        return userCreateHelper.handle(user);
    }

    @Override
    public void editPassword(UserEditPasswordReq req) {
        log.info("editPassword-userId:{}", req.getUserId());
        userEditPasswordHelper.handle(req);
    }

    @Override
    public void editUserValue(UserEditValueReq req) {
        log.info("editUserValue-req:{}", req);
        userEditValueHelper.handle(req);
    }

    @Override
    public void enable(UserOperationReq req) {
        log.info("enable-req:{}", req);
        userEnableHelper.handle(req);
    }

    @Override
    public void disable(UserOperationReq req) {
        log.info("disable-req:{}", req);
        userDisableHelper.handle(req);
    }

    @Override
    public void forbidden(UserOperationReq req) {
        log.info("forbidden-req:{}", req);
        userForbiddenHelper.handle(req);
    }

    @Override
    public void activate(UserOperationReq req) {
        log.info("activate-req:{}", req);
        userActivateHelper.handle(req);
    }

    @Override
    public void valid(UserOperationReq req) {
        log.info("valid-req:{}", req);
        userValidHelper.handle(req);
    }
}
