package org.zj2.common.uac.org.service;

import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.req.OrgEditReq;
import org.zj2.common.uac.org.dto.req.OrgQuery;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 *  OrgService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface OrgService extends BaseInnerService<OrgDTO>, OrgApi {
    /**
     * 创建
     * @param req
     * @return
     */
    OrgDTO create(OrgEditReq req);

    /**
     * 编辑
     * @param req
     */
    void edit(OrgEditReq req);

    /**
     * 启用
     * @param orgCode
     */
    void enable(String orgCode);

    /**
     * 禁用
     * @param orgCode
     */
    void disable(String orgCode);

    /**
     *
     * @param query
     * @return
     */
    ZListResp<OrgDTO> pageQuery(OrgQuery query);
}
