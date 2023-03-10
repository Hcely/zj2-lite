package org.zj2.common.uac.user.service;

import org.zj2.common.uac.user.dto.UserPasswordDTO;
import org.zj2.lite.service.BaseInnerService;

/**
 * UserLogService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface UserPasswordService extends BaseInnerService<UserPasswordDTO> {
    UserPasswordDTO addPassword(boolean editPassword, String userId, String password);
}
