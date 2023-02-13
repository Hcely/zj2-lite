package org.zj2.lite.common.function;

@FunctionalInterface
public interface LongBeanConsumer<V> {
    void accept(long value, V e);
}