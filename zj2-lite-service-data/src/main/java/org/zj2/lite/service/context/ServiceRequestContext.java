package org.zj2.lite.service.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.context.BaseContext;
import org.zj2.lite.common.context.ZContext;

/**
 *  ServiceRequestConext
 *
 * @author peijie.ye
 * @date 2022/11/28 3:05
 */
@Setter
@NoArgsConstructor
public class ServiceRequestContext extends BaseContext {
    private static final int IDX = nextIdx();
    public static final String AUTHORIZATION = "Authorization";
    public static final String ATTR_IP = "attrIp";
    public static final String DEVICE = "device";

    public static String currentAttrIp() {
        ServiceRequestContext requestContext = ZContext.getSubContext(IDX, null);
        return requestContext == null ? "" : requestContext.getAttrIp();
    }

    public static String currentDevice() {
        ServiceRequestContext requestContext = ZContext.getSubContext(IDX, null);
        return requestContext == null ? "" : requestContext.getDevice();
    }

    public static boolean currentAuthenticated() {
        ServiceRequestContext requestContext = ZContext.getSubContext(IDX, null);
        return requestContext != null && requestContext.isAuthenticated();
    }

    public static ServiceRequestContext setContext(ServiceRequestContext context) {
        return ZContext.setSubContext(IDX, context);
    }

    public static ServiceRequestContext currentContext() {
        return ZContext.getSubContext(IDX, ServiceRequestContext::new);
    }

    @Getter
    private TokenType tokenType;
    private String token;
    @Getter
    private long tokenTime;
    @Getter
    private String namespace;
    private String method;
    private String uri;
    @Getter
    private boolean authenticated;
    //
    private String attrIp;
    private String device;

    public String getMethod() {
        return method == null ? "" : method;
    }

    public String getUri() {
        return uri == null ? "" : uri;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public String getAttrIp() {
        return attrIp == null ? "" : attrIp;
    }

    public String getDevice() {
        return device == null ? "" : device;
    }
}
