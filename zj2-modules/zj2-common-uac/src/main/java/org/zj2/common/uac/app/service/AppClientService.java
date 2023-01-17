package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.req.AppClientQuery;
import org.zj2.common.uac.app.dto.req.AppClientSaveReq;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 *  AppClientService
 *
 * @author peijie.ye
 * @date 2022/12/6 11:05
 */
public interface AppClientService extends BaseInnerService<AppClientDTO>, AppClientApi {
    /**
     * 创建
     * @param req
     * @return
     */
    AppClientDTO saveClient(AppClientSaveReq req);

    /**
     * 启用
     * @param appClientId
     */
    void enable(String appClientId);

    /**
     * 禁用
     * @param appClientId
     */
    void disable(String appClientId);

    /**
     * 查询应用客户端
     * @param query
     * @return
     */
    ZListResp<AppClientDTO> pageQuery(AppClientQuery query);
}
