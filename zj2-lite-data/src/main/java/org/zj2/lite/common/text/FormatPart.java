package org.zj2.lite.common.text;


import org.apache.commons.text.TextStringBuilder;

public interface FormatPart {
    void append( TextStringBuilder sb, Object[] args);

    void appendObj( TextStringBuilder sb, Object obj);
}