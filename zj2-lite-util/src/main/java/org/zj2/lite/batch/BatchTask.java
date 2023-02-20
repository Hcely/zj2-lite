package org.zj2.lite.batch;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.IBuilder;
import org.zj2.lite.common.util.Concurrent;
import org.zj2.lite.util.stream.DataArrayStream;
import org.zj2.lite.util.stream.DataCollectionStream;
import org.zj2.lite.util.stream.DataFixedStream;
import org.zj2.lite.util.stream.DataOffsetReader;
import org.zj2.lite.util.stream.DataPageReader;
import org.zj2.lite.util.stream.DataStream;
import org.zj2.lite.util.AsyncUtil;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Consumer;

/**
 * BatchTask
 *
 * @author peijie.ye
 * @date 2023/2/17 16:19
 */
@SuppressWarnings("all")
public class BatchTask<T> extends AsyncUtil.AsyncCommand {
    private static final TaskListener NONE_LISTENER = new TaskListener() {
    };
    private static final AtomicIntegerFieldUpdater<BatchTask> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            BatchTask.class, "state");
    public static final int STATE_INIT = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FINISH = 10;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> Builder<T> builder(String taskName) {
        return new Builder<T>().taskName(taskName);
    }

    public static class Builder<T> implements IBuilder<BatchTask<T>> {
        private String taskId;
        private Logger logger;
        private String taskName;
        private int workerCount;
        private DataStream<T> stream;
        private Consumer<T> consumer;
        private Executor executor;
        private TaskListener<T> taskListener;

        protected Builder() {
        }


        public Builder<T> logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder<T> taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder<T> taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Builder<T> workerCount(int workerCount) {
            this.workerCount = workerCount;
            return this;
        }

        public Builder<T> stream(DataStream<T> stream) {
            this.stream = stream;
            return this;
        }

        @SafeVarargs
        public final Builder<T> stream(T... array) {
            return stream(new DataArrayStream<>(array));
        }

        public Builder<T> stream(Collection<T> coll) {
            return stream(new DataCollectionStream<>(coll));
        }

        public Builder<T> stream(DataPageReader.PageQuery<T> query, int pageSize) {
            return stream(new DataPageReader<>(query, pageSize).stream());
        }

        public <I extends Comparable> Builder<T> stream(Class<I> offsetType, DataOffsetReader.OffsetQuery<I, T> query,
                DataOffsetReader.OffsetGetter<I, T> offsetGetter, int pageSize) {
            return stream(new DataOffsetReader<>(query, offsetGetter, pageSize).stream());
        }

        public Builder<T> consumer(Consumer<T> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder<T> executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder<T> listener(TaskListener<T> taskListener) {
            this.taskListener = taskListener;
            return this;
        }

        @Override
        public BatchTask<T> build() {
            BatchTask<T> batchTask = new BatchTask<>();
            batchTask.setTaskId(taskId);
            batchTask.setLogger(logger);
            batchTask.setTaskName(taskName);
            batchTask.setWorkerCount(workerCount);
            batchTask.setExecutor(executor);
            batchTask.setStream(stream);
            batchTask.setConsumer(consumer);
            batchTask.setTaskListener(taskListener);
            return batchTask;
        }
    }

    private volatile int state;
    private Logger logger;
    private String taskId;
    private String taskName;
    private int taskSize;
    private int workerCount;
    private Executor executor;
    private DataStream<T> stream;
    private Consumer<T> consumer;
    private TaskListener<T> taskListener;
    //
    private long startTime;
    private long endTime;
    private AtomicInteger taskCount;
    private AtomicInteger endWorkerCount;
    private TaskWorker[] workers;

    protected BatchTask() {
        this.state = STATE_INIT;
    }

    protected void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    protected void setLogger(Logger logger) {
        this.logger = logger == null ? LoggerFactory.getLogger(BatchTask.class) : logger;
    }

    protected void setTaskName(String taskName) {
        this.taskName = StringUtils.defaultIfEmpty(taskName, "BatchTask");
    }

    protected void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    protected void setExecutor(Executor executor) {
        this.executor = executor;
    }

    protected void setStream(DataStream<T> stream) {
        this.stream = stream;
        if (stream instanceof DataFixedStream) {
            taskSize = ((DataFixedStream<T>) stream).size();
        } else {
            taskSize = -1;
        }
    }

    protected void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    protected void setTaskListener(TaskListener<T> taskListener) {
        this.taskListener = taskListener;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    protected void run0() {
        if (!STATE_UPDATER.compareAndSet(this, STATE_INIT, STATE_RUNNING)) { return; }
        final boolean sync = workerCount < 1;
        if (StringUtils.isEmpty(taskId)) { this.taskId = Long.toString(System.currentTimeMillis(), 36); }
        if (taskListener == null) { taskListener = NONE_LISTENER; }
        this.taskCount = new AtomicInteger(0);
        this.endWorkerCount = new AtomicInteger(0);
        this.workers = createWorkers(sync ? stream : createConcurrentStream(stream), Math.max(workerCount, 1));
        this.startTime = System.currentTimeMillis();
        //
        logger.info("[{}-{}]:start", taskName, taskId);
        taskListener.onStart(this);
        if (sync) {
            workers[0].run();
        } else {
            if (executor == null) {
                for (TaskWorker worker : workers) { AsyncUtil.execute(worker); }
            } else {
                for (TaskWorker worker : workers) { executor.execute(worker); }
            }
        }
    }

    private static <T> DataStream<T> createConcurrentStream(DataStream<T> stream) {
        return stream instanceof Concurrent ? stream : new TaskStreamWrapper<>(stream);
    }

    protected void onWorkerStart(TaskWorker worker) {
        logger.info("[{}-{}]:worker[{}] start", taskName, taskId, worker.workerIdx);
    }

    protected int onTaskExecuting(TaskWorker worker, T task) {
        int taskIdx = taskCount.getAndIncrement();
        logger.info("[{}-{}]:task[{}] executing", taskName, taskId, taskIdx);
        taskListener.onTaskExecuting(this, taskIdx, task);
        return taskIdx;
    }

    protected void onTaskExecuted(TaskWorker worker, int taskIdx, T task, long takeTime) {
        if (taskSize > 0) {
            logger.info("[{}-{}]:task[{}] executed({}ms), process:{}/{}", taskName, taskId, taskIdx, takeTime,
                    taskIdx + 1, taskSize);
        } else {
            logger.info("[{}-{}]:executed task[{}]({}ms)", taskName, taskId, taskIdx, takeTime);
        }
        taskListener.onTaskExecuted(this, taskIdx, task, takeTime);
    }

    protected void onTaskError(TaskWorker worker, int taskIdx, T task, Throwable error) {
        String message = "[" + taskName + "-" + taskId + "]:task[" + taskIdx + "] error";
        logger.error(message, error);
        taskListener.onTaskError(this, taskIdx, task, error);
    }

    protected void onWorkerError(TaskWorker worker, Throwable error) {
        String message = "[" + taskName + "-" + taskId + "]:worker[" + worker.workerIdx + "] error";
        logger.error(message, error);
    }

    protected void onWorkerFinish(TaskWorker worker, int taskCount, long takeTime) {
        final int endCount = endWorkerCount.incrementAndGet();
        logger.info("[{}-{}]:worker[{}] finish({}/{})", taskName, taskId, worker.workerIdx, endCount, workers.length);
        if (endCount >= workers.length && STATE_UPDATER.compareAndSet(this, STATE_RUNNING, STATE_FINISH)) {
            final long totalTakeTime = System.currentTimeMillis() - startTime;
            logger.info("[{}-{}]:finish({}ms)", taskName, taskId, totalTakeTime);
            taskListener.onEnd(this, this.taskCount.get(), totalTakeTime);
        }
    }

    protected TaskWorker[] createWorkers(DataStream<T> stream, int workerCount) {
        TaskWorker[] result = new TaskWorker[workerCount];
        for (int i = 0; i < workerCount; ++i) {
            result[i] = createWorker(i, stream, consumer);
        }
        return result;
    }

    protected TaskWorker createWorker(int workerIdx, DataStream<T> stream, Consumer<T> consumer) {
        return new TaskWorker(workerIdx, this, stream, consumer);
    }
}
