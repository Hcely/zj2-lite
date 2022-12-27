package org.zj2.lite.service.configure.logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.context.BaseContext;

/**
 *  RequestLogContext
 *
 * @author peijie.ye
 * @date 2022/12/27 22:07
 */
@Getter
@NoArgsConstructor
class RequestLogContext extends BaseContext {
    public static final int INITIALIZED = 0;
    public static final int REQUESTING = 1;
    public static final int RESPONSE = 2;
    private static final int IDX = nextIdx();

    static RequestLogContext setContext(String method, String uri) {
        RequestLogContext context = getSubContext(IDX, RequestLogContext::new);
        context.startTime = System.currentTimeMillis();
        context.method = method;
        context.uri = uri;
        context.logState = INITIALIZED;
        return context;
    }

    public static RequestLogContext current() {
        return getSubContext(IDX, RequestLogContext::new);
    }

    //
    private long startTime;
    private long endTime;
    //
    private String method;
    private String uri;
    @Setter
    private int logState;
    //
    private int responseStatus;
    private Object result;
    private Throwable error;

    public void setResponse(int responseStatus, Object result, Throwable error) {
        this.endTime = System.currentTimeMillis();
        this.responseStatus = responseStatus;
        this.result = result;
        this.error = error;
        this.logState = RESPONSE;
    }
}
