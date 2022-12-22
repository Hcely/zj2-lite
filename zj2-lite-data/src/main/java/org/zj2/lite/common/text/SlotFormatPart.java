package org.zj2.lite.common.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.util.PropertyUtil;

public final class SlotFormatPart implements FormatPart {
    private final StrFormatter strFormatter;
    private String rawArgKey;
    //
    private int valueIdx;
    private String valueKey;
    private String[] valuePathKeys;
    private String valueFormatter;

    SlotFormatPart(StrFormatter strFormatter, int argIdx) {
        this.strFormatter = strFormatter;
        this.valueIdx = argIdx;
        //
        this.valuePathKeys = NoneConstants.EMPTY_STRINGS;
        this.valueFormatter = "";
    }

    SlotFormatPart(StrFormatter strFormatter, String formatter, int start, int end) {
        this.strFormatter = strFormatter;
        this.rawArgKey = formatter.substring(start, end);
        this.valueIdx = -2;
    }

    @Override
    public void append(TextStringBuilder sb, Object[] args) {
        if (args == null || args.length == 0) {
            appendValue(sb, null);
        } else {
            int idx = getIdx();
            appendValue(sb, idx > -1 && idx < args.length ? args[idx] : null);
        }
    }

    @Override
    public void appendObj(TextStringBuilder sb, Object obj) {
        if (obj == null) {
            appendValue(sb, null);
        } else {
            String[] paths = loadValuePathKeys();
            Object value = paths == null || paths.length == 0 ?
                    PropertyUtil.getProperty(obj, getKey()) :
                    PropertyUtil.getProperty(obj, paths);
            appendValue(sb, value);
        }
    }

    void appendValue(TextStringBuilder sb, Object value) {
        strFormatter.appendValue(sb, this, value);
    }

    public String getKey() {
        String k = valueKey;
        if (k == null) {
            k = getKeyPart(loadArgKey());
            valueKey = k;
        }
        return k;
    }

    public int getIdx() {
        int idx = valueIdx;
        if (idx == -2) {
            try {
                idx = Integer.parseInt(getKeyPart(rawArgKey));
            } catch (Exception e) {idx = -1;}
            valueIdx = idx < 0 ? -1 : idx;
        }
        return idx;
    }

    public String getValueFormatter() {
        String formatter = valueFormatter;
        if (formatter == null) {
            formatter = getFormatterPart(loadArgKey());
            valueFormatter = formatter;
        }
        return formatter == null || formatter.isEmpty() ? null : formatter;
    }

    private String loadArgKey() {
        String key = rawArgKey;
        if (key == null) {
            int idx = valueIdx;
            key = idx < 0 ? "" : String.valueOf(idx);
            rawArgKey = key;
            valueKey = key;
            valuePathKeys = NoneConstants.EMPTY_STRINGS;
            valueFormatter = "";
        }
        return key;
    }

    private String[] loadValuePathKeys() {
        String[] paths = valuePathKeys;
        if (paths == null) {
            paths = PropertyUtil.getPathKeys(getKey());
            if (paths != null && paths.length == 1) {paths = NoneConstants.EMPTY_STRINGS;}
            valuePathKeys = paths;
        }
        return paths;
    }

    private static String getKeyPart(String key) {
        if (key == null) {return "";}
        int i = key.indexOf(',');
        return i == -1 ? StringUtils.trimToEmpty(key) : StringUtils.trimToEmpty(key.substring(0, i));
    }

    private static String getFormatterPart(String key) {
        if (key == null) {return "";}
        int i = key.indexOf(',');
        return i == -1 ? "" : StringUtils.trimToEmpty(key.substring(i + 1));
    }
}