package org.zj2.lite.codec;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

import static org.zj2.lite.codec.CodecUtil.plus;

/**
 * Crypto
 *
 * @author peijie.ye
 * @date 2022/11/24 0:56
 */
public interface Crypto {
    default String encrypt64(String value) {
        if(StringUtils.isEmpty(value)) { return StringUtils.EMPTY; }
        byte[] valueData = value.getBytes(StandardCharsets.UTF_8);
        if(plus.isAllowFastMode(valueData.length)) {
            // 快速模式
            return plus.fastEncrypt64(this, null, valueData).toString();
        } else {
            return Base64Util.ENCODER.encode(encrypt(valueData, 0, valueData.length));
        }
    }

    default StringBuilder encrypt64(StringBuilder sb, String value) {
        if(StringUtils.isEmpty(value)) { return sb == null ? new StringBuilder() : sb; }
        byte[] valueData = value.getBytes(StandardCharsets.UTF_8);
        if(plus.isAllowFastMode(valueData.length)) {
            // 快速模式
            return plus.fastEncrypt64(this, sb, valueData);
        } else {
            return Base64Util.ENCODER.encode(sb, encrypt(valueData, 0, valueData.length));
        }
    }

    default String decrypt64(CharSequence base64Value) {
        return decrypt64(base64Value, 0, StringUtils.length(base64Value));
    }

    default String decrypt64(CharSequence base64Value, int offset, int length) {
        if(length == 0) { return StringUtils.EMPTY; }
        if(plus.isAllowFastMode(length)) {
            // 快速模式
            return plus.fastDecrypt64(this, base64Value, offset, length);
        } else {
            byte[] base64Data = Base64Util.DECODER.decode(base64Value, offset, length);
            return new String(decrypt(base64Data, 0, base64Data.length), StandardCharsets.UTF_8);
        }
    }

    int encrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff);

    int decrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff);

    byte[] encrypt(byte[] value, int offset, int length);

    byte[] decrypt(byte[] value, int offset, int length);
}
