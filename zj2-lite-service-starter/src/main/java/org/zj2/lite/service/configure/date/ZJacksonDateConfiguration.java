package org.zj2.lite.service.configure.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zj2.lite.common.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *  ZJacksonConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/23 15:10
 */
@Configuration
public class ZJacksonDateConfiguration implements InitializingBean {
    @Autowired(required = false)
    private List<ObjectMapper> objectMappers;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (objectMappers == null) {return;}
        SimpleModule module = new SimpleModule().addDeserializer(Date.class, DateDeserializer.INSTANCE)
                .addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE)
                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        for (ObjectMapper mapper : objectMappers) {mapper.registerModule(module);}
    }

    private static class DateDeserializer extends JsonDeserializer<Date> {
        public static final DateDeserializer INSTANCE = new DateDeserializer();

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return DateUtil.parseAsDate(p.getText());
        }
    }

    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return DateUtil.parse(p.getText());
        }
    }

    private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return DateUtil.parseAsLocalDate(p.getText());
        }
    }

}
