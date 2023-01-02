package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.req.AppCreateSaveReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  AppService
 *
 * @author peijie.ye
 * @date 2022/12/3 7:37
 */
public interface AppService extends BaseInnerService<AppDTO>, AppApi {
    /**
     * 创建应用
     * @param req
     * @return
     */
    AppDTO createApp(AppCreateSaveReq req);

    /**
     * 编辑应用
     * @param req
     */
    void editApp(AppCreateSaveReq req);

    /**
     * 编辑密钥
     * @param appCode
     * @param appSecret
     */
    void editSecret(String appCode, String appSecret);

    /**
     * 启用
     * @param appCode
     */
    void enable(String appCode);

    /**
     * 禁用
     * @param appCode
     */
    void disable(String appCode);
}
