package org.zj2.common.uac.user.service;

import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.dto.req.UserEditPasswordReq;
import org.zj2.common.uac.user.dto.req.UserEditValueReq;
import org.zj2.common.uac.user.dto.req.UserOperationReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  UserService
 *
 * @author peijie.ye
 * @date 2022/11/27 20:28
 */
public interface UserService extends BaseInnerService<UserDTO>, UserApi {
    UserDTO getUser(UserValueTypeEnum valueType, String userValue, String userExtValue);

    UserDTO getIfAbsentError(String userId);

    UserDTO create(UserCreateReq user);

    void editPassword(UserEditPasswordReq req);

    void editUserValue(UserEditValueReq req);

    void enable(UserOperationReq req);

    void disable(UserOperationReq req);

    void forbidden(UserOperationReq req);

    void activate(UserOperationReq req);

    void valid(UserOperationReq req);
}
