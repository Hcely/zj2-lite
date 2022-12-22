package org.zj2.lite.helper.chain;

public interface ChainCompletedListener<T> {
    void onCompleted(T context, Throwable error, boolean interrupted);
}