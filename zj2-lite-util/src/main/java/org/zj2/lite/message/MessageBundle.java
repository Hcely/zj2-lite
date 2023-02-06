package org.zj2.lite.message;

import java.util.Locale;

/**
 * MessageProvider
 *
 * @author peijie.ye
 * @date 2023/2/1 23:36
 */
public interface MessageBundle {
    String get(String namespace, String code, Locale locale);
}
