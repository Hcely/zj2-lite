package org.zj2.lite.common.context;

import java.util.Locale;

/**
 * LocaleContext
 *
 * @author peijie.ye
 * @date 2023/2/2 17:55
 */
public class LocaleContext extends ZContext {
    private static final ThreadContext<LocaleContext> THREAD_CONTEXT = new ThreadContext<>();

    public static Locale currentLocale() {
        LocaleContext context = THREAD_CONTEXT.get();
        Locale locale = context == null ? null : context.locale;
        locale = locale == null ? Locale.getDefault() : locale;
        return locale == null ? Locale.CHINA : locale;
    }

    public static void setLocale(Locale locale) {
        if (locale == null) {
            LocaleContext context = THREAD_CONTEXT.get();
            if (context != null) { context.locale = null; }
        } else {
            THREAD_CONTEXT.get(LocaleContext::new).locale = locale;
        }
    }

    public static void clear() {
        THREAD_CONTEXT.clear();
    }

    private LocaleContext() {
    }

    private Locale locale;
}
