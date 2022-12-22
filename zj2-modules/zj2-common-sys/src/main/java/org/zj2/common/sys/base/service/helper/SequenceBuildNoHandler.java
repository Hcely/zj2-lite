package org.zj2.common.sys.base.service.helper;

import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.SequenceNextContext;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  SequenceBuildNoHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceBuildNoHandler implements BizVHandler<SequenceNextContext> {
    @Override
    public void handle(SequenceNextContext context) {
        String seqNo = SequenceUtil.STR_FORMATTER_MANAGER.getFormatter(context.getSeqNoFormat())
                .formatObj(context.getParams());
        context.setSequenceNo(new SequenceNo(context.getSequenceRuleCode(), context.getSequenceKey(), seqNo));
    }
}
