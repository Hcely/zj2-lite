package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.api.AppApi;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.req.AppCreateSaveReq;
import org.zj2.common.uac.app.dto.req.AppEditSecretReq;
import org.zj2.common.uac.app.dto.req.AppQuery;
import org.zj2.lite.common.entity.result.ZListResp;
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
    AppDTO addApp(AppCreateSaveReq req);

    /**
     * 编辑应用
     * @param req
     */
    void editApp(AppCreateSaveReq req);

    /**
     * 编辑密钥
     * @param req
     */
    void editSecret(AppEditSecretReq req);

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

    /**
     * 查询应用
     * @param query
     * @return
     */
    ZListResp<AppDTO> pageQuery(AppQuery query);
}
