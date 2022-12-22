package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.SequenceNextContext;
import org.zj2.common.sys.base.dto.SysSequenceDTO;
import org.zj2.common.sys.base.service.SysSequenceService;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.helper.handler.BizHandler;
import org.zj2.lite.helper.handler.BizVHandler;

import java.util.Objects;

/**
 *  SequenceSetNumHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceSetNumHandler implements BizVHandler<SequenceNextContext> {
    @Autowired
    private SysSequenceService sysSequenceService;

    @Override
    public void handle(SequenceNextContext context) {
        String key = context.getSequenceKey();
        Long num = context.getSequenceNum();
        for (int i = 0; i < 5; ++i) {
            SysSequenceDTO sequence = sysSequenceService.getByKey(key);
            if (sequence == null) {
                try {
                    sequence = new SysSequenceDTO();
                    sequence.setSequenceKey(key);
                    sequence.setSequenceRuleCode(context.getSequenceRuleCode());
                    sequence.setSequenceNum(num);
                    sysSequenceService.add(sequence);
                    return;
                } catch (Throwable ignore) {//NOSONAR
                }
            } else {
                Long oldValue = ObjectUtils.defaultIfNull(sequence.getSequenceNum(), 0L);
                if (Objects.equals(oldValue, num) || sysSequenceService.update(key, oldValue, num)) {
                    return;
                }
            }
        }
        throw ZRBuilder.failureErr("序号服务设置异常");
    }

}
