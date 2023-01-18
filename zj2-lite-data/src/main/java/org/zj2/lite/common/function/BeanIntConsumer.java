package org.zj2.lite.common.function;

@FunctionalInterface
public interface BeanIntConsumer<V> {
    void accept(V e, int value);
}