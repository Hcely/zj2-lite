package org.zj2.lite.service.configure.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.DateUtil;

import java.time.LocalDate;

/**
 *  ZJDateConvertor
 *
 * @author peijie.ye
 * @date 2022/12/25 16:24
 */
@Component
public class ZJLocalDateConverter implements Converter<String, LocalDate> {
    @SuppressWarnings("NullableProblems")
    @Override
    public LocalDate convert(String source) {
        return DateUtil.parseAsLocalDate(source);
    }
}
