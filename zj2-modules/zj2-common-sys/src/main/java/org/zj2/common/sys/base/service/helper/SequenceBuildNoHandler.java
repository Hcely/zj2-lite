package org.zj2.common.sys.base.service.helper;

import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 * SequenceBuildNoHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceBuildNoHandler implements BizVHandler<NumNextContext> {
    @Override
    public void handle(NumNextContext context) {
        String seqNo = SequenceUtil.STR_FORMATTER_MANAGER.getFormatter(context.getNumRuleFormat()).formatObj(context.getParams());
        context.setSequenceNo(new SequenceNo(context.getNumRuleCode(), context.getSequenceKey(), seqNo));
    }
}
