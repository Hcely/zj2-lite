package org.zj2.lite.common.function;

import java.util.Objects;

/**
 * ByteConsumer
 *
 * @author peijie.ye
 * @date 2023/2/13 9:55
 */
@FunctionalInterface
public interface ByteConsumer {
    void accept(byte b);

    default ByteConsumer andThen(ByteConsumer after) {
        Objects.requireNonNull(after);
        return (byte t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
