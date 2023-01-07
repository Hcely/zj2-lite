package org.zj2.lite.codec;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *  Base64
 *
 * @author peijie.ye
 * @date 2022/12/5 1:19
 */
public class Base64Util {
    private static final char[] BASE64_ENCODE_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final char[] BASE64_URL_ENCODE_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

    private static final byte[] BASE64_DECODE_BYTES = new byte[128];

    static {
        byte i = 0;
        for (char c : BASE64_ENCODE_CHARS) { BASE64_DECODE_BYTES[c] = i++; }
        BASE64_DECODE_BYTES['-'] = 62;//url
        BASE64_DECODE_BYTES['_'] = 63;//url
    }

    public static final Encoder ENCODER = new Encoder(false, true);
    public static final Decoder DECODER = new Decoder();

    /**
     *  Base64Decoder
     *
     * @author peijie.ye
     * @date 2022/12/5 1:07
     */
    public static class Decoder {
        public byte[] decode(CharSequence base64Value) {
            return decode(base64Value, 0, StringUtils.length(base64Value));
        }

        public byte[] decode(CharSequence base64Value, int offset, int length) {
            if (length == 0 || (length = base64BufLength(base64Value, offset, length)) == 0) {
                return ArrayUtils.EMPTY_BYTE_ARRAY;
            }
            byte[] buf = new byte[base64BufCapacity(length)];
            decode0(base64Value, offset, length, buf, 0);
            return buf;
        }

        public int decode(CharSequence base64Value, int srcOffset, int srcLength, byte[] buf, int bufOffset) {
            if (srcLength == 0 || (srcLength = base64BufLength(base64Value, srcOffset, srcLength)) == 0) { return 0; }
            return decode0(base64Value, srcOffset, srcLength, buf, bufOffset);
        }

        int decode0(CharSequence base64Value, int srcOffset, int srcLength, byte[] buf, int bufOffset) {
            int b;
            final int srcBufOffset = bufOffset;
            byte[] decodeBytes = BASE64_DECODE_BYTES;
            for (srcLength += srcOffset; srcOffset < srcLength; ++srcOffset) {
                // 1
                b = decodeBytes[base64Value.charAt(srcOffset)] << 18;
                // 2
                if (++srcOffset < srcLength) {
                    b |= decodeBytes[base64Value.charAt(srcOffset)] << 12;
                } else {
                    buf[bufOffset++] = (byte) (b >>> 16);
                    return bufOffset - srcBufOffset;
                }
                // 3
                if (++srcOffset < srcLength) {
                    b |= decodeBytes[base64Value.charAt(srcOffset)] << 6;
                } else {
                    buf[bufOffset++] = (byte) (b >>> 16);
                    return bufOffset - srcBufOffset;
                }
                // 4
                if (++srcOffset < srcLength) {
                    b |= decodeBytes[base64Value.charAt(srcOffset)];
                } else {
                    buf[bufOffset++] = (byte) (b >>> 16);
                    buf[bufOffset++] = (byte) (b >>> 8);
                    return bufOffset - srcBufOffset;
                }
                buf[bufOffset++] = (byte) (b >>> 16);
                buf[bufOffset++] = (byte) (b >>> 8);
                buf[bufOffset++] = (byte) b;
            }
            return bufOffset - srcBufOffset;
        }

        private static int base64BufLength(CharSequence base64Value, int offset, int length) {
            for (; length > 0; --length) {
                if (base64Value.charAt(offset + length - 1) != '=') { break; }
            }
            return length;
        }

        private static int base64BufCapacity(int length) {
            int capacity = (length >> 2) * 3;
            int remain = length & 3;
            if (remain == 1 || remain == 2) {
                return capacity + 1;
            } else if (remain == 3) {
                return capacity + 2;
            } else {
                return capacity;
            }
        }
    }

    /**
     *  Base64Encoder
     *
     * @author peijie.ye
     * @date 2022/12/5 1:02
     */
    public static class Encoder {
        private final boolean urlEncode;
        private final boolean padding;

        public Encoder(boolean urlEncode, boolean padding) {
            this.urlEncode = urlEncode;
            this.padding = padding;
        }

        public String encode(byte[] buffer) {
            return encode(buffer, 0, buffer.length);
        }

        public String encode(byte[] buffer, int offset, int length) {
            return length == 0 ? StringUtils.EMPTY : encode(null, buffer, offset, length).toString();
        }

        public StringBuilder encode(StringBuilder sb, byte[] srcBuf) {
            return encode(sb, srcBuf, 0, srcBuf == null ? 0 : srcBuf.length);
        }

        public StringBuilder encode(StringBuilder sb, byte[] srcBuf, int srcOffset, int srcLength) {
            if (srcLength == 0) { return initSB(sb, 0); }
            int capacity = base64StrCapacity(srcLength);
            sb = initSB(sb, capacity);
            int b;
            final char[] encodeChars = urlEncode ? BASE64_URL_ENCODE_CHARS : BASE64_ENCODE_CHARS;
            for (srcLength += srcOffset; srcOffset < srcLength; ++srcOffset) {
                // 1
                b = (0xFF & srcBuf[srcOffset]) << 16;
                // 2
                if (++srcOffset < srcLength) {
                    b |= (0xFF & srcBuf[srcOffset]) << 8;
                } else {
                    sb.append(encodeChars[(b >>> 18) & 63]).append(encodeChars[(b >>> 12) & 63]);
                    if (padding) { sb.append('=').append('='); }
                    return sb;
                }
                // 3
                if (++srcOffset < srcLength) {
                    b |= 0xFF & srcBuf[srcOffset];
                } else {
                    sb.append(encodeChars[(b >>> 18) & 63]).append(encodeChars[(b >>> 12) & 63])
                            .append(encodeChars[(b >>> 6) & 63]);
                    if (padding) { sb.append('='); }
                    return sb;
                }
                sb.append(encodeChars[(b >>> 18) & 63]).append(encodeChars[(b >>> 12) & 63])
                        .append(encodeChars[((b >>> 6) & 63)]).append(encodeChars[(b & 63)]);
            }
            return sb;
        }

        private static StringBuilder initSB(StringBuilder sb, int capacity) {
            if (sb == null) {
                return new StringBuilder(capacity);
            } else {
                sb.ensureCapacity(sb.length() + capacity);
                return sb;
            }
        }

        private static int base64StrCapacity(int bufLength) {
            int capacity = bufLength / 3;
            if (capacity * 3 < bufLength) { ++capacity; }
            return capacity << 1;
        }

    }
}
