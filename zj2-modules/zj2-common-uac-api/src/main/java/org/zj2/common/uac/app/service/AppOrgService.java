package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.api.AppOrgApi;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.dto.AppOrgExtDTO;
import org.zj2.common.uac.app.dto.req.AppOrgAddReq;
import org.zj2.common.uac.app.dto.req.AppOrgQuery;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 *  AppOrgService
 *
 * @author peijie.ye
 * @date 2022/12/3 7:37
 */
public interface AppOrgService extends BaseInnerService<AppOrgDTO>, AppOrgApi {
    /**
     * 添加机构
     * @param req
     * @return
     */
    AppOrgDTO addOrg(AppOrgAddReq req);

    /**
     * 移除机构
     * @param appOrgId
     */
    void removeOrg(String appOrgId);

    /**
     * 启用
     * @param appOrgId
     */
    void enable(String appOrgId);

    /**
     * 禁用
     * @param appOrgId
     */
    void disable(String appOrgId);

    /**
     * 查询应用机构
     * @param query
     * @return
     */
    ZListResp<AppOrgExtDTO> pageQuery(AppOrgQuery query);
}
