package org.zj2.lite.common.context;

import lombok.SneakyThrows;

import java.util.function.Supplier;

/**
 *  IContext
 *
 * @author peijie.ye
 * @date 2022/12/8 18:04
 */
public abstract class BaseContext implements Cloneable {
    protected static int nextIdx() {
        return ZContext.nextIdx();
    }

    protected static <T extends BaseContext> T getSubContext(int idx, Supplier<T> supplier) {
        return ZContext.getSubContext(idx, supplier);
    }

    protected static <T extends BaseContext> T setSubContext(int idx, T context) {
        return ZContext.setSubContext(idx, context);
    }

    protected static <T extends BaseContext> T clearSubContext(int idx) {
        return ZContext.clearSubContext(idx);
    }

    @Override
    @SneakyThrows
    public BaseContext clone() {//NOSONAR
        return (BaseContext) super.clone();
    }
}
