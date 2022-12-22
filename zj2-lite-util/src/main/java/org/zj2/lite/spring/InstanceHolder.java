package org.zj2.lite.spring;


import org.zj2.lite.common.constant.NoneConstants;

import java.util.function.Supplier;

/**
 *
 * <br>CreateDate 八月 19,2022
 * @author peijie.ye
 */
public class InstanceHolder<T> {
    private volatile Object instance;//NOSONAR
    private final Supplier<?> supplier;

    public InstanceHolder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public final T get() {
        Object result = instance;
        if (result == null) {
            synchronized (this) {
                if ((result = instance) == null) {
                    result = getImpl();
                    instance = result == null ? NoneConstants.NONE_OBJ : result;
                }
            }
        }
        return result == NoneConstants.NONE_OBJ ? null : (T) result;
    }

    protected Object getImpl() {
        return supplier == null ? null : supplier.get();
    }
}
