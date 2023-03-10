package org.zj2.common.uac.auth.server.hider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * MobileHideShredder
 *
 * @author peijie.ye
 * @date 2023/1/7 2:53
 */
@Order(100)
@Component
public class MobileValueHider implements PropertyValueHider {
    @Override
    public boolean supports(String propertyName) {
        return StringUtils.containsAnyIgnoreCase(propertyName, "mobile", "phone");
    }

    @Override
    public String hide(String value) {
        int len = StringUtils.length(value);
        if(len <= 4) { return STAR_4_STR; }
        StringBuilder sb = new StringBuilder(11);
        if(len <= 7) {
            sb.append(value, 0, len - 4).append(STAR_4_STR);
        } else {
            sb.append(value, 0, 3).append(STAR_4_STR).append(value, len - 4, len);
        }
        return sb.toString();
    }
}
