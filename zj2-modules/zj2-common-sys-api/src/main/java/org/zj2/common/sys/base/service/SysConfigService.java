package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.dto.req.SysConfigQuery;
import org.zj2.common.sys.base.dto.req.SysConfigSaveReq;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 * SysConfigService
 *
 * @author peijie.ye
 * @date 2022/12/10 2:31
 */
public interface SysConfigService extends BaseInnerService<SysConfigDTO>, SysConfigApi {
    SysConfigDTO saveConfig(SysConfigSaveReq req);

    void removeConfig(String sysConfigId);

    ZListResp<SysConfigDTO> pageQuery(SysConfigQuery query);
}
