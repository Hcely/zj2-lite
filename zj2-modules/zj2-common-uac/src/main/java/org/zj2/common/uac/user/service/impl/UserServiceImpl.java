package org.zj2.common.uac.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.uac.user.constant.UserConstants;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.dto.req.UserEditPasswordReq;
import org.zj2.common.uac.user.dto.req.UserEditValueReq;
import org.zj2.common.uac.user.dto.req.UserOperationReq;
import org.zj2.common.uac.user.dto.req.UserQuery;
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
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.util.CryptUtil;
import org.zj2.lite.util.ZRBuilder;

/**
 * UserServiceImpl
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
        return findUser(UserValueTypeEnum.ACCOUNT_NAME, accountName, null);
    }

    @Override
    public UserDTO getByEmail(String email) {
        return findUser(UserValueTypeEnum.EMAIL, email, null);
    }

    @Override
    public UserDTO getByMobile(String mobile) {
        return findUser(UserValueTypeEnum.MOBILE, mobile, UserConstants.DEF_MOBILE_AREA_CODE);
    }

    @Override
    public UserDTO getByMobile(String mobileAreaCode, String mobile) {
        return findUser(UserValueTypeEnum.MOBILE, mobile, mobileAreaCode);
    }

    @Override
    public UserDTO findUser(UserValueTypeEnum valueType, String userValue, String userExtValue) {
        String userId = userValueService.findUserId(valueType, userValue, userExtValue);
        return get(userId);
    }

    @Override
    public UserDTO getIfAbsentError(String userId) {
        UserDTO user = get(userId);
        if(user == null) { throw ZRBuilder.failureErr("用户账户不存在"); }
        return user;
    }

    @Override
    public ZListResp<UserDTO> pageQuery(UserQuery query) {
        return pageQuery(query, q -> query(wrapper(true).eq(UserDTO::getUserMobile, CryptUtil.encrypt(q.getUserMobile()))
                .eq(UserDTO::getUserEmail, CryptUtil.encrypt(q.getUseremail())).like(UserDTO::getUserName, q.getUserName())
                .eq(UserDTO::getActivateFlag, q.getActivateFlag()).eq(UserDTO::getEnableFlag, q.getEnableFlag()).orderByDesc(UserDTO::getUserId)));
    }

    @Override
    @Transactional
    public UserDTO create(UserCreateReq req) {
        log.info("createUser-req:{}", req);
        return userCreateHelper.handle(req);
    }

    @Override
    @Transactional
    public void editPassword(UserEditPasswordReq req) {
        log.info("editPassword-userId:{}", req.getUserId());
        userEditPasswordHelper.handle(req);
    }

    @Override
    @Transactional
    public void editUserValue(UserEditValueReq req) {
        log.info("editUserValue-req:{}", req);
        userEditValueHelper.handle(req);
    }

    @Override
    @Transactional
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
