package org.zj2.lite.common.function;

@FunctionalInterface
public interface IdxKeyValueConsumer<K, V> {
    void accept(int idx, K key, V value);
}