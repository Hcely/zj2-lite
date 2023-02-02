package org.zj2.lite.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zj2.lite.spring.SpringBeanRef;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TransactionSyncUtil 事务同步执行工具
 * <br>CreateDate 一月 16,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
@SuppressWarnings("all")
public class TransactionSyncUtil {
    private static final Logger log = LoggerFactory.getLogger(TransactionSyncUtil.class);
    //
    private static final SpringBeanRef<TransactionExecutor> TRANSACTION_EXECUTOR_REF = new SpringBeanRef<>(
            TransactionExecutor.class);
    private static final ThreadLocal<AtomicInteger> IDX_TL = ThreadLocal.withInitial(AtomicInteger::new);
    // 防止被嵌套调用
    private static final ThreadLocal<Boolean> TRANS_STATE_TL = new ThreadLocal<>();
    //
    private static final ThreadLocal<Set> synchronizations;

    static {
        ThreadLocal<Set> tmp;
        try {
            tmp = (ThreadLocal<Set>) FieldUtils.readStaticField(TransactionSynchronizationManager.class,
                    "synchronizations", true);
        } catch (Throwable e) {
            tmp = null;
        }
        synchronizations = tmp;
    }

    public static TransactionExecutor transExecutor() {
        return TRANSACTION_EXECUTOR_REF.get();
    }

    /**
     * 事务提交后执行
     *
     * @param cmd
     */
    public static void afterCommit(Runnable cmd) {
        transSync(cmd, null, null, true);
    }

    /**
     * 事务提交后执行，作为优化调用编码的备选方式
     *
     * @param cmd
     */
    public static <T> void afterCommit(T value, Consumer<T> consumer) {
        transSync(null, value, consumer, true);
    }

    /**
     * 事务回滚后执行
     *
     * @param cmd
     */
    public static void afterRollback(Runnable cmd) {
        transSync(cmd, null, null, false);
    }

    /**
     * 事务回滚后执行，作为优化调用编码的备选方式
     *
     * @param cmd
     */
    public static <T> void afterRollback(T value, Consumer<T> consumer) {
        transSync(null, value, consumer, false);
    }

    private static <T> void transSync(Runnable cmd, T value, Consumer<T> consumer, boolean afterCommit) {
        Boolean committedflag = TRANS_STATE_TL.get();
        if (committedflag != null) {
            if (committedflag == afterCommit) { executeCmd(cmd, value, consumer); }
        } else if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 本地事务或全局事务起始
            TransactionSynchronizationManager.registerSynchronization(
                    new AfterCommitRunner(getSyncIdx(), cmd, value, consumer, afterCommit));
        } else {
            if (afterCommit) { executeCmd(cmd, value, consumer); }
        }
    }

    /**
     * 是否存在事务
     *
     * @param cmd
     */
    public static boolean isTransactionActive() {
        return TRANS_STATE_TL.get() == null && TransactionSynchronizationManager.isActualTransactionActive();
    }

    private static void executeCmd(Runnable cmd, Object value, Consumer consumer) {
        if (cmd != null) {
            cmd.run();
        } else if (consumer != null) {
            consumer.accept(value);
        }
    }

    public static class AfterCommitRunner implements TransactionSynchronization {
        private final int idx;
        private final Runnable cmd;
        private final Object value;
        private final Consumer consumer;
        private final boolean afterCommit;


        private AfterCommitRunner(int idx, Runnable cmd, Object value, Consumer consumer, boolean afterCommit) {
            this.idx = idx;
            this.cmd = cmd;
            this.value = value;
            this.consumer = consumer;
            this.afterCommit = afterCommit;
        }

        @Override
        public int getOrder() {
            return idx;
        }

        @Override
        public void afterCommit() {
            if (afterCommit) { doExecute(Boolean.TRUE); }
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK && !afterCommit) { doExecute(Boolean.FALSE); }
        }

        private void doExecute(Boolean commited) {
            try {
                TRANS_STATE_TL.set(commited);
                executeCmd(cmd, value, consumer);
            } catch (Throwable e) {
                throw e;
            } finally {
                TRANS_STATE_TL.remove();
            }
        }
    }

    private static int getSyncIdx() {
        if (synchronizations != null) {
            Set value = synchronizations.get();
            return value == null ? 0 : value.size();
        } else {
            return IDX_TL.get().getAndIncrement() & Integer.MAX_VALUE;
        }
    }

    @Component
    public static class TransactionExecutor {
        @Transactional
        public void doWithTX(Runnable cmd) {
            cmd.run();
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void doWithNewTX(Runnable cmd) {
            cmd.run();
        }

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public void doWithoutTX(Runnable cmd) {
            cmd.run();
        }

        @Transactional
        public <T> void doWithTX(T value, Consumer<T> consumer) {
            consumer.accept(value);
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public <T> void doWithNewTX(T value, Consumer<T> consumer) {
            consumer.accept(value);
        }

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public <T> void doWithoutTX(T value, Consumer<T> consumer) {
            consumer.accept(value);
        }

        @Transactional
        public <T> T doWithTX(Supplier<T> supplier) {
            return supplier.get();
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public <T> T doWithNewTX(Supplier<T> supplier) {
            return supplier.get();
        }

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public <T> T doWithoutTX(Supplier<T> supplier) {
            return supplier.get();
        }

        @Transactional
        public <R, T> T doWithTX(R value, Function<R, T> function) {
            return function.apply(value);
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public <R, T> T doWithNewTX(R value, Function<R, T> function) {
            return function.apply(value);
        }

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public <R, T> T doWithoutTX(R value, Function<R, T> function) {
            return function.apply(value);
        }
    }
}

