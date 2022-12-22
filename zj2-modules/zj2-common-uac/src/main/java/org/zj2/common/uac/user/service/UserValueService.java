package org.zj2.common.uac.user.service;

import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserValueDTO;
import org.zj2.lite.service.BaseInnerService;

/**
 *  UserLogService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface UserValueService extends BaseInnerService<UserValueDTO> {
    boolean existUserValue(UserValueTypeEnum valueType, String userValue);

    boolean existUserValue(UserValueTypeEnum valueType, String userValue, String userExtValue);

    String findUserId(UserValueTypeEnum valueType, String userValue, String userExtValue);

    UserValueDTO addUserValue(boolean editValue, String userId, UserValueTypeEnum valueType, String userValue,
            String userExtValue);
}
