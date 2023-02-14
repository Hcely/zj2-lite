package org.zj2.lite.service.configure.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZJ2WebCorsProperties {
    private String allowHeaders = "*";
    private String allowMethods = "*";
    private String allowedOrigins = "*";
    private int maxAge = 1800;
}