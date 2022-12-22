package org.zj2.lite.common.text;

import org.apache.commons.text.TextStringBuilder;

public final class ConstantFormatPart implements FormatPart {
    private final String formatter;
    private final int start;
    private final int length;

    ConstantFormatPart(String formatter, int start, int end) {
        this.formatter = formatter;
        this.start = start;
        this.length = end - start;
    }

    public void append(TextStringBuilder sb, Object[] args) {
        sb.append(formatter, start, length);
    }

    @Override
    public void appendObj(TextStringBuilder sb, Object obj) {
        sb.append(formatter, start, length);
    }
}