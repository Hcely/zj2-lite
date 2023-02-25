package org.zj2.lite.codec;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.CollUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AesCrypto
 *
 * @author peijie.ye
 * @date 2023/2/24 23:38
 */
public class AesGCM {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    public static AesGCM of128(String secret) {
        return new AesGCM(secret, 16);
    }

    public static AesGCM of128(byte[] secret) {
        return new AesGCM(secret, 16);
    }

    public static AesGCM of256(String secret) {
        return new AesGCM(secret, 32);
    }

    public static AesGCM of256(byte[] secret) {
        return new AesGCM(secret, 32);
    }

    protected final SecretKeySpec secretKeySpec;


    public AesGCM(String secret, int keyLen) {
        this(StringUtils.isEmpty(secret) ? null : secret.getBytes(StandardCharsets.ISO_8859_1), keyLen);
    }

    public AesGCM(byte[] secret, int keyLen) {
        this.secretKeySpec = new SecretKeySpec(AesCrypto.normalizeParams(secret, keyLen), "AES");
    }

    public Crypto getCrypto(String nonce) {
        return getCrypto(nonce, null);
    }

    public Crypto getCrypto(String nonce, String aad) {
        byte[] nonceBytes = StringUtils.isEmpty(nonce) ? null : nonce.getBytes(StandardCharsets.ISO_8859_1);
        byte[] addBytes = StringUtils.isEmpty(aad) ? null : aad.getBytes(StandardCharsets.ISO_8859_1);
        return getCrypto(nonceBytes, addBytes);
    }

    public Crypto getCrypto(byte[] nonce) {
        return getCrypto(nonce, null);
    }

    public Crypto getCrypto(byte[] nonce, byte[] aad) {
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, AesCrypto.normalizeParams(nonce, 16));
        return new GCMCrypto(secretKeySpec, parameterSpec, aad);
    }

    public static class GCMCrypto implements Crypto {
        protected final SecretKeySpec secretKeySpec;
        protected final GCMParameterSpec nonce;
        protected final byte[] aad;

        public GCMCrypto(SecretKeySpec secretKeySpec, GCMParameterSpec nonce, byte[] aad) {
            this.secretKeySpec = secretKeySpec;
            this.nonce = nonce;
            this.aad = aad;
        }

        @Override
        public int encrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
            return doCrypt(true, src, srcOff, srcLen, dst, dstOff);
        }

        @Override
        public int decrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
            return doCrypt(false, src, srcOff, srcLen, dst, dstOff);
        }

        @Override
        public byte[] encrypt(byte[] value, int offset, int length) {
            return doCrypt(true, value, offset, length);
        }

        @Override
        public byte[] decrypt(byte[] value, int offset, int length) {
            return doCrypt(false, value, offset, length);
        }

        @SneakyThrows
        protected int doCrypt(boolean encrypt, byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
            if (srcLen == 0) { return 0; }
            return getCipher(encrypt).doFinal(src, srcOff, srcLen, dst, dstOff);
        }

        @SneakyThrows
        protected byte[] doCrypt(boolean encrypt, byte[] value, int offset, int length) {
            if (length == 0) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
            return getCipher(encrypt).doFinal(value, offset, length);
        }

        protected Cipher getCipher(boolean encrypt)
                throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
                InvalidKeyException {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKeySpec, nonce);
            if (CollUtil.isNotEmpty(aad)) { cipher.updateAAD(aad); }
            return cipher;
        }
    }
}
