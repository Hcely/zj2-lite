package org.zj2.lite.service.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.zj2.lite.common.context.ThreadContext;
import org.zj2.lite.common.context.ZContext;
import org.zj2.lite.common.util.StrUtil;

/**
 * ServiceRequestConext
 *
 * @author peijie.ye
 * @date 2022/11/28 3:05
 */
@SuppressWarnings("all")
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
public abstract class RequestContext extends ZContext {
    private static final ThreadContext<RequestContext> THREAD_CONTEXT = new ThreadContext<>();

    public static String currentAttrIp() {
        RequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getAttrIp();
    }

    public static String currentDevice() {
        RequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getDevice();
    }

    public static String currentRootUri() {
        RequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getRootUri();
    }

    public static String currentUri() {
        RequestContext requestContext = THREAD_CONTEXT.get();
        return requestContext == null ? "" : requestContext.getUri();
    }

    public static RequestContext set(RequestContext context) {
        return THREAD_CONTEXT.set(context);
    }

    public static RequestContext current() {
        return THREAD_CONTEXT.get(BaseRequestContext::new);
    }

    protected String rootUri;
    protected String method;
    protected String uri;
    protected String attrIp;
    protected String device;

    public String getRootUri() {
        return StringUtils.isEmpty(rootUri) ? getUri() : rootUri;
    }

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

    public String getRequestParamStr(String key) {
        return StrUtil.toStrIfNull(getRequestParam(key));
    }

    @Nullable
    public abstract Object rawRequest();

    public abstract Object getRequestParam(String key);

    public abstract void setRequestParam(String key, Object param);

    @Override
    public ZContext copy() {
        return new BaseRequestContext(this);
    }
}
