package org.zj2.common.uac.user.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.PatternUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.sign.Md5Sign;

import java.util.regex.Pattern;

/**
 * UserUtil
 *
 * @author peijie.ye
 * @date 2022/11/28 15:00
 */
public class UserUtil {
    private static final String PASSWORD_SUFFIX = "nW125ejfsaDUivaweFNw24efb";
    private static final Pattern ACCOUNT_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{2,20}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{2,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]{1,50}\\.[a-zA-Z0-9._-]+$");

    public static boolean isAccountName(String accountName) {
        return PatternUtil.test(ACCOUNT_NAME_PATTERN, accountName);
    }

    public static boolean isMobile(String mobile) {
        return PatternUtil.test(MOBILE_PATTERN, mobile);
    }

    public static boolean isEmail(String email) {
        return PatternUtil.test(EMAIL_PATTERN, email);
    }

    public static String buildPassword(String password) {
        return buildPassword(password, PASSWORD_SUFFIX);
    }

    public static String buildPassword(String password, String suffix) {
        if(StringUtils.isEmpty(password)) { return ""; }
        String value = StrUtil.concat(password, StringUtils.defaultIfEmpty(suffix, PASSWORD_SUFFIX));
        return Md5Sign.INSTANCE.sign(value);
    }
}
