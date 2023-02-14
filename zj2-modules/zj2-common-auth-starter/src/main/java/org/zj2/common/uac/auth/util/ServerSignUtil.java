package org.zj2.common.uac.auth.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.auth.AuthorizationServerSign;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.util.KeyValueParser;

import java.security.MessageDigest;

/**
 * ServerSignUtil
 *
 * @author peijie.ye
 * @date 2022/12/7 17:01
 */
@Component
public class ServerSignUtil {
    private static String serviceSecret;
    private static final String HEADER = "Digest ";
    private static final String ALGORITHM_MD5 = "MD5";
    //
    private static final String KEY_ALGORITHM = "algorithm";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NONCE = "nonce";
    private static final String KEY_RESPONSE = "response";
    private static final KeyValueParser<AuthorizationServerSign> PARSER = new KeyValueParser<>(',',
            AuthorizationServerSign::new, ServerSignUtil::handleNameValue);

    public ServerSignUtil(@Value("${zj2.service.secret:}") String serviceSecret) {
        ServerSignUtil.serviceSecret = serviceSecret;//NOSONAR
    }

    public static boolean isDigest(String token) {
        return StringUtils.length(token) > 30 && StringUtils.startsWithIgnoreCase(token, HEADER);
    }

    public static String buildAuthorization(AuthContext authContext, String method, String uri) {
        return buildAuthorization(authContext.getServiceName(), serviceSecret, method, uri);
    }

    public static String buildAuthorization(String serviceName, String serviceSecret, String method, String uri) {
        String nonce = Long.toString(System.currentTimeMillis(), 36);
        StringBuilder sb = new StringBuilder(192);
        sb.append(HEADER + KEY_ALGORITHM + "=" + ALGORITHM_MD5 + "," + KEY_USERNAME + "=\"").append(serviceName);
        sb.append("\"," + KEY_NONCE + "=\"").append(nonce);
        sb.append("\"," + KEY_RESPONSE + "=\"");
        CodecUtil.encodeHex(sb, buildSignBytes(serviceName, serviceSecret, nonce, method, uri));
        sb.append('"');
        return sb.toString();
    }

    public static AuthorizationServerSign parse(String authentication) {
        if (!isDigest(authentication)) { return null; }
        return PARSER.parse(authentication, HEADER.length(), authentication.length());
    }

    private static boolean handleNameValue(AuthorizationServerSign result, CharSequence sign, int nameStart,
            int nameEnd, int valueStart, int valueEnd) {
        if (StrUtil.equalsIgnoreCase(KEY_ALGORITHM, sign, nameStart, nameEnd)) {
            return StrUtil.equalsIgnoreCase(ALGORITHM_MD5, sign, valueStart, valueEnd);
        } else if (StrUtil.equalsIgnoreCase(KEY_USERNAME, sign, nameStart, nameEnd)) {
            result.setAppCode(StrUtil.substring(sign, valueStart, valueEnd));
        } else if (StrUtil.equalsIgnoreCase(KEY_NONCE, sign, nameStart, nameEnd)) {
            String nonce = StrUtil.substring(sign, valueStart, valueEnd);
            result.setTimestamp(Long.valueOf(nonce, 36));
        } else if (StrUtil.equalsIgnoreCase(KEY_RESPONSE, sign, nameStart, nameEnd)) {
            result.setSign(StrUtil.substring(sign, valueStart, valueEnd));
        }
        return true;
    }

    public static boolean valid(RequestContext requestContext, AuthContext authContext) {
        String sign = buildSign(authContext.getServiceName(), serviceSecret, authContext.getTokenTime(),
                requestContext.getMethod(), requestContext.getUri());
        return StringUtils.equalsIgnoreCase(sign, authContext.getToken());
    }

    public static String buildSign(String serviceName, String serviceSecret, long timestamp, String method,
            String uri) {
        return CodecUtil.encodeHex(
                buildSignBytes(serviceName, serviceSecret, Long.toString(timestamp, 36), method, uri));
    }

    @SneakyThrows
    private static byte[] buildSignBytes(String serviceName, String serviceSecret, String nonce, String method,
            String uri) {
        StringBuilder sb = new StringBuilder(96);
        MessageDigest md5Digest = MessageDigest.getInstance(ALGORITHM_MD5);
        appendPart1(sb, md5Digest, serviceName, serviceSecret);
        sb.append(':').append(nonce).append(':');
        appendPart2(sb, md5Digest, method, uri);
        return buildSign(md5Digest, sb);
    }

    private static void appendPart1(StringBuilder sb, MessageDigest md5Digest, String serviceName,
            String serviceSecret) {
        md5Digest.reset();
        putDigestData(md5Digest, serviceName);
        md5Digest.update((byte) ':');
        putDigestData(md5Digest, serviceSecret);
        CodecUtil.encodeHex(sb, md5Digest.digest());
    }

    private static void appendPart2(StringBuilder sb, MessageDigest md5Digest, String method, String uri) {
        md5Digest.reset();
        putDigestData(md5Digest, method);
        md5Digest.update((byte) ':');
        putDigestData(md5Digest, uri);
        CodecUtil.encodeHex(sb, md5Digest.digest());
    }

    private static byte[] buildSign(MessageDigest md5Digest, StringBuilder sb) {
        md5Digest.reset();
        putDigestData(md5Digest, sb);
        return md5Digest.digest();
    }

    private static void putDigestData(MessageDigest md5Digest, CharSequence value) {
        for (int i = 0, len = StringUtils.length(value); i < len; ++i) {
            md5Digest.update((byte) value.charAt(i));
        }
    }
}
