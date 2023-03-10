package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.common.sys.base.dto.SysSequenceDTO;
import org.zj2.common.sys.base.service.SysSequenceService;
import org.zj2.lite.helper.handler.BizHandler;
import org.zj2.lite.util.ZRBuilder;

/**
 * SequenceIncNumHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceIncNumHandler implements BizHandler<NumNextContext> {
    @Autowired
    private SysSequenceService sysSequenceService;

    @Override
    public boolean handle(NumNextContext context) {
        Long sequenceNum = nextSequenceNum(context);
        context.setSequenceNum(sequenceNum);
        context.putParam("sequence", sequenceNum);
        context.putParam("sequenceNum", sequenceNum);
        context.putParam("seq", sequenceNum);
        context.putParam("seqNum", sequenceNum);
        return true;
    }

    private Long nextSequenceNum(NumNextContext context) {
        String key = context.getSequenceKey();
        for(int i = 0; i < 10000; ++i) {
            SysSequenceDTO sequence = sysSequenceService.getByKey(key);
            if(sequence == null) {
                try {
                    sequence = new SysSequenceDTO();
                    sequence.setSequenceKey(key);
                    sequence.setSequenceRuleCode(context.getNumRuleCode());
                    sequence.setSequenceNum(1L);
                    sysSequenceService.add(sequence);
                    return 1L;
                } catch(Throwable ignore) {//NOSONAR
                }
            } else {
                Long oldValue = ObjectUtils.defaultIfNull(sequence.getSequenceNum(), 0L);
                Long newValue = oldValue + 1L;
                if(sysSequenceService.update(key, oldValue, newValue)) {
                    return newValue;
                }
            }
        }
        throw ZRBuilder.failureErr("序号服务自增异常");
    }

}
