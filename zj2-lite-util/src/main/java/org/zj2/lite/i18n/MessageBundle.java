package org.zj2.lite.i18n;

import java.util.Locale;

/**
 * MessageProvider
 *
 * @author peijie.ye
 * @date 2023/2/1 23:36
 */
public interface MessageBundle {
    String get(String code, Locale locale);
}
