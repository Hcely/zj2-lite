package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.common.uac.app.dto.AppUserPlusDTO;
import org.zj2.common.uac.app.dto.req.AppQuery;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 *  AppUserService
 *
 * @author peijie.ye
 * @date 2022/12/3 7:37
 */
public interface AppUserService extends BaseInnerService<AppUserDTO>, AppUserApi {
    /**
     * 添加用户
     * @param appCode
     * @param userId
     * @return
     */
    AppUserDTO addUser(String appCode, String userId);

    /**
     * 移除用户
     * @param appUserId
     */
    void removeUser(String appUserId);

    /**
     * 启用
     * @param appUserId
     */
    void enable(String appUserId);

    /**
     * 禁用
     * @param appUserId
     */
    void disable(String appUserId);

    /**
     * 查询应用用户
     * @param query
     * @return
     */
    ZListResp<AppUserPlusDTO> pageQuery(AppQuery query);
}
