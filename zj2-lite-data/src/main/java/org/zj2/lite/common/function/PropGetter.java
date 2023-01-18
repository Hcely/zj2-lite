package org.zj2.lite.common.function;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface PropGetter<T, R> extends Function<T, R>, Serializable {
}