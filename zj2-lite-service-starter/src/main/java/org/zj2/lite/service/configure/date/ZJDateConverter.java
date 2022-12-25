package org.zj2.lite.service.configure.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.DateUtil;

import java.util.Date;

/**
 *  ZJDateConvertor
 *
 * @author peijie.ye
 * @date 2022/12/25 16:24
 */
@Component
public class ZJDateConverter implements Converter<String, Date> {
    @SuppressWarnings("NullableProblems")
    @Override
    public Date convert(String source) {
        return DateUtil.parseAsDate(source);
    }
}
