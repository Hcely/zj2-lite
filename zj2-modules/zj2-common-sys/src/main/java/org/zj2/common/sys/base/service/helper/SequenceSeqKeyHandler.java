package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.SequenceNextContext;
import org.zj2.lite.common.text.FormatPart;
import org.zj2.lite.common.text.SlotFormatPart;
import org.zj2.lite.common.text.StrFormatter;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  SequenceBakHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequenceSeqKeyHandler implements BizVHandler<SequenceNextContext> {
    @Override
    public void handle(SequenceNextContext context) {
        String keyFormatter = StringUtils.defaultIfEmpty(context.getSeqIncKeyFormat(), context.getSeqNoFormat());
        StrFormatter formatter = SequenceUtil.STR_FORMATTER_MANAGER.getFormatter(keyFormatter);
        TextStringBuilder sb = new TextStringBuilder(192);
        sb.append("SEQ:");// 前缀
        sb.append(context.getSequenceRuleCode()).append(':');//
        //
        String appCode = context.getAppCode();
        if (StringUtils.isNotEmpty(appCode)) {sb.append(appCode).append(':');}
        //
        for (FormatPart part : formatter.getParts()) {
            if (part instanceof SlotFormatPart) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if (StringUtils.startsWith(slotPart.getKey(), "seq")) {
                    slotPart.appendObj(sb, context.getParams());
                }
            }
        }
        context.setSequenceKey(StringUtils.upperCase(sb.toString()));
    }
}
