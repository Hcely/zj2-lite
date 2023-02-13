package org.zj2.lite.codec;

import org.springframework.util.DigestUtils;

import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;

/**
 * CodecUtil
 *
 * @author peijie.ye
 * @date 2022/11/24 15:10
 */
public class CodecUtil {
    private static final int BUFFER_SIZE = 512;
    private static final SoftBufferThreadLocal BUFFERS = new SoftBufferThreadLocal();
    private static final char[] CHAR_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F'};

    public static CacheBuffer getBuffer() {
        return BUFFERS.getBuffer();
    }

    public static boolean isAllowFastMode(int dataLength) {
        return dataLength < ((BUFFER_SIZE - 32) * 3 / 4);
    }

    static StringBuilder fastEncrypt64(Crypto crypto, StringBuilder sb, byte[] valueData) {
        // 快速模式，使用缓存，减少对象新建
        CodecUtil.CacheBuffer buffer = CodecUtil.getBuffer();
        // 加密
        ByteArrayBuf buf = buffer.buf1.reset();
        crypto.encrypt(valueData, buf);
        // 64编码
        return Base64Util.ENCODER.encode(sb, buf.buffer(), 0, buf.writePos());
    }

    static String fastDecrypt64(Crypto crypto, CharSequence base64Value, int offset, int length) {
        // 快速模式，使用缓存，减少对象新建
        CodecUtil.CacheBuffer buffer = CodecUtil.getBuffer();
        // 64解码
        ByteArrayBuf buf1 = buffer.buf1.reset();
        int wrote = Base64Util.DECODER.decode(base64Value, offset, length, buf1.buffer(), buf1.writePos());
        buf1.addWritePos(wrote);
        // 解密
        ByteArrayBuf buf2 = buffer.buf2.reset();
        crypto.decrypt(buf1, buf2);
        return new String(buf2.buffer(), 0, buf2.writePos(), StandardCharsets.UTF_8);
    }

    public static byte[] getKeyByte16(String key) {
        byte[] keyData = key.getBytes(StandardCharsets.ISO_8859_1);
        if (keyData.length > 16) {
            return DigestUtils.md5Digest(keyData);
        } else {
            byte[] bytes = new byte[16];
            for (int i = 0, len = keyData.length; i < len; ++i) {
                bytes[i & 15] ^= keyData[i];
            }
            return bytes;
        }
    }

    public static String encodeHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) { return ""; }
        char[] charHex = CHAR_HEX;
        int len = bytes.length;
        char[] sb = new char[bytes.length << 1];
        for (int i = 0, n = 0; i < len; ++i, n += 2) {
            byte b = bytes[i];
            sb[n] = charHex[(b >>> 4) & 15];
            sb[n + 1] = charHex[b & 15];
        }
        return new String(sb);
    }

    public static StringBuilder encodeHex(StringBuilder sb, byte[] bytes) {
        if (bytes == null || bytes.length == 0) { return sb == null ? new StringBuilder() : sb; }
        char[] charHex = CHAR_HEX;
        if (sb == null) {
            sb = new StringBuilder(bytes.length << 1);
        } else {
            sb.ensureCapacity(sb.length() + bytes.length << 1);
        }
        for (byte b : bytes) {
            sb.append(charHex[(b >>> 4) & 15]);
            sb.append(charHex[b & 15]);
        }
        return sb;
    }

    public static String encodeHex(int v) {
        return encodeHex(null, v).toString();
    }

    public static StringBuilder encodeHex(StringBuilder sb, int v) {
        if (sb == null) {
            sb = new StringBuilder(8);
        } else {
            sb.ensureCapacity(sb.length() + 8);
        }
        sb.append(CHAR_HEX[(v >>> 28) & 15]);
        sb.append(CHAR_HEX[(v >>> 24) & 15]);
        sb.append(CHAR_HEX[(v >>> 20) & 15]);
        sb.append(CHAR_HEX[(v >>> 16) & 15]);
        sb.append(CHAR_HEX[(v >>> 12) & 15]);
        sb.append(CHAR_HEX[(v >>> 8) & 15]);
        sb.append(CHAR_HEX[(v >>> 4) & 15]);
        sb.append(CHAR_HEX[v & 15]);
        return sb;
    }

    public static String encodeHex(long v) {
        return encodeHex(null, v).toString();
    }

    public static StringBuilder encodeHex(StringBuilder sb, long v) {
        if (sb == null) {
            sb = new StringBuilder(16);
        } else {
            sb.ensureCapacity(sb.length() + 16);
        }
        sb.append(CHAR_HEX[(int) ((v >>> 60) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 56) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 52) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 48) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 44) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 40) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 36) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 32) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 28) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 24) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 20) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 16) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 12) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 8) & 15)]);
        sb.append(CHAR_HEX[(int) ((v >>> 4) & 15)]);
        sb.append(CHAR_HEX[(int) (v & 15)]);
        return sb;
    }

    private static class SoftBufferThreadLocal extends ThreadLocal<SoftReference<CacheBuffer>> {
        public CacheBuffer getBuffer() {
            SoftReference<CacheBuffer> bufferRef = get();
            CacheBuffer buffer;
            if (bufferRef == null || (buffer = bufferRef.get()) == null) {
                buffer = new CacheBuffer();
                set(new SoftReference<>(buffer));
            }
            return buffer;
        }
    }

    public static class CacheBuffer {
        public final ByteArrayBuf buf1 = new ByteArrayBuf(BUFFER_SIZE);
        public final ByteArrayBuf buf2 = new ByteArrayBuf(BUFFER_SIZE);
    }
}
