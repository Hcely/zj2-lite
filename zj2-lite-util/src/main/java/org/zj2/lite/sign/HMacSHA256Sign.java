package org.zj2.lite.sign;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.codec.CodecUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 *  HMacSHA256Sign
 *
 * @author peijie.ye
 * @date 2022/12/4 20:54
 */
public class HMacSHA256Sign implements DigestSign {
    public static final String ALGORITHM = "HmacSHA256";
    private final String secret;
    private final SecretKeySpec secretKey;

    public HMacSHA256Sign(String secret) {
        this.secret = secret;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String sign(String value) {
        if (StringUtils.isEmpty(value)) { return ""; }
        return CodecUtil.encodeHex(sign(secretKey, value));
    }

    public static String signStr(String secret, String value) {
        if (StringUtils.isEmpty(value)) { return ""; }
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        return CodecUtil.encodeHex(sign(secretKey, value));
    }

    public static byte[] signBytes(String secret, String value) {
        if (StringUtils.isEmpty(value)) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        return sign(secretKey, value);
    }

    private static byte[] sign(SecretKeySpec secretKey, String value) {
        try {
            Mac sha256 = Mac.getInstance(ALGORITHM);
            sha256.init(secretKey);
            return sha256.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    public static byte[] signISO(String secret, CharSequence value, int length) {
        try {
            Mac sha256 = Mac.getInstance(HMacSHA256Sign.ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.ISO_8859_1),
                    HMacSHA256Sign.ALGORITHM);
            sha256.init(secretKey);
            for (int i = 0; i < length; ++i) { sha256.update((byte) value.charAt(i)); }
            return sha256.doFinal();
        } catch (Exception e) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }
}

