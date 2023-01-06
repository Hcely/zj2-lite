package org.zj2.common.uac.auth.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *  EmailHideShredder
 *
 * @author peijie.ye
 * @date 2023/1/7 3:00
 */
@Order(101)
@Component
public class EmailHideShredder implements PropertyHideShredder {
    @Override
    public boolean supports(String propertyName) {
        return StringUtils.containsIgnoreCase(propertyName, "email");
    }

    @Override
    public String hide(String value) {
        int idx = StringUtils.indexOf(value, '@');
        if (idx == -1) { return STAR_4_STR; }
        int valueLen = StringUtils.length(value);
        int nameLen = idx + 1;
        StringBuilder sb = new StringBuilder(64);
        if (nameLen <= 4) {
            sb.append(STAR_4_STR).append(value, idx, valueLen);
        } else {
            sb.append(value, 0, Math.min(3, nameLen - 4)).append(STAR_4_STR).append(value, idx, valueLen);
        }
        return sb.toString();
    }
}
