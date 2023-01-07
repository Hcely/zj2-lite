package org.zj2.lite.codec;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 *  Crypto
 *
 * @author peijie.ye
 * @date 2022/11/24 0:56
 */
public interface Crypto {
    default String encrypt64(String value) {
        if (StringUtils.isEmpty(value)) { return StringUtils.EMPTY; }
        byte[] valueData = value.getBytes(StandardCharsets.UTF_8);
        if (CodecUtil.isAllowFastMode(valueData.length)) {
            // 快速模式
            return CodecUtil.fastEncrypt64(this, null, valueData).toString();
        } else {
            return Base64Util.ENCODER.encode(encrypt(valueData));
        }
    }

    default StringBuilder encrypt64(StringBuilder sb, String value) {
        if (StringUtils.isEmpty(value)) { return sb == null ? new StringBuilder() : sb; }
        byte[] valueData = value.getBytes(StandardCharsets.UTF_8);
        if (CodecUtil.isAllowFastMode(valueData.length)) {
            // 快速模式
            return CodecUtil.fastEncrypt64(this, sb, valueData);
        } else {
            return Base64Util.ENCODER.encode(sb, encrypt(valueData));
        }
    }

    default String decrypt64(CharSequence base64Value) {
        return decrypt64(base64Value, 0, StringUtils.length(base64Value));
    }

    default String decrypt64(CharSequence base64Value, int offset, int length) {
        if (length == 0) { return StringUtils.EMPTY; }
        if (CodecUtil.isAllowFastMode(length)) {
            // 快速模式
            return CodecUtil.fastDecrypt64(this, base64Value, offset, length);
        } else {
            byte[] base64Data = Base64Util.DECODER.decode(base64Value, offset, length);
            return new String(decrypt(base64Data), StandardCharsets.UTF_8);
        }
    }

    void encrypt(byte[] src, ByteArrayBuf dst);

    void decrypt(ByteArrayBuf src, ByteArrayBuf dst);

    byte[] encrypt(byte[] value, int offset, int length);

    byte[] decrypt(byte[] value, int offset, int length);

    default byte[] encrypt(byte[] value) {
        return encrypt(value, 0, value == null ? 0 : value.length);
    }

    default byte[] decrypt(byte[] value) {
        return decrypt(value, 0, value == null ? 0 : value.length);
    }
}
