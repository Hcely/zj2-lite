package org.zj2.lite.batch;

/**
 * TaskListener
 *
 * @author peijie.ye
 * @date 2023/2/19 17:16
 */
public interface TaskListener<T> {
    default void onStart(BatchTask<T> batchTask) {
    }

    default void onTaskExecuting(BatchTask<T> batchTask, int taskIdx, T task) {
    }

    default void onTaskExecuted(BatchTask<T> batchTask, int taskIdx, T task, long takeTime) {
    }

    default void onTaskError(BatchTask<T> batchTask, int taskIdx, T task, Throwable error) {
    }

    default void onEnd(BatchTask<T> batchTask, int taskCount, long takeTime) {
    }

}
