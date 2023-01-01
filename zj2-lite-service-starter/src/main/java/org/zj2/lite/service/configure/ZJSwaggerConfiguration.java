package org.zj2.lite.service.configure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *  ZJSwaggerConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/21 18:00
 */
@Configuration
@ConditionalOnProperty(value = "zj2.doc.enabled", havingValue = "true", matchIfMissing = true)
@EnableOpenApi
public class ZJSwaggerConfiguration {
    @Value("${zj2.doc.title:}")
    private String docName = "";

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).enable(true).select()
                //apis： 添加swagger接口提取范围
                .apis(RequestHandlerSelectors.basePackage("org.zj2")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(StringUtils.defaultIfEmpty(docName, "ZJ2.0接口文档")).version("1.0").build();
    }
}
