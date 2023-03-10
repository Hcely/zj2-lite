package org.zj2.lite.helper.entity;

/**
 * <br>CreateDate 四月 03,2022
 *
 * @author peijie.ye
 */
public final class BizInterceptorError extends RuntimeException {
    private static final long serialVersionUID = 20220403095906L;
    public static final BizInterceptorError INSTANCE = new BizInterceptorError();

    private BizInterceptorError() {
        setStackTrace(new StackTraceElement[0]);
    }
}
