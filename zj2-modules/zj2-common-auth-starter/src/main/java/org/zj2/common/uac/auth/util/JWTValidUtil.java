package org.zj2.common.uac.auth.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.codec.Base64Util;
import org.zj2.lite.codec.ByteArrayBuf;
import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.service.auth.AuthorizationJWT;
import org.zj2.lite.sign.HMacSHA256Sign;
import org.zj2.lite.util.ZRBuilder;

import java.nio.charset.StandardCharsets;

/**
 * JWTUtil
 *
 * @author peijie.ye
 * @date 2022/12/4 14:32
 */
@Slf4j
public class JWTValidUtil {
    private static final int JWT_SIGN_LENGTH = 44;
    private static final int JWT_HEADER_LENGTH = 37;
    private static final int AUTH_HEADER_LENGTH = 44;
    private static final String JWT_HEADER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";
    private static final String AUTH_HEADER = "Bearer " + JWT_HEADER;
    private static final Base64Util.Encoder URL_ENCODER = new Base64Util.Encoder(true, false);

    public static boolean isJWT(String token) {
        int length = StringUtils.length(token);
        return length >= JWT_SIGN_LENGTH + JWT_HEADER_LENGTH + 3 && token.charAt(length - JWT_SIGN_LENGTH) == '.' && (
                token.startsWith(JWT_HEADER) || token.startsWith(AUTH_HEADER));
    }

    public static AuthorizationJWT parse(String token) {
        int length = StringUtils.length(token);
        if (length < JWT_SIGN_LENGTH + JWT_HEADER_LENGTH + 3) { return null; }
        if (token.charAt(length - JWT_SIGN_LENGTH) == '.') { return null; }
        int headerLength;
        if (token.startsWith(AUTH_HEADER)) {
            headerLength = AUTH_HEADER_LENGTH;
        } else if (token.startsWith(JWT_HEADER)) {
            headerLength = JWT_HEADER_LENGTH;
        } else { return null; }
        try {
            int payloadLength = length - JWT_SIGN_LENGTH - headerLength;
            if (CodecUtil.isAllowFastMode(payloadLength)) {
                ByteArrayBuf buf = CodecUtil.getBuffer().buf1.reset();
                int wroteLen = Base64Util.DECODER.decode(token, headerLength, payloadLength, buf.buffer(), 0);
                return JSON.parseObject(buf.buffer(), 0, wroteLen, StandardCharsets.UTF_8, AuthorizationJWT.class);
            } else {
                byte[] data = Base64Util.DECODER.decode(token, headerLength, payloadLength);
                return JSON.parseObject(data, 0, data.length, StandardCharsets.UTF_8, AuthorizationJWT.class);
            }
        } catch (Throwable e) {//NOSONAR
            log.error("解析token异常:" + token, e);
            throw ZRBuilder.failureErr("无效token格式");
        }
    }

    public static boolean valid(String secret, String token) {
        byte[] signData = HMacSHA256Sign.signISO(secret, token, token.length() - JWT_SIGN_LENGTH);
        return StringUtils.endsWithIgnoreCase(token, URL_ENCODER.encode(signData));
    }
}