package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.lite.service.ApiReference;

/**
 * SysSequenceApi
 *
 * @author peijie.ye
 * @date 2022/12/10 2:16
 */
@ApiReference
public interface SysNumRuleApi {
    SysNumRuleDTO getRule(String numRuleCode);

    SequenceNo next(NumNextReq rule);

    void back(SequenceNo sequenceNo);
}
