package org.zj2.lite.i18n;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * AbstractMessageBundle
 *
 * @author peijie.ye
 * @date 2023/2/1 2:09
 */
@Component
public class BundleMessageSource extends AbstractMessageSource {


    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        return null;
    }
}
