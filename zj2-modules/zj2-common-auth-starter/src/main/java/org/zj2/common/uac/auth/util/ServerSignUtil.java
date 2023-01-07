package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.zj2.common.uac.auth.dto.AuthenticationSign;
import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.common.util.StrUtil;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *  ServerSignUtil
 *
 * @author peijie.ye
 * @date 2022/12/7 17:01
 */
public class ServerSignUtil {
    private static final String HEADER = "Digest ";
    private static final int PARSE_STATE_START = 0;
    private static final int PARSE_STATE_NAME = 1;
    private static final int PARSE_STATE_NAME_END = 2;
    private static final int PARSE_STATE_VALUE = 3;
    private static final String USERNAME = "username";
    private static final String NONCE = "nonce";
    private static final String RESPONSE = "response";
    private static final Map<String, String> RES_METHOD_MAP = new HashMap<>(512);

    private ServerSignUtil() {
    }

    public static boolean isDigest(String token) {
        return StringUtils.length(token) > 30 && StringUtils.startsWithIgnoreCase(token, HEADER);
    }

    public static String buildAuthentication(String appCode, String appSecret, String method, String uri) {
        String nonce = Long.toString(System.currentTimeMillis(), 36);
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(128);
        sb.append(HEADER + USERNAME + "=\"").append(appCode);
        sb.append("\"," + NONCE + "=\"").append(nonce);
        sb.append("\"," + RESPONSE + "=\"").append(buildSign(appCode, appSecret, nonce, method, uri));
        sb.append('"');
        return sb.toString();
    }

    public static AuthenticationSign parse(String sign) {//NOSONAR
        if (!isDigest(sign)) { return null; }
        AuthenticationSign result = new AuthenticationSign();
        int state = PARSE_STATE_START;
        int nameStart = 0, nameEnd = 0;// NOSONAR
        int valueStart = 0;
        final int len = sign.length();
        for (int i = HEADER.length(); i < len; ++i) {
            char ch = sign.charAt(i);
            if (state == PARSE_STATE_START) {
                if (ch != ' ') {
                    nameStart = i;
                    state = PARSE_STATE_NAME;
                }
            } else if (state == PARSE_STATE_NAME) {
                if (ch == '=') {
                    nameEnd = i;
                    state = PARSE_STATE_NAME_END;
                }
            } else if (state == PARSE_STATE_NAME_END) {// NOSONAR
                if (ch != ' ') {
                    valueStart = i;
                    state = PARSE_STATE_VALUE;
                }
            } else {// PARSE_STATE_VALUE
                if (ch == ',') {
                    state = PARSE_STATE_START;
                    handleNameValue(result, sign, nameStart, nameEnd, valueStart, i);
                }
            }
        }
        if (state == PARSE_STATE_VALUE) { handleNameValue(result, sign, nameStart, nameEnd, valueStart, len); }
        return result;
    }

    private static void handleNameValue(AuthenticationSign result, String sign, int nameStart, int nameEnd,
            int valueStart, int valueEnd) {
        while (sign.charAt(nameEnd - 1) == ' ') { --nameEnd; }
        while (sign.charAt(valueEnd - 1) == ' ') { --valueEnd; }
        if (sign.charAt(valueStart) == '"') {
            ++valueStart;
            if (sign.charAt(valueEnd - 1) == '"') { --valueEnd; }
        }
        if (valueStart >= valueEnd) { return; }
        if (StrUtil.equalsIgnoreCase(USERNAME, sign, nameStart, nameEnd)) {
            result.setAppCode(sign.substring(valueStart, valueEnd));
        } else if (StrUtil.equalsIgnoreCase(NONCE, sign, nameStart, nameEnd)) {
            String nonce = sign.substring(valueStart, valueEnd);
            result.setTimestamp(Long.valueOf(nonce, 36));
        } else if (StrUtil.equalsIgnoreCase(RESPONSE, sign, nameStart, nameEnd)) {
            result.setSign(sign.substring(valueStart, valueEnd));
        }
    }

    public static String getUri(Class<?> apiClass, Method method) {
        return getUri(null, apiClass, method.getName(), method.getParameterTypes());
    }

    public static String getUri(String prefix, Class<?> apiClass, Method method) {
        return getUri(prefix, apiClass, method.getName(), method.getParameterTypes());
    }

    public static String getUri(Class<?> apiClass, String methodName, Class<?>[] paramTypes) {
        return getUri(null, apiClass, methodName, paramTypes);
    }

    public static String getUri(String prefix, Class<?> apiClass, String methodName, Class<?>[] paramTypes) {
        String key = getMethodKey(prefix, apiClass, methodName, paramTypes);
        String result = RES_METHOD_MAP.get(key);
        if (result == null) {
            synchronized (RES_METHOD_MAP) {
                result = RES_METHOD_MAP.computeIfAbsent(key,
                        k -> buildMethodUri(prefix, apiClass, methodName, paramTypes));
            }
        }
        return result;
    }

    private static String buildMethodUri(String prefix, Class<?> apiClass, String methodName, Class<?>[] paramsTypes) {
        String className = apiClass.getName();
        int capacity = StringUtils.length(prefix) + className.length() + methodName.length() + 20;
        StringBuilder sb = new StringBuilder(capacity);
        if (StringUtils.isNotEmpty(prefix)) {
            if (prefix.charAt(0) != '/') { sb.append('/'); }
            sb.append(prefix);
        }
        if (StrUtil.lastChar(sb) != '/') { sb.append('/'); }
        sb.append(className).append('/').append(methodName).append('/');
        CodecUtil.encodeHex(sb, getParamHash(paramsTypes));
        return sb.toString();
    }

    private static String getMethodKey(String prefix, Class<?> apiClass, String methodName, Class<?>[] paramsTypes) {
        StringBuilder sb = new StringBuilder(40);
        if (StringUtils.isNotEmpty(prefix)) { CodecUtil.encodeHex(sb, prefix.hashCode()); }
        CodecUtil.encodeHex(sb, apiClass.hashCode());
        CodecUtil.encodeHex(sb, methodName.hashCode());
        CodecUtil.encodeHex(sb, getParamHash(paramsTypes));
        return sb.toString();
    }

    private static long getParamHash(Class<?>[] paramsTypes) {
        if (paramsTypes != null && paramsTypes.length > 0) {
            long result = 1;
            for (Class<?> e : paramsTypes) { result = 63 * result + e.getName().hashCode(); }
            return result;
        } else {
            return 0;
        }
    }

    public static String buildSign(String appCode, String appSecret, long timestamp, String method, String uri) {
        return buildSign(appCode, appSecret, Long.toString(timestamp, 36), method, uri);
    }

    private static String buildSign(String appCode, String appSecret, String nonce, String method, String uri) {
        StringBuilder sb = new StringBuilder(96);
        String part1 = StrUtil.concat(appCode, ":", appSecret);
        String part2 = StrUtil.concat(method, ":", uri);
        CodecUtil.encodeHex(sb, DigestUtils.md5Digest(part1.getBytes(StandardCharsets.ISO_8859_1)));
        sb.append(':').append(nonce).append(':');
        CodecUtil.encodeHex(sb, DigestUtils.md5Digest(part2.getBytes(StandardCharsets.ISO_8859_1)));
        return CodecUtil.encodeHex(DigestUtils.md5Digest(sb.toString().getBytes(StandardCharsets.ISO_8859_1)));
    }
}
