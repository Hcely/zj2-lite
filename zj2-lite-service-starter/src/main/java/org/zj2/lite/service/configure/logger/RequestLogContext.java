package org.zj2.lite.service.configure.logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
    public static final int STATE_INITIALIZED = 0;
    public static final int STATE_REQUEST = 1;
    public static final int STATE_RESPONSE = 2;
    public static final int STATE_COMPLETED = 3;
    private static final int IDX = nextIdx();

    static RequestLogContext setContext(String method, String uri) {
        RequestLogContext context = getSubContext(IDX, RequestLogContext::new);
        context.startTime = System.currentTimeMillis();
        context.method = method;
        context.uri = uri;
        context.logState = STATE_INITIALIZED;
        return context;
    }

    public static RequestLogContext current() {
        return getSubContext(IDX, RequestLogContext::new);
    }

    //
    private long startTime;
    private long endTime;
    //
    private long executeStartTime;
    private long executeEndTime;
    //
    private String method;
    private String uri;
    private int logState;
    //
    private int responseStatus;
    private Object[] params;
    private Object result;
    private Throwable error;


    public boolean request() {
        if (logState == STATE_INITIALIZED) {
            logState = STATE_REQUEST;
            executeStartTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public void response(Object result, Object[] params, Throwable error) {
        this.logState = STATE_RESPONSE;
        this.executeEndTime = System.currentTimeMillis();
        this.params = params;
        this.result = result;
        this.error = error;
    }

    public void completed(int responseStatus) {
        this.logState = STATE_COMPLETED;
        this.responseStatus = responseStatus;
        this.endTime = System.currentTimeMillis();
    }
}
