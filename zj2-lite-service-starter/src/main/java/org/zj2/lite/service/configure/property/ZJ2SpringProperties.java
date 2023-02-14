package org.zj2.lite.service.configure.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ZJ2SpringProperties
 *
 * @author peijie.ye
 * @date 2023/2/14 21:40
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zj2")
public class ZJ2SpringProperties {
    private ZJ2WebProperties web =new ZJ2WebProperties();
    private ZJ2SwaggerDocProperties doc = new ZJ2SwaggerDocProperties();
    private ZJ2CryptProperties crypt = new ZJ2CryptProperties();
    private ZJ2LogProperties log = new ZJ2LogProperties();
}
