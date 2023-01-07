package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.regex.Pattern;

/**
 *  PatternUtil
 *
 * @author peijie.ye
 * @date 2022/12/1 11:39
 */
public class PatternUtil {
    private static final Pattern WORD_PATTERN = Pattern.compile("^[0-9a-zA-Z_-]+$");
    private static final Pattern ASCII_PATTERN = Pattern.compile("^[\\x00-\\x7F]+$");
    private static final Pattern INT_NUM_PATTERN = Pattern.compile("^\\d+$");
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    static {
        pathMatcher.setCaseSensitive(true);
        pathMatcher.setCachePatterns(true);
    }

    public static boolean isAscii(String value) {
        return test(ASCII_PATTERN, value);
    }

    public static boolean isWord(String value) {
        return test(WORD_PATTERN, value);
    }

    public static boolean isIntNum(String value) {
        return test(INT_NUM_PATTERN, value);
    }

    public static boolean test(String pattern, String value) {
        if (StringUtils.isEmpty(pattern) || StringUtils.isEmpty(value)) {
            return false;
        }
        return Pattern.matches(pattern, value);
    }

    public static boolean test(Pattern pattern, String value) {
        if (pattern == null || StringUtils.isEmpty(value)) {
            return false;
        }
        return pattern.matcher(value).matches();
    }

    public static boolean matchPath(String pattern, String path) {
        if (StringUtils.isEmpty(pattern)) { return false; }
        return pathMatcher.match(pattern, path);
    }
}
