package org.zj2.lite.common.context;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * IContext
 *
 * @author peijie.ye
 * @date 2022/12/8 18:04
 */
public class ThreadContext<T extends ZContext> {
    private static final AtomicInteger contextIdx = new AtomicInteger(0);
    private final int idx = contextIdx.getAndIncrement();

    public T get() {
        return ZContexts.getContext(idx, null);
    }

    public T get(Supplier<T> supplier) {
        return ZContexts.getContext(idx, supplier);
    }

    public T set(T context) {
        return ZContexts.setContext(idx, context);
    }

    public T clear() {
        return ZContexts.clearContext(idx);
    }
}
