package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.text.SlotFormatPart;
import org.zj2.lite.common.text.StrFormatterManager;
import org.zj2.lite.common.text.ValueSerializer;

/**
 *  SequenceUtil
 *
 * @author peijie.ye
 * @date 2022/12/12 0:11
 */
public class SequenceUtil {
    private SequenceUtil() {
    }

    static final StrFormatterManager STR_FORMATTER_MANAGER = new StrFormatterManager();

    static {
        STR_FORMATTER_MANAGER.addValueSerializer(new NumberSerializer());
    }


    private static class NumberSerializer implements ValueSerializer {
        @Override
        public String serialize(SlotFormatPart part, Object value) {
            String format = part.getValueFormatter();
            if (StringUtils.startsWith(part.getKey(), "seq") && StringUtils.isNotEmpty(format)
                    && value instanceof Long) {
                return serializeSeq((Long) value, format);
            }
            return null;
        }
    }

    public static String serializeSeq(long value, String formatter) {
        if (StringUtils.isEmpty(formatter)) {
            return String.valueOf(value);
        } else {
            int bitSize = getFormatterBizSize(formatter);
            StringBuilder sb = new StringBuilder(bitSize);
            sb.setLength(bitSize);
            for (int idx = bitSize - 1; idx >= 0; --idx, value /= 10) {
                char ch = (char) ('0' + (value % 10));
                sb.setCharAt(idx, ch);
            }
            if (value > 0) {throw ZRBuilder.failureErr("系统自增序号超出最大值");}
            return sb.toString();
        }
    }

    public static int getFormatterBizSize(String formatter) {
        if (StringUtils.isEmpty(formatter)) {return 0;}
        int bitSize = 0;
        for (int i = 0, len = formatter.length(); i < len; ++i) {
            if (formatter.charAt(i) == '#') {++bitSize;}
        }
        return bitSize;
    }
}
