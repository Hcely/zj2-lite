package org.zj2.lite.codec;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * CipherCrypto
 *
 * @author peijie.ye
 * @date 2022/11/23 23:37
 */
public class Aes128Crypto extends AesCrypto {
    private Cipher encrypt;
    private Cipher decrypt;

    public Aes128Crypto() {
        this(null, null);
    }

    public Aes128Crypto(AesMode mode, CryptPadding padding) {
        super(16, mode, padding);
    }

    @Override
    @SneakyThrows
    protected void init0(byte[] secret, byte[] iv) {
        String transformation = getTransformation(aesMode, padding);
        //
        encrypt = Cipher.getInstance(transformation);
        decrypt = Cipher.getInstance(transformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "AES");
        IvParameterSpec parameterSpec = new IvParameterSpec(iv);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        decrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
    }

    protected static String getTransformation(AesMode aesMode, CryptPadding padding) {
        StringBuilder sb = new StringBuilder(24).append("AES/").append(aesMode).append('/' );
        if (padding == CryptPadding.PKCSPadding) {
            sb.append("PKCS5Padding");
        } else {
            sb.append("NoPadding");
        }
        return sb.toString();
    }

    @Override
    public int encrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        return doCrypt(encrypt, src, srcOff, srcLen, dst, dstOff);
    }

    @Override
    public int decrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        return doCrypt(decrypt, src, srcOff, srcLen, dst, dstOff);
    }

    @SneakyThrows
    @Override
    public byte[] encrypt(byte[] value, int offset, int length) {
        return doCrypt(encrypt, value, offset, length);
    }

    @SneakyThrows
    @Override
    public byte[] decrypt(byte[] value, int offset, int length) {
        return doCrypt(decrypt, value, offset, length);
    }

    @SneakyThrows
    protected int doCrypt(Cipher cipher, byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        if (srcLen == 0) { return 0; }
        return cipher.doFinal(src, srcOff, srcLen, dst, dstOff);
    }

    @SneakyThrows
    protected byte[] doCrypt(Cipher cipher, byte[] value, int offset, int length) {
        if (length == 0) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
        return cipher.doFinal(value, offset, length);
    }
}
