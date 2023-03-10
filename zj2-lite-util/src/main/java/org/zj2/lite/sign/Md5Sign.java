package org.zj2.lite.sign;

import org.springframework.util.DigestUtils;
import org.zj2.lite.codec.CodecUtil;

import java.nio.charset.StandardCharsets;

/**
 * Md5SignFunc
 *
 * @author peijie.ye
 * @date 2022/11/28 0:54
 */
public class Md5Sign implements DigestSign {//NOSONAR
    public static final Md5Sign INSTANCE = new Md5Sign();

    @Override
    public String sign(String s) {
        return CodecUtil.encodeHex(DigestUtils.md5Digest(s.getBytes(StandardCharsets.UTF_8)));
    }

    public String sign(byte[] bytes) {
        return CodecUtil.encodeHex(DigestUtils.md5Digest(bytes));
    }
}
