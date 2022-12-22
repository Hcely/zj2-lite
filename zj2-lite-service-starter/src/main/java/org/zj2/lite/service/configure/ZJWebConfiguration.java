package org.zj2.lite.service.configure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  CorConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/21 15:05
 */
@Configuration
public class ZJWebConfiguration implements WebMvcConfigurer {
    @Value("${zj2.web.cors.allowHeaders:*}")
    private String allowHeaders = "*";
    @Value("${zj2.web.cors.allowMethods:*}")
    private String allowMethods = "*";
    @Value("${zj2.web.cors.allowedOrigins:*}")
    private String allowedOrigins = "*";
    @Value("${zj2.web.cors.maxAge:1800}")
    private int maxAge = 1800;
    @Value("${zj2.web.index:}")
    private String indexPage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        if (StringUtils.isNotEmpty(indexPage)) {
            registry.addResourceHandler("/", "/index.html").addResourceLocations(indexPage);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders(buildParams(allowHeaders)).allowedMethods(buildParams(allowMethods))
                .maxAge(maxAge).allowedOrigins(buildParams(allowedOrigins));
    }

    private String[] buildParams(String value) {
        if (StringUtils.isEmpty(value) || StringUtils.equals("*", value)) {
            return new String[]{"*"};
        } else {
            String[] values = StringUtils.split(value, ',');
            for (int i = 0, len = values.length; i < len; ++i) {
                values[i] = StringUtils.trimToEmpty(values[i]);
            }
            return values;
        }
    }
}
