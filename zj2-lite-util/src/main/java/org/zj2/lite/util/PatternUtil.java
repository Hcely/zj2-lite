package org.zj2.lite.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 *  PatternUtil
 *
 * @author peijie.ye
 * @date 2022/12/1 11:39
 */
public class PatternUtil {
    private static final Pattern WORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]+$");
    private static final Pattern INT_NUM_PATTERN = Pattern.compile("^\\d+$");

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
}
