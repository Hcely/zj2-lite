package org.zj2.lite.common;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface PropFunc<T, R> extends Function<T, R>, Serializable {
}