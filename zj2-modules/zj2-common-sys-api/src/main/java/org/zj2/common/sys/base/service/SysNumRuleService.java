package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.SysNumRuleQuery;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

/**
 * SysNumRuleService
 *
 * @author peijie.ye
 * @date 2022/12/24 13:29
 */
public interface SysNumRuleService extends BaseInnerService<SysNumRuleDTO> {
    SysNumRuleDTO saveRule(SysNumRuleDTO rule);

    SysNumRuleDTO getRule(String numRuleCode);

    void removeRule(String numRuleCode);

    ZListResp<SysNumRuleDTO> pageQuery(SysNumRuleQuery query);
}
