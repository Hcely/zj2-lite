package org.zj2.common.uac.user.service;

import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.service.BaseApi;
import org.zj2.lite.service.annotation.ApiReference;

/**
 *  UserApi
 *
 * @author peijie.ye
 * @date 2022/11/27 20:28
 */
@ApiReference
public interface UserApi extends BaseApi<UserDTO> {
    UserDTO getByAccountName(String accountName);

    UserDTO getByEmail(String email);

    UserDTO getByMobile(String mobile);

    UserDTO getByMobile(String mobileAreaCode, String mobile);

    UserDTO getUser(UserValueTypeEnum valueType, String userValue, String userExtValue);


}
