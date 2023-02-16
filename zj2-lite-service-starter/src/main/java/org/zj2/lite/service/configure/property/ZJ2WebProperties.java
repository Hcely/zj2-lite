package org.zj2.lite.service.configure.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * ZJ2WebCorsProperties
 *
 * @author peijie.ye
 * @date 2023/2/14 18:22
 */
@Getter
@Setter
public class ZJ2WebProperties {
    private String index;
    @NestedConfigurationProperty
    private ZJ2WebCorsProperties cors = new ZJ2WebCorsProperties();
}
