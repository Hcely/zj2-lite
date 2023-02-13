package org.zj2.lite.common.function;

@FunctionalInterface
public interface IntBeanConsumer<V> {
    void accept(int value, V e);
}