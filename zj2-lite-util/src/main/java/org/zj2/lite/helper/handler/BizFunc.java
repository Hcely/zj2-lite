package org.zj2.lite.helper.handler;


import org.zj2.lite.helper.entity.BizInterceptorError;

/**
 * 业务处理器标识
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
public interface BizFunc {// NOSONAR
    BizInterceptorError INTERCEPTOR = BizInterceptorError.INSTANCE;
}
