package org.zj2.lite.service.configure.property;

import lombok.Getter;
import lombok.Setter;

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
    private ZJ2WebCorsProperties cors = new ZJ2WebCorsProperties();
}
