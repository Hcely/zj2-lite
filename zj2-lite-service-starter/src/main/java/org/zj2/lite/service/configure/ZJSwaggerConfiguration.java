package org.zj2.lite.service.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.zj2.lite.common.constant.ZJ2Constants;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.ApiDoc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

/**
 * ZJSwaggerConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/21 18:00
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "zj2.doc.enabled", havingValue = "true", matchIfMissing = true)
@EnableOpenApi
public class ZJSwaggerConfiguration implements EnvironmentAware, BeanFactoryPostProcessor {
    private String title;
    private String version;
    private Set<String> basePackages;

    @Bean
    public Docket defaultDocket() {
        // 总文档
        String docTitle = StringUtils.defaultIfEmpty(title, "ZJ2.0接口文档");
        String docVersion = StringUtils.defaultIfEmpty(version, "1.0");
        ApiInfo apiInfo = new ApiInfoBuilder().title(docTitle).version(docVersion).build();
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo).enable(true).select().apis(requestHandler -> {
            //noinspection deprecation
            Class<?> type = requestHandler.declaringClass();//NOSONAR
            String packageName = type.getPackageName();
            if (StringUtils.startsWith(packageName, ZJ2Constants.ZJ2_PACKAGE_PREFIX)) {
                return true;
            }
            return CollUtil.anyMatch(basePackages, e -> StringUtils.startsWith(packageName, e));
        }).paths(PathSelectors.any()).build();
    }

    @Override
    public void setEnvironment(Environment environment) {
        title = environment.getProperty("zj2.doc.title");
        version = environment.getProperty("zj2.doc.version");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        int i = 0;
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            if (isApiDoc(beanDefinition.getBeanClassName())) {
                ApiDoc apiDoc = (ApiDoc) beanFactory.getBean(name);
                String docGroupName = apiDoc.getGroupName();
                String docTitle = StringUtils.defaultIfEmpty(apiDoc.getTitle(), docGroupName);
                String docVersion = StringUtils.defaultIfEmpty(apiDoc.getVersion(), version);
                String basePackage = apiDoc.getBasePackage();
                addPackage(basePackage);
                beanFactory.registerSingleton(name + "$docket_" + i,
                        createDocket(docTitle, docVersion, docGroupName, basePackage));
                log.info("加载文档模块:{}", docGroupName);
                ++i;
            }
        }
    }

    private void addPackage(String basePackage) {
        if (basePackages == null) { basePackages = new HashSet<>(); }
        basePackages.add(basePackage);
    }

    protected static Docket createDocket(String title, String version, String group, String basePackages) {
        title = StringUtils.defaultIfEmpty(title, "ZJ2.0接口文档");
        version = StringUtils.defaultIfEmpty(version, "1.0");
        ApiInfo apiInfo = new ApiInfoBuilder().title(title).version(version).build();
        return new Docket(DocumentationType.OAS_30).groupName(group).apiInfo(apiInfo).enable(true).select()
                .apis(RequestHandlerSelectors.basePackage(basePackages)).paths(PathSelectors.any()).build();
    }

    private static boolean isApiDoc(String className) {
        if (!StringUtils.containsIgnoreCase(className, ApiDoc.class.getSimpleName())) { return false; }
        try {
            Class<?> type = Class.forName(className);
            return ApiDoc.class.isAssignableFrom(type);
        } catch (Throwable ignored) {//NOSONAR
        }
        return false;
    }
}
