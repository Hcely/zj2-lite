package org.zj2.lite.net.promise;

/**
 * ResultPromise
 *
 * @author peijie.ye
 * @date 2023/3/5 2:33
 */
public class ResultPromise<T> extends AbstractPromise {
    protected Object result;
    protected Throwable error;

    public ResultPromise() {
    }

    public ResultPromise(Object flag) {
        super(flag);
    }

    public ResultPromise<T> success() {
        return success(null);
    }

    public ResultPromise<T> success(Object result) {
        if (tryCompleted(STATE_SUCCESS)) {
            this.result = result;
            completed();
        }
        return this;
    }

    public ResultPromise<T> failure() {
        return failure(null);
    }

    public ResultPromise<T> failure(Throwable error) {
        if (tryCompleted(STATE_FAILURE)) {
            this.error = error;
            completed();
        }
        return this;
    }

    public boolean isSuccess() {
        return getState() == STATE_SUCCESS;
    }

    public boolean isFailure() {
        return getState() == STATE_FAILURE;
    }

    public T result() {
        //noinspection unchecked
        return (T) result;
    }

    public Throwable error() {
        return error;
    }
}
