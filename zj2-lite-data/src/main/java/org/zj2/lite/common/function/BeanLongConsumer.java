package org.zj2.lite.common.function;

@FunctionalInterface
public interface BeanLongConsumer<V> {
    void accept(V e, long value);
}