package org.zj2.lite.helper;

import org.springframework.aop.support.AopUtils;
import org.zj2.lite.helper.handler.BizHandler;

/**
 * CommonBizHelper
 * <br>CreateDate 一月 11,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
@SuppressWarnings("all")
public abstract class CommonBizHelper<T> implements BizHandler<T> {
    private volatile BizHandler handler;

    private BizHandler getHandler() {
        BizHandler result = handler;
        if(result == null) {
            synchronized(this) {
                if((result = handler) == null) {
                    handler = result = BizReference.Helper.createHandler(AopUtils.getTargetClass(this));
                }
            }
        }
        return result;
    }

    @Override
    public boolean handle(T context) {
        return getHandler().handle(context);
    }
}
