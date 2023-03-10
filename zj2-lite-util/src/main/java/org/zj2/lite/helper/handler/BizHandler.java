package org.zj2.lite.helper.handler;

/**
 * BizHandler
 * <br>CreateDate 一月 10,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
public interface BizHandler<C> extends BizFunc {
    @SuppressWarnings("rawtypes")
    BizHandler EMPTY_HANDLER = context -> true;

    boolean handle(C context);
}
