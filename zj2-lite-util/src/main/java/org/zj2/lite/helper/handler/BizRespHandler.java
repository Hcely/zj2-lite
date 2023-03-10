package org.zj2.lite.helper.handler;

/**
 * BizHandler
 * <br>CreateDate 一月 10,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
public interface BizRespHandler<C, R> extends BizFunc {
    R handle(C context);
}
