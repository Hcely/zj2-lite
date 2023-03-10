package org.zj2.lite.codec;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.CollUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * AbstractCrypto
 *
 * @author peijie.ye
 * @date 2023/2/23 23:56
 */
public abstract class AesCrypto implements Crypto {
    public static AesCrypto of128(AesMode aesMode) {
        return of128(aesMode, null);
    }

    public static AesCrypto of128(AesMode aesMode, CryptPadding padding) {
        return new Aes128Crypto(aesMode, padding);
    }

    public static AesCrypto of256(AesMode aesMode) {
        return of256(aesMode, null);
    }

    public static AesCrypto of256(AesMode aesMode, CryptPadding padding) {
        return new Aes256Crypto(aesMode, padding);
    }

    protected final int keyLen;
    protected final AesMode aesMode;
    protected final CryptPadding padding;

    protected AesCrypto(int keyLen, AesMode aesMode, CryptPadding padding) {
        this.keyLen = keyLen;
        this.aesMode = aesMode == null ? AesMode.CBC : aesMode;
        this.padding = padding == null ? CryptPadding.PKCSPadding : padding;
    }

    public AesCrypto init(String secret) {
        return init(secret, null);
    }

    public AesCrypto init(String secret, String iv) {
        byte[] secretBytes = StringUtils.isEmpty(secret) ? null : secret.getBytes(StandardCharsets.ISO_8859_1);
        byte[] ivBytes = StringUtils.isEmpty(iv) ? null : iv.getBytes(StandardCharsets.ISO_8859_1);
        return init(secretBytes, ivBytes);
    }

    public AesCrypto initBase64(String secretBase64) {
        return initBase64(secretBase64, null);
    }

    public AesCrypto initBase64(String secretBase64, String ivBase64) {
        byte[] secretBytes = Base64Util.DECODER.decode(secretBase64);
        byte[] ivBytes = Base64Util.DECODER.decode(ivBase64);
        return init(secretBytes, ivBytes);
    }

    public AesCrypto init(byte[] secret) {
        return init(secret, null);
    }

    public AesCrypto init(byte[] secret, byte[] iv) {
        secret = normalizeParams(secret, keyLen);
        iv = normalizeParams(iv, 16);
        init0(secret, iv);
        return this;
    }

    protected abstract void init0(byte[] secret, byte[] iv);

    static byte[] normalizeParams(byte[] key, int keyLen) {
        int len = CollUtil.size(key);
        if(len == 0) { return new byte[keyLen]; }
        return len == keyLen ? key : Arrays.copyOf(key, keyLen);
    }
}
