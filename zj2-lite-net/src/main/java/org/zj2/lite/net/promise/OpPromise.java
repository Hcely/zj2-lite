package org.zj2.lite.net.promise;

/**
 * OpPromiseImpl
 *
 * @author peijie.ye
 * @date 2023/3/5 0:50
 */

public class OpPromise extends AbstractPromise {
    public OpPromise() {
    }

    public OpPromise(Object flag) {
        super(flag);
    }

    public OpPromise finish() {
        if (tryCompleted(STATE_SUCCESS)) { completed(); }
        return this;
    }
}
