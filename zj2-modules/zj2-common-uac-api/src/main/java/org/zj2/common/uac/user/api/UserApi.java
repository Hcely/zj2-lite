package org.zj2.common.uac.user.api;

import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

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

    UserDTO findUser(UserValueTypeEnum valueType, String userValue, String userExtValue);

}
