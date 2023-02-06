package org.zj2.lite.service.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.context.ThreadContext;
import org.zj2.lite.common.context.ZContext;

/**
 * ServiceRequestConext
 *
 * @author peijie.ye
 * @date 2022/11/28 3:05
 */
@Setter
@Getter
@NoArgsConstructor
public class ServiceRequestContext extends ZContext {
    private static final ThreadContext<ServiceRequestContext> THREAD_CONTEXT = new ThreadContext<>();

    public static String currentAttrIp() {
        ServiceRequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getAttrIp();
    }

    public static String currentDevice() {
        ServiceRequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getDevice();
    }

    public static ServiceRequestContext set(ServiceRequestContext context) {
        return THREAD_CONTEXT.set(context);
    }

    public static ServiceRequestContext current() {
        return THREAD_CONTEXT.get(ServiceRequestContext::new);
    }

    private boolean filtered;
    private Object request;
    private String method;
    private String uri;
    private String attrIp;
    private String device;

    public String getMethod() {
        return method == null ? "" : method;
    }

    public String getUri() {
        return uri == null ? "" : uri;
    }

    public String getAttrIp() {
        return attrIp == null ? "" : attrIp;
    }

    public String getDevice() {
        return device == null ? "" : device;
    }

    @Override
    public ZContext clone() {//NOSONAR
        ServiceRequestContext context = (ServiceRequestContext) super.clone();
        context.request = null;
        return context;
    }
}
