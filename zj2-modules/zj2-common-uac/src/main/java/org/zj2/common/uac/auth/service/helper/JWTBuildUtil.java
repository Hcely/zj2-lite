package org.zj2.common.uac.auth.service.helper;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.auth.dto.AuthenticationJWT;
import org.zj2.lite.codec.Base64Util;
import org.zj2.lite.sign.HMacSHA256Sign;

/**
 *  JWTUtil
 *
 * @author peijie.ye
 * @date 2022/12/4 14:32
 */
public class JWTBuildUtil {
    private static final int JWT_SIGN_LENGTH = 44;
    private static final int JWT_HEADER_LENGTH = 37;
    private static final String JWT_HEADER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";
    private static final Base64Util.Encoder URL_ENCODER = new Base64Util.Encoder(true, false);

    public static String getSignPart(String token) {
        int length = StringUtils.length(token);
        if (length >= JWT_SIGN_LENGTH + JWT_HEADER_LENGTH + 3) {
            return token.substring(length - JWT_SIGN_LENGTH + 1);
        } else {
            return "";
        }
    }

    public static String build(String secret, AuthenticationJWT token) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(JWT_HEADER);
        URL_ENCODER.encode(sb, JSON.toJSONBytes(token));
        byte[] signData = HMacSHA256Sign.signISO(secret, sb, sb.length());
        sb.append('.');
        return URL_ENCODER.encode(sb, signData).toString();
    }
}