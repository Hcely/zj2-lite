package org.zj2.lite.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * KeyNameParser
 *
 * @author peijie.ye
 * @date 2023/2/10 21:43
 */
public class KeyValueParser<R> {
    public static final char DEF_EQUAL_CHAR = '=';
    public static final char DEF_SEPARATOR_CHAR = '&';
    private static final int PARSE_STATE_START = 0;
    private static final int PARSE_STATE_KEY_ING = 1;
    private static final int PARSE_STATE_VALUE_ING = 3;
    private char equalChar;
    private char separatorChar;
    private Supplier<? extends R> resultSupplier;
    private KeyValueHandler<R> handler;

    public KeyValueParser() {
        this(null, null);
    }

    public KeyValueParser(Supplier<? extends R> resultSupplier, KeyValueHandler<R> handler) {
        this(DEF_EQUAL_CHAR, DEF_SEPARATOR_CHAR, resultSupplier, handler);
    }

    public KeyValueParser(char equalChar, char separatorChar) {
        this(equalChar, separatorChar, null, null);
    }

    public KeyValueParser(char separatorChar, Supplier<? extends R> resultSupplier, KeyValueHandler<R> handler) {
        this(DEF_EQUAL_CHAR, separatorChar, resultSupplier, handler);
    }

    public KeyValueParser(char equalChar, char separatorChar, Supplier<? extends R> resultSupplier,
            KeyValueHandler<R> handler) {
        this.equalChar = equalChar;
        this.separatorChar = separatorChar;
        this.resultSupplier = resultSupplier;
        this.handler = handler;
    }

    public void setEqualChar(char equalChar) {
        this.equalChar = equalChar;
    }

    public void setSeparatorChar(char separatorChar) {
        this.separatorChar = separatorChar;
    }

    public void setHandler(KeyValueHandler<R> handler) {
        this.handler = handler;
    }

    public void setResultSupplier(Supplier<? extends R> resultSupplier) {
        this.resultSupplier = resultSupplier;
    }

    public R parse(CharSequence value) {
        if (StringUtils.isEmpty(value)) { return null; }
        return parse0(value, 0, value.length());
    }

    public R parse(CharSequence value, int startIdx) {
        return parse(value, startIdx, value == null ? 0 : value.length());
    }

    public R parse(CharSequence value, int startIdx, int endIdx) {
        if (StringUtils.isEmpty(value) || startIdx < 0 || startIdx >= endIdx || endIdx > value.length()) {
            return null;
        }
        return parse0(value, startIdx, endIdx);
    }

    private R parse0(CharSequence value, int startIdx, int endIdx) {// NOSONAR
        final Supplier<? extends R> resultSupplier = this.resultSupplier;// NOSONAR
        final KeyValueHandler<R> handler = this.handler;// NOSONAR
        if (resultSupplier == null || handler == null) { return null; }
        final char equalChar = this.equalChar, separatorChar = this.separatorChar;// NOSONAR
        R result = resultSupplier.get();
        int parseState = PARSE_STATE_START;
        boolean startValue = false;
        int keyStartIdx = 0, keyEndIdx = 0;// NOSONAR
        int valueStartIdx = 0;
        char ch;
        for (int idx = startIdx; idx < endIdx; ++idx) {
            ch = value.charAt(idx);
            switch (parseState) {
                case PARSE_STATE_START: {
                    if (ch != separatorChar && ch != ' ') {
                        keyStartIdx = idx;
                        if (ch == equalChar) {
                            keyEndIdx = idx;
                            valueStartIdx = idx + 1;
                            startValue = false;
                            parseState = PARSE_STATE_VALUE_ING;
                        } else {
                            parseState = PARSE_STATE_KEY_ING;
                        }
                    }
                    break;
                }
                case PARSE_STATE_KEY_ING: {
                    if (ch == separatorChar) {
                        parseState = PARSE_STATE_START;
                        boolean ok = handleKeyValue(result, value, keyStartIdx, idx, idx, idx, handler);
                        if (!ok) { return null; }
                    } else if (ch == equalChar) {
                        keyEndIdx = idx;
                        valueStartIdx = idx + 1;
                        startValue = false;
                        parseState = PARSE_STATE_VALUE_ING;
                    }
                    break;
                }
                default: {
                    if (ch == separatorChar) {
                        parseState = PARSE_STATE_START;
                        boolean ok = handleKeyValue(result, value, keyStartIdx, keyEndIdx, valueStartIdx, idx, handler);
                        if (!ok) { return null; }
                    } else if (!startValue) {
                        startValue = ch != ' ';
                        valueStartIdx = idx;
                    }
                }
            }
        }
        if (parseState == PARSE_STATE_KEY_ING) {
            boolean ok = handleKeyValue(result, value, keyStartIdx, endIdx, endIdx, endIdx, handler);
            if (!ok) { return null; }
        } else if (parseState == PARSE_STATE_VALUE_ING) {
            boolean ok = handleKeyValue(result, value, keyStartIdx, keyEndIdx, valueStartIdx, endIdx, handler);
            if (!ok) { return null; }
        }
        return result;
    }

    private boolean handleKeyValue(R result, CharSequence value, int keyStartIdx, int keyEndIdx, int valueStartIdx,
            int valueEndIdx, KeyValueHandler<R> handler) {
        //
        keyEndIdx = normalizeEndIdx(value, keyStartIdx, keyEndIdx);
        valueEndIdx = normalizeEndIdx(value, valueStartIdx, valueEndIdx);
        if (valueStartIdx < valueEndIdx && value.charAt(valueStartIdx) == '"') {
            ++valueStartIdx;
            if (value.charAt(valueEndIdx - 1) == '"') { --valueEndIdx; }
            valueEndIdx = Math.max(valueStartIdx, valueEndIdx);
        }
        if (keyStartIdx < keyEndIdx || valueStartIdx < valueEndIdx) {
            return handler.handle(result, value, keyStartIdx, keyEndIdx, valueStartIdx, valueEndIdx);
        } else {
            return true;
        }
    }

    private static int normalizeEndIdx(CharSequence value, int startIdx, int endIdx) {
        while (startIdx < endIdx && value.charAt(endIdx - 1) == ' ') { --endIdx; }
        return endIdx;
    }

    public interface KeyValueHandler<R> {
        boolean handle(R result, CharSequence value, int keyStartIdx, int keyEndIdx, int valueStartIdx,
                int valueEndIdx);
    }
}
