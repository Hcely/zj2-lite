package org.zj2.lite.message;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.context.LocaleContext;
import org.zj2.lite.spring.SpringBeanRef;

import java.util.Locale;

/**
 * MessageUtil
 *
 * @author peijie.ye
 * @date 2023/2/2 17:45
 */
public class MessageUtil {
    private static final SpringBeanRef<MessageBundle> MESSAGE_BUNDLE_REF = new SpringBeanRef<>(MessageBundle.class);

    public static String get(String namespace, String code, Locale locale) {
        if (StringUtils.isEmpty(code)) { return ""; }
        MessageBundle messageBundle = MESSAGE_BUNDLE_REF.get();
        if (messageBundle == null) {
            return "";
        } else {
            try {
                String msg = messageBundle.get(namespace, code, locale);
                return msg == null ? "" : msg;
            } catch (Throwable ignored) {// NOSONAR
                return "";
            }
        }
    }

    public static String get(String namespace, String code) {
        return get(namespace, code, LocaleContext.currentLocale());
    }

}
