package org.zj2.lite.codec;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.zj2.lite.common.util.CollUtil;

/**
 * Aes256Crypto
 *
 * @author peijie.ye
 * @date 2023/2/23 23:50
 */
public class Aes256Crypto extends AesCrypto {
    private AesCipher encrypt;
    private AesCipher decrypt;

    public Aes256Crypto(AesMode mode, CryptPadding padding) {
        super(32, mode, padding);
    }

    @Override
    protected void init0(byte[] secret, byte[] iv) {
        encrypt = new InnerPaddingCipher(aesMode, padding);
        decrypt = new InnerPaddingCipher(aesMode, padding);
        encrypt.init(true, buildParams(secret, iv));
        decrypt.init(false, buildParams(secret, iv));
    }

    @Override
    public int encrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        return doCrypt(encrypt, src, srcOff, srcLen, dst, dstOff);
    }

    @Override
    public int decrypt(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        return doCrypt(decrypt, src, srcOff, srcLen, dst, dstOff);
    }

    @Override
    public byte[] encrypt(byte[] value, int offset, int length) {
        return doCrypt(encrypt, value, offset, length);
    }

    @Override
    public byte[] decrypt(byte[] value, int offset, int length) {
        return doCrypt(decrypt, value, offset, length);
    }


    protected byte[] doCrypt(AesCipher cipher, byte[] src, int srcOff, int srcLen) {
        if(srcLen == 0) { return ArrayUtils.EMPTY_BYTE_ARRAY; }
        int len = cipher.getOutputSize(srcLen);
        byte[] dst = new byte[len];
        doCrypt(cipher, src, srcOff, srcLen, dst, 0);
        return dst;
    }

    @SneakyThrows
    protected int doCrypt(AesCipher cipher, byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff) {
        if(srcLen == 0) { return 0; }
        int len = cipher.update(src, srcOff, srcLen, dst, dstOff);
        int lastLen = cipher.doFinal(dst, dstOff + len);
        return len + lastLen;
    }


    protected CipherParameters buildParams(byte[] secret, byte[] iv) {
        if(CollUtil.isEmpty(iv)) {
            return new KeyParameter(secret);
        } else {
            return new ParametersWithIV(new KeyParameter(secret), iv);
        }

    }

    protected static class InnerPaddingCipher extends PaddedBufferedBlockCipher implements AesCipher {
        public InnerPaddingCipher(AesMode aesMode, CryptPadding padding) {
            super(buildCipher(aesMode), padding == CryptPadding.PKCSPadding ? new PKCS7Padding() : new ZeroBytePadding());
        }

        @Override
        public int update(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
            return processBytes(in, inOff, len, out, outOff);
        }

        protected static BlockCipher buildCipher(AesMode aesMode) {
            switch(aesMode) {
                case CFB:
                    return new CFBBlockCipher(new AESEngine(), 128);
                case OFB:
                    return new OFBBlockCipher(new AESEngine(), 128);
                default:
                    return new CBCBlockCipher(new AESEngine());
            }
        }
    }

    protected interface AesCipher {
        void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException;

        int getOutputSize(int len);

        int update(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException, IllegalStateException;

        int doFinal(byte[] out, int outOff) throws DataLengthException, IllegalStateException, InvalidCipherTextException;
    }
}
