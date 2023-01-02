package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;

/**
 *  HidePropertyUtil
 *
 * @author peijie.ye
 * @date 2023/1/2 20:44
 */
public class HidePropertyUtil {
    public static Object hideProperty(String name, Object value) {
        if (!(value instanceof String)) {return null;}
        String valueStr = value.toString();
        if (StringUtils.containsAnyIgnoreCase(name, "mobile", "phone")) {
            // 隐藏手机号
            return hideMobile(valueStr);
        } else if (StringUtils.containsIgnoreCase(name, "email")) {
            // 隐藏邮箱
            return hideEmail(valueStr);
        } else {
            return "****";
        }
    }

    public static String hideMobile(String mobile) {
        return "****";
    }

    public static String hideEmail(String email) {
        return "****@***";
    }

}
