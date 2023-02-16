package org.zj2.lite.service.configure.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * ZJ2SpringProperties
 *
 * @author peijie.ye
 * @date 2023/2/14 21:40
 */
@Getter
@Setter
@ConfigurationProperties("zj2")
public class ZJ2SpringProperties {
    @NestedConfigurationProperty
    private ZJ2WebProperties web = new ZJ2WebProperties();
    @NestedConfigurationProperty
    private ZJ2SwaggerDocProperties doc = new ZJ2SwaggerDocProperties();
    @NestedConfigurationProperty
    private ZJ2CryptProperties crypt = new ZJ2CryptProperties();
    @NestedConfigurationProperty
    private ZJ2LogProperties log = new ZJ2LogProperties();
}
