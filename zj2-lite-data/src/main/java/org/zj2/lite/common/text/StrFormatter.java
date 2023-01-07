package org.zj2.lite.common.text;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.beans.BeanUtils;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.common.util.NumUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * StrFormatter，StringBuilder的内存优化对非英语地区没多大提升，因此改使用TextStringBuilder
 * <br>CreateDate 六月 30,2020
 * @author peijie.ye
 * @since 1.0
 */
public class StrFormatter {
    private static final FormatPart[] EMPTY_PARTS = {};
    private static final String SPECIAL_CHARS = "sdbtSDBT";

    private static FormatPart[] buildParts(StrFormatter strFormatter, String formatter) { //NOSONAR
        if (StringUtils.isBlank(formatter)) { return EMPTY_PARTS; }
        final int len = formatter.length();
        if (len == 1) { return EMPTY_PARTS; }
        List<FormatPart> parts = new LinkedList<>();
        int start = 0, argCount = 0, i = 0, safeLen = len - 1;//NOSONAR
        char ch, nextCh;//NOSONAR
        for (; i < safeLen; ++i) {
            if ((ch = formatter.charAt(i)) == '%') {
                if ((nextCh = formatter.charAt(i + 1)) == '%') {
                    ++i;
                    if (start < i) { parts.add(new ConstantFormatPart(formatter, start, i)); }
                    start = i + 1;
                } else if (SPECIAL_CHARS.indexOf(nextCh) != -1) {
                    if (start < i) { parts.add(new ConstantFormatPart(formatter, start, i)); }
                    ++i;
                    parts.add(new SlotFormatPart(strFormatter, argCount++));
                    start = i + 1;
                }
            } else if (ch == '{') {
                int end = formatter.indexOf('}', i + 1);
                if (end == -1) { break; }
                if (i + 1 == end) {
                    if (start < i) { parts.add(new ConstantFormatPart(formatter, start, i)); }
                    ++i;
                    parts.add(new SlotFormatPart(strFormatter, argCount++));
                    start = i + 1;
                } else {
                    if (start < i) { parts.add(new ConstantFormatPart(formatter, start, i)); }
                    parts.add(new SlotFormatPart(strFormatter, formatter, i + 1, end));
                    i = end;
                    start = i + 1;
                }
            }
        }
        if (start == 0) { return EMPTY_PARTS; }
        if (start < len) { parts.add(new ConstantFormatPart(formatter, start, len)); }
        return parts.toArray(EMPTY_PARTS);
    }

    private final StrFormatterManager formatterManager;
    private final String formatter;
    private final FormatPart[] parts;
    private final int minCapacity;
    private int initCapacity;

    public StrFormatter(String formatter) {
        this(null, formatter);
    }

    public StrFormatter(StrFormatterManager formatterManager, String formatter) {
        this.formatterManager = formatterManager == null ? StrFormatterManager.DEFAULT : formatterManager;
        this.formatter = formatter;
        this.parts = buildParts(this, formatter);
        this.minCapacity = StringUtils.length(formatter) + 32;
        this.initCapacity = minCapacity;
    }

    public FormatPart[] getParts() {
        return parts;
    }

