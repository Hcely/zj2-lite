package org.zj2.common.uac.auth.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.auth.AuthorizationServerSign;
import org.zj2.lite.service.constant.ServiceConstants;
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
    private static String serviceSecret = "5jMlBEV8kH1gXSpY3Ziw";
    private static final String HEADER = "Digest ";
    private static final String ALGORITHM_MD5 = "MD5";
    //
    private static final String KEY_ALGORITHM = "algorithm";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REALM = "realm";
    private static final String KEY_NONCE = "nonce";
    private static final String KEY_RESPONSE = "response";
    private static final KeyValueParser<AuthorizationServerSign> PARSER = new KeyValueParser<>(',',
            AuthorizationServerSign::new, ServerSignUtil::handleNameValue);

    public ServerSignUtil(@Value("${zj2.service.secret:}") String serviceSecret) {
        if (StringUtils.isNotEmpty(serviceSecret)) {
            setServiceSecret(serviceSecret);
        }
    }

    public static void setServiceSecret(String serviceSecret) {
        ServerSignUtil.serviceSecret = serviceSecret;//NOSONAR
    }

    public static boolean isDigest(String token) {
        return StringUtils.length(token) > 30 && StringUtils.startsWithIgnoreCase(token, HEADER);
    }

    public static String buildAuthorization(AuthContext authContext, String method, String uri) {
        String serviceName = ServiceConstants.serviceName();
        return buildAuthorization(serviceName, authContext.getAppCode(), authContext.getClientCode(),
                authContext.getRootService(), method, uri);
    }

    public static String buildAuthorization(String serviceName, String appCode, String clientCode, String rootService,
            String method, String uri) {
        serviceName = StringUtils.defaultString(serviceName);
        appCode = StringUtils.defaultString(appCode);
        clientCode = StringUtils.defaultString(clientCode);
        serviceName = StringUtils.defaultString(serviceName);
        rootService = StringUtils.defaultIfEmpty(rootService, serviceName);
        method = StringUtils.defaultString(method);
        uri = StringUtils.defaultString(uri);
        String nonce = Long.toString(System.currentTimeMillis(), 36);
        //
        StringBuilder sb = new StringBuilder(256);
        sb.append(HEADER + KEY_ALGORITHM + "=" + ALGORITHM_MD5);
        sb.append(',').append(KEY_USERNAME).append("=\"").append(serviceName).append('"');
        sb.append(',').append(KEY_NONCE).append("=\"").append(nonce).append('"');
        // realm = clientCode.appCode@rootService
        sb.append(',').append(KEY_REALM).append("=\"");
        if (StringUtils.isNotEmpty(clientCode) || StringUtils.isNotEmpty(appCode)) {
            sb.append(clientCode).append('.').append(appCode).append('@');
        }
        sb.append(rootService).append('"');
        //
        sb.append("," + KEY_RESPONSE + "=\"");
        CodecUtil.encodeHex(sb, buildResponseBytes(serviceName, appCode, clientCode, rootService, nonce, method, uri));
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
            result.setServiceName(StrUtil.substring(sign, valueStart, valueEnd));
        } else if (StrUtil.equalsIgnoreCase(KEY_REALM, sign, nameStart, nameEnd)) {
            handleRealm(result, sign, valueStart, valueEnd);
        } else if (StrUtil.equalsIgnoreCase(KEY_NONCE, sign, nameStart, nameEnd)) {
            String nonce = StrUtil.substring(sign, valueStart, valueEnd);
            result.setTimestamp(Long.valueOf(nonce, 36));
        } else if (StrUtil.equalsIgnoreCase(KEY_RESPONSE, sign, nameStart, nameEnd)) {
            result.setSign(StrUtil.substring(sign, valueStart, valueEnd));
        }
        return true;
    }

    private static void handleRealm(AuthorizationServerSign result, CharSequence sign, int valueStart, int valueEnd) {
        int idx = StrUtil.indexOf(sign, '.', valueStart, valueEnd);
        if (idx != -1) {
            result.setClientCode(StrUtil.substring(sign, valueStart, idx));
            valueStart = idx + 1;
        } else {
            result.setClientCode("");
        }
        idx = StrUtil.indexOf(sign, '@', valueStart, valueEnd);
        if (idx != -1) {
            result.setAppCode(StrUtil.substring(sign, valueStart, idx));
            valueStart = idx + 1;
        } else {
            result.setAppCode("");
        }
        result.setRootService(StrUtil.substring(sign, valueStart, valueEnd));
    }

    public static boolean valid(RequestContext requestContext, AuthContext authContext) {
        String response = CodecUtil.encodeHex(
                buildResponseBytes(authContext.getServiceName(), authContext.getAppCode(), authContext.getClientCode(),
                        authContext.getRootService(), Long.toString(authContext.getTokenTime(), 36),
                        requestContext.getMethod(), requestContext.getUri()));
        return StringUtils.equalsIgnoreCase(response, authContext.getToken());
    }

    public static boolean valid(AuthorizationServerSign serverSign, String method, String uri) {
        String sign = CodecUtil.encodeHex(
                buildResponseBytes(serverSign.getServiceName(), serverSign.getAppCode(), serverSign.getClientCode(),
                        serverSign.getRootService(), Long.toString(serverSign.getTimestamp(), 36), method, uri));
        return StringUtils.equalsIgnoreCase(sign, serverSign.getSign());
    }

    @SneakyThrows
    public static byte[] buildResponseBytes(String serviceName, String appCode, String clientCode, String rootService,
            String nonce, String method, String uri) {
        StringBuilder sb = new StringBuilder(96);
        MessageDigest md5Digest = MessageDigest.getInstance(ALGORITHM_MD5);
        appendPart1(sb, md5Digest, serviceName, serviceSecret, appCode, clientCode, rootService);
        sb.append(':').append(nonce).append(':');
        appendPart2(sb, md5Digest, method, uri);
        return buildSign(md5Digest, sb);
    }

    private static void appendPart1(StringBuilder sb, MessageDigest md5Digest, String serviceName, String serviceSecret,
            String appCode, String clientCode, String rootService) {
        md5Digest.reset();
        // username
        putDigestData(md5Digest, serviceName);
        //
        md5Digest.update((byte) ':');
        // realm = clientCode.appCode@rootService
        putDigestData(md5Digest, clientCode);
        md5Digest.update((byte) '.');
        putDigestData(md5Digest, appCode);
        md5Digest.update((byte) '@');
        putDigestData(md5Digest, rootService);
        //
        md5Digest.update((byte) ':');
        // password
        putDigestData(md5Digest, serviceSecret);
        //
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
