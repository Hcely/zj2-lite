package org.zj2.lite.codec;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *  CipherCrypto
 *
 * @author peijie.ye
 * @date 2022/11/23 23:37
 */
public class AesCrypto implements Crypto {
    private final Cipher encrypt;
    private final Cipher decrypt;

    public AesCrypto() {
        this(null, null);
    }

    public AesCrypto(AesMode mode) {
        this(mode, null);
    }

    @SneakyThrows
    public AesCrypto(AesMode mode, CryptPadding padding) {
        //noinspection StringBufferReplaceableByString
        String transformation = new StringBuilder(32).append("AES/").append(mode == null ? AesMode.CBC : mode)
                .append('/').append(padding == null ? CryptPadding.PKCS5Padding : padding).toString();
        //
        encrypt = Cipher.getInstance(transformation);
        decrypt = Cipher.getInstance(transformation);
    }

    public AesCrypto init(String secret) {
        return init(secret, null);
    }

    @SneakyThrows
    public AesCrypto init(String secret, String iv) {
        byte[] secretBytes = CodecUtil.getKeyByte16(secret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(
                StringUtils.isEmpty(iv) ? secretBytes : CodecUtil.getKeyByte16(iv));
        encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        decrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return this;
    }

    @SneakyThrows
    @Override
    public void encrypt(byte[] src, ByteArrayBuf dst) {
        int wroteLen = encrypt.doFinal(src, 0, src.length, dst.buffer(), dst.writePos());
        dst.addWritePos(wroteLen);
    }

    @SneakyThrows
    @Override
    public void decrypt(ByteArrayBuf src, ByteArrayBuf dst) {
        int wroteLen = decrypt.doFinal(src.buffer(), 0, src.writePos(), dst.buffer(), dst.writePos());
        dst.addWritePos(wroteLen);
    }

    @SneakyThrows
    @Override
    public byte[] encrypt(byte[] value, int offset, int length) {
        if (length == 0) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
        return encrypt.doFinal(value, offset, length);
    }

    @SneakyThrows
    @Override
    public byte[] decrypt(byte[] value, int offset, int length) {
        if (length == 0) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
        return decrypt.doFinal(value, offset, length);
    }
}
