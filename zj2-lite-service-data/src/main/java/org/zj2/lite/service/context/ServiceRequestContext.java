package org.zj2.lite.service.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.context.BaseContext;

/**
 *  ServiceRequestConext
 *
 * @author peijie.ye
 * @date 2022/11/28 3:05
 */
@Setter
@Getter
@NoArgsConstructor
public class ServiceRequestContext extends BaseContext {
    private static final int IDX = nextIdx();

    public static String currentAttrIp() {
        ServiceRequestContext requestContext = getSubContext(IDX, null);
        return requestContext == null ? "" : requestContext.getAttrIp();
    }

    public static String currentDevice() {
        ServiceRequestContext requestContext = getSubContext(IDX, null);
        return requestContext == null ? "" : requestContext.getDevice();
    }

    public static boolean currentAuthenticated() {
        ServiceRequestContext requestContext = getSubContext(IDX, null);
        return requestContext != null && requestContext.isAuthenticated();
    }

    public static ServiceRequestContext setContext(ServiceRequestContext context) {
        return setSubContext(IDX, context);
    }

    public static ServiceRequestContext current() {
        return getSubContext(IDX, ServiceRequestContext::new);
    }

    private Object request;
    private TokenType tokenType;
    private String token;
    private long tokenTime;
    private String namespace;
    private String method;
    private String uri;
    private boolean filtered;
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

    @Override
    public BaseContext clone() {//NOSONAR
        ServiceRequestContext context = (ServiceRequestContext) super.clone();
        context.request = null;
        return context;
    }
}
