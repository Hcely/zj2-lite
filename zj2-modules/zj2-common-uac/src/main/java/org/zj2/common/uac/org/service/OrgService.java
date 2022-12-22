package org.zj2.common.uac.org.service;

import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.req.OrgCreateReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  UserLogService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface OrgService extends BaseInnerService<OrgDTO>, OrgApi {
    OrgDTO create(OrgCreateReq req);

    void enable(String orgCode);

    void disable(String orgCode);
}
