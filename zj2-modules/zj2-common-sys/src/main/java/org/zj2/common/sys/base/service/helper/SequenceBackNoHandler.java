package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.service.SysSequenceService;
import org.zj2.lite.helper.handler.BizHandler;

/**
 * SequenceBakHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceBackNoHandler implements BizHandler<NumNextContext> {
    @Autowired
    private SysSequenceService sysSequenceService;

    @Override
    public boolean handle(NumNextContext context) {
        String sequenceNo = sysSequenceService.getBackSequenceNo(context.getSequenceKey());
        if(StringUtils.isNotEmpty(sequenceNo)) {
            context.setSequenceNo(new SequenceNo(context.getNumRuleCode(), context.getSequenceKey(), sequenceNo));
            return false;
        } else {
            return true;
        }
    }
}
