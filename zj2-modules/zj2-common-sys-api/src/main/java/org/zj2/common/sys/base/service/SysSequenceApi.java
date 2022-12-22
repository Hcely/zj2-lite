package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.req.SequenceNextReq;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.lite.service.annotation.ApiReference;

/**
 *  SysSequenceApi
 *
 * @author peijie.ye
 * @date 2022/12/10 2:16
 */
@ApiReference
public interface SysSequenceApi {
    SequenceNo next(SequenceNextReq rule);

    void setSequenceNum(SequenceNextReq rule, long num);

    void back(SequenceNo sequenceNo);
}
