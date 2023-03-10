package org.zj2.lite.service.configure.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.DateUtil;

import java.time.LocalDateTime;

/**
 * ZJDateConvertor
 *
 * @author peijie.ye
 * @date 2022/12/25 16:24
 */
@Component
public class ZJLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @SuppressWarnings("NullableProblems")
    @Override
    public LocalDateTime convert(String source) {
        return DateUtil.parse(source);
    }
}
