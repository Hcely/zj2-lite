package org.zj2.lite.batch;

import org.zj2.lite.util.stream.DataStream;
import org.zj2.lite.util.AsyncUtil;

import java.util.function.Consumer;

/**
 * TaskWorker
 *
 * @author peijie.ye
 * @date 2023/2/17 16:13
 */
@SuppressWarnings("all")
class TaskWorker extends AsyncUtil.AsyncCommand {
    protected final int workerIdx;
    protected final BatchTask batchTask;
    protected final DataStream stream;
    protected final Consumer consumer;
    protected int taskCount;


    TaskWorker(int workerIdx, BatchTask batchTask, DataStream stream, Consumer consumer) {
        this.workerIdx = workerIdx;
        this.batchTask = batchTask;
        this.stream = stream;
        this.consumer = consumer;
    }

    @Override
    protected void run0() {
        long startTime = System.currentTimeMillis();
        batchTask.onWorkerStart(this);
        try {
            executeTasks();
        } catch (Throwable e) {
            batchTask.onWorkerError(this, e);
        } finally {
            batchTask.onWorkerFinish(this, taskCount, System.currentTimeMillis() - startTime);
        }
    }

    protected void executeTasks() {
        final BatchTask batchTask = this.batchTask;
        final DataStream stream = this.stream;
        final Consumer consumer = this.consumer;
        Object task;
        while ((task = stream.next()) != null) {
            ++taskCount;
            long time = System.currentTimeMillis();
            int taskIdx = batchTask.onTaskExecuting(this, task);
            Throwable error = null;
            try {
                consumer.accept(task);
            } catch (Throwable e) {
                error = e;
            }
            time = System.currentTimeMillis() - time;
            if (error == null) {
                batchTask.onTaskExecuted(this, taskIdx, task, time);
            } else {
                batchTask.onTaskError(this, taskIdx, task, error);
            }
        }
    }
}
