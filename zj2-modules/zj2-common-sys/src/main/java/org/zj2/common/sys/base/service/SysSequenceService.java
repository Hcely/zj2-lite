package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.SysSequenceDTO;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  SysConfigService
 *
 * @author peijie.ye
 * @date 2022/12/10 2:31
 */
public interface SysSequenceService extends BaseInnerService<SysSequenceDTO>, SysNumRuleApi {
    SysSequenceDTO getByKey(String sequenceKey);

    void setSequenceNum(NumNextReq rule, long num);

    boolean update(String sequenceKey, Long oldNum, Long newNum);

    String getBackSequenceNo(String sequenceKey);
}
