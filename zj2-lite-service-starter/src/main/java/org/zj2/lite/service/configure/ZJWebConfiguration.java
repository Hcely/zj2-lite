package org.zj2.lite.service.configure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zj2.lite.service.configure.property.ZJ2SpringProperties;
import org.zj2.lite.service.configure.property.ZJ2WebCorsProperties;
import org.zj2.lite.service.configure.property.ZJ2WebProperties;

/**
 * CorConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/21 15:05
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class ZJWebConfiguration implements WebMvcConfigurer {
    @Autowired
    private ZJ2SpringProperties properties;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        ZJ2WebProperties webProperties = properties.getWeb();
        if(StringUtils.isNotEmpty(webProperties.getIndex())) {
            registry.addResourceHandler("/", "/index.html").addResourceLocations(webProperties.getIndex());
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        ZJ2WebCorsProperties corsProperties = properties.getWeb().getCors();
        registry.addMapping("/**").allowedHeaders(buildParams(corsProperties.getAllowHeaders()))
                .allowedMethods(buildParams(corsProperties.getAllowMethods())).maxAge(corsProperties.getMaxAge())
                .allowedOrigins(buildParams(corsProperties.getAllowedOrigins()));
    }

    private String[] buildParams(String value) {
        if(StringUtils.isEmpty(value) || StringUtils.equals("*", value)) {
            return new String[]{"*"};
        } else {
            String[] values = StringUtils.split(value, ',' );
            for(int i = 0, len = values.length; i < len; ++i) {
                values[i] = StringUtils.trimToEmpty(values[i]);
            }
            return values;
        }
    }
}