    public String format(Object arg0) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), arg0));
    }

    public String format(Object arg0, Object arg1) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), arg0, arg1));
    }

    public String format(Object arg0, Object arg1, Object arg2) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), arg0, arg1, arg2));
    }

    public String format(Object arg0, Object arg1, Object arg2, Object arg3) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), arg0, arg1, arg2, arg3));
    }

    public String format(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), arg0, arg1, arg2, arg3, arg4));
    }

    public String format(Object... args) {
        if (parts.length == 0) { return formatter; }
        return toStr(appendArgsImpl(new TextStringBuilder(initCapacity), args));
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object arg0) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), arg0);
        }
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object arg0, Object arg1) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), arg0, arg1);
        }
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object arg0, Object arg1, Object arg2) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), arg0, arg1, arg2);
        }
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object arg0, Object arg1, Object arg2, Object arg3) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), arg0, arg1, arg2, arg3);
        }
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object arg0, Object arg1, Object arg2, Object arg3,
            Object arg4) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), arg0, arg1, arg2, arg3, arg4);
        }
    }

    public TextStringBuilder appendArgs(TextStringBuilder sb, Object... args) {
        if (parts.length == 0) {
            return initializeSb(sb).append(formatter);
        } else {
            return appendArgsImpl(initializeSb(sb), args);
        }
    }

    public String formatObj(Object obj) {
        if (parts.length == 0) { return formatter; }
        TextStringBuilder sb = new TextStringBuilder(initCapacity);
        for (FormatPart part : parts) { part.appendObj(sb, obj); }
        return toStr(sb);
    }

    public TextStringBuilder appendObj(TextStringBuilder sb, Object obj) {
        sb = initializeSb(sb);
        if (parts.length == 0) {
            return sb.append(formatter);
        } else {
            for (FormatPart part : parts) { part.appendObj(sb, obj); }
            return sb;
        }
    }

    private String toStr(TextStringBuilder sb) {
        int l = sb.length();
        if (l > minCapacity) {
            int oldCapacity = initCapacity;
            initCapacity = (oldCapacity >>> 1) + (l >>> 1);
        }
        return sb.toString();
    }

    private TextStringBuilder initializeSb(TextStringBuilder sb) {
        return sb == null ? new TextStringBuilder(initCapacity) : sb.ensureCapacity(sb.length() + initCapacity);
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object arg0) {
        for (FormatPart part : parts) {
            if (part.getClass() == SlotFormatPart.class) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if (slotPart.getIdx() == 0) {
                    slotPart.appendValue(sb, arg0);
                } else {
                    part.append(sb, null);
                }
            } else {
                part.append(sb, null);
            }
        }
        return sb;
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object arg0, Object arg1) {
        int argIdx;
        for (FormatPart part : parts) {
            if (part.getClass() == SlotFormatPart.class) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if ((argIdx = slotPart.getIdx()) == 0) {
                    slotPart.appendValue(sb, arg0);
                } else if (argIdx == 1) {
                    slotPart.appendValue(sb, arg1);
                } else {
                    part.append(sb, null);
                }
            } else {
                part.append(sb, null);
            }
        }
        return sb;
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object arg0, Object arg1, Object arg2) {
        int argIdx;
        for (FormatPart part : parts) {
            if (part.getClass() == SlotFormatPart.class) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if ((argIdx = slotPart.getIdx()) == 0) {
                    slotPart.appendValue(sb, arg0);
                } else if (argIdx == 1) {
                    slotPart.appendValue(sb, arg1);
                } else if (argIdx == 2) {
                    slotPart.appendValue(sb, arg2);
                } else {
                    part.append(sb, null);
                }
            } else {
                part.append(sb, null);
            }
        }
        return sb;
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object arg0, Object arg1, Object arg2, Object arg3) {
        int argIdx;
        for (FormatPart part : parts) {
            if (part.getClass() == SlotFormatPart.class) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if ((argIdx = slotPart.getIdx()) == 0) {
                    slotPart.appendValue(sb, arg0);
                } else if (argIdx == 1) {
                    slotPart.appendValue(sb, arg1);
                } else if (argIdx == 2) {
                    slotPart.appendValue(sb, arg2);
                } else if (argIdx == 3) {
                    slotPart.appendValue(sb, arg3);
                } else {
                    part.append(sb, null);
                }
            } else {
                part.append(sb, null);
            }
        }
        return sb;
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object arg0, Object arg1, Object arg2, Object arg3,
            Object arg4) {
        int argIdx;
        for (FormatPart part : parts) {
            if (part.getClass() == SlotFormatPart.class) {
                SlotFormatPart slotPart = (SlotFormatPart) part;
                if ((argIdx = slotPart.getIdx()) == 0) {
                    slotPart.appendValue(sb, arg0);
                } else if (argIdx == 1) {
                    slotPart.appendValue(sb, arg1);
                } else if (argIdx == 2) {
                    slotPart.appendValue(sb, arg2);
                } else if (argIdx == 3) {
                    slotPart.appendValue(sb, arg3);
                } else if (argIdx == 4) {
                    slotPart.appendValue(sb, arg4);
                } else {
                    part.append(sb, null);
                }
            } else {
                part.append(sb, null);
            }
        }
        return sb;
    }

    private TextStringBuilder appendArgsImpl(TextStringBuilder sb, Object... args) {
        for (FormatPart part : parts) { part.append(sb, args); }
        return sb;
    }

    protected void appendValue(TextStringBuilder sb, SlotFormatPart part, Object value) {
        if (value == null) {
            if (!formatterManager.isNullAsEmpty()) { sb.append("null"); }
        } else {
            try {// 严防错误
                String valueStr = serializeValue(part, value);
                if (valueStr != null) {
                    sb.append(valueStr);
                } else {
                    appendValue(true, sb, part, value); // 默认处理
                }
            } catch (Throwable ignored) { }//NOSONAR
        }
    }

    private String serializeValue(SlotFormatPart part, Object value) {
        CopyOnWriteArrayList<ValueSerializer> serializers = formatterManager.serializers;
        int len = serializers.size();
        if (len == 0) { return null; }
        String valueStr;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < len; ++i) {// 减少不必的对象产生
            ValueSerializer serializer = serializers.get(i);// CopyOnWriteArrayList 性能不会差
            if (serializer != null && (valueStr = serializer.serialize(part, value)) != null) {
                return valueStr;
            }
        }
        return null;
    }

    private static void appendValue(boolean root, TextStringBuilder sb, SlotFormatPart part, Object value) {
        if (value instanceof Date) {
            sb.append(DateUtil.format((Date) value, part.getValueFormatter()));
        } else if (value instanceof LocalDateTime) {
            sb.append(DateUtil.format((LocalDateTime) value, part.getValueFormatter()));
        } else if (value instanceof LocalDate) {
            sb.append(DateUtil.format((LocalDate) value, part.getValueFormatter()));
        } else if (value instanceof Number) {
            sb.append(NumUtil.toStr((Number) value));
        } else if (BeanUtils.isSimpleValueType(value.getClass())) {
            sb.append(value);
        } else if (root && value instanceof Iterable) {
            int i = 0;
            for (Object v : (Iterable<?>) value) {
                if (++i > 1) { sb.append(','); }
                appendValue(false, sb, part, v);
            }
        } else {
            sb.append(JSON.toJSONString(value));
        }
    }
}
