package org.zj2.lite.net.promise;

/**
 * Promise
 *
 * @author peijie.ye
 * @date 2023/3/2 12:49
 */
public interface Promise {
    Promise flag(Object value);

    <T> T flag();

    boolean isCompleted();

    Promise sync();

    Promise sync(long timeout);

    Promise addListener(PromiseListener listener);
}
