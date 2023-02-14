package org.zj2.lite.service.configure;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.zj2.lite.service.configure.property.ZJ2SpringProperties;

/**
 * ZJConfiguration
 *
 * @author peijie.ye
 * @date 2023/2/14 18:29
 */
@Configuration
@ConfigurationPropertiesScan(basePackages = "org.zj2")
@EnableConfigurationProperties(ZJ2SpringProperties.class)
public class ZJConfiguration {
}
