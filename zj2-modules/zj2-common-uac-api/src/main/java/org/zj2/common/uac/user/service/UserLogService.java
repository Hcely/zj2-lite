package org.zj2.common.uac.user.service;

import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.lite.service.BaseInnerService;

/**
 * UserLogService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface UserLogService extends BaseInnerService<UserLogDTO> {
    void addLogAfterCommit(UserLogDTO log);
}
