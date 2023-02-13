package org.zj2.lite.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.common.Destroyable;
import org.zj2.lite.common.Releasable;
import org.zj2.lite.common.function.IntBeanConsumer;
import org.zj2.lite.common.function.LongBeanConsumer;
import org.zj2.lite.common.util.BeanUtil;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * RingArrayStream
 *
 * @author peijie.ye
 * @date 2023/1/17 12:40
 */
@SuppressWarnings("all")
public class RingArrayStream<T extends Releasable> implements Destroyable {
    private static int normalizeCapacity(int capacity) {
        if (capacity < 16) { return 16; }
        return 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RingArrayStream.class);
    private static final VarHandle STATES_AA = MethodHandles.arrayElementVarHandle(byte[].class);
    private static final int TRY_COUNT_CAL = 10;
    private static final int TRY_COUNT_YIELD = TRY_COUNT_CAL + 100;
    private static final int TRY_COUNT_PARK = TRY_COUNT_YIELD + 100;
    private static final int TRY_COUNT_MAX = 1 << 14;
    private static final int MAX_CONSUME_SIZE = 1 << 14;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_DESTROYED = 10;
    private static final int STATE_DESTROYED_NOW = 11;
    public static final long FAILURE_POS = -1;
    public static final long DESTROY_POS = -2;
    //
    private final int capacity;
    private final int mask;
    private final StateStep produceStep;
    private final StateStep[] steps;
    private volatile int streamState;
    private final byte[] states;
    private final Releasable[] references;

    public RingArrayStream(int capacity, Class<T> enumType) {
        this(1, capacity, enumType);
    }

    public RingArrayStream(int stepCount, int capacity, final Class<T> enumType) {
        this(stepCount, capacity, () -> BeanUtil.newInstance(enumType));
    }

    public RingArrayStream(int capacity, Supplier<T> enumCreator) {
        this(1, capacity, enumCreator);
    }

    public RingArrayStream(int stepCount, int capacity, Supplier<T> enumCreator) {
        assert stepCount < Byte.MAX_VALUE;
        capacity = normalizeCapacity(capacity);
        this.capacity = capacity;
        this.mask = capacity - 1;
        this.produceStep = new StateStep(0, capacity, stepCount);
        this.steps = new StateStep[stepCount + 1];
        this.streamState = STATE_RUNNING;
        this.states = new byte[capacity];
        this.references = new Releasable[capacity];
        steps[0] = produceStep;
        for (int i = 1; i <= stepCount; ++i) { steps[i] = new StateStep(i, capacity, stepCount); }
        for (int i = 0; i < capacity; ++i) { references[i] = enumCreator.get(); }
    }

    @Override
    public void destroy() {
        this.streamState = STATE_DESTROYED;
        produceStep.tryNotify();
    }

    public void destroyNow() {
        this.streamState = STATE_DESTROYED_NOW;
        produceStep.tryNotify();
    }

    public int getCapacity() {
        return capacity;
    }

    public <E> boolean add(E value, BiConsumer<T, E> handler) {
        return add0(value, handler, false);
    }

    public <E> boolean tryAdd(E value, BiConsumer<T, E> handler) {
        return add0(value, handler, true);
    }

    public boolean add(int value, IntBeanConsumer<T> handler) {
        return add0(value, handler, false);
    }

    public boolean tryAdd(int value, IntBeanConsumer<T> handler) {
        return add0(value, handler, true);
    }

    public boolean add(long value, LongBeanConsumer<T> handler) {
        return add0(value, handler, false);
    }

    public boolean tryAdd(long value, LongBeanConsumer<T> handler) {
        return add0(value, handler, true);
    }

    private <E> boolean add0(E value, BiConsumer<T, E> handler, boolean tryNext) {
        long pos = tryNext ? tryNext() : next();
        if (pos > FAILURE_POS) {
            try {
                handler.accept(get(pos), value);
            } finally {
                publish(pos);
            }
            return true;
        }
        return false;
    }

    private boolean add0(long value, LongBeanConsumer<T> handler, boolean tryNext) {
        long pos = tryNext ? tryNext() : next();
        if (pos > FAILURE_POS) {
            try {
                handler.accept(value, get(pos));
            } finally {
                publish(pos);
            }
            return true;
        }
        return false;
    }

    private boolean add0(int value, IntBeanConsumer<T> handler, boolean tryNext) {
        long pos = tryNext ? tryNext() : next();
        if (pos > FAILURE_POS) {
            try {
                handler.accept(value, get(pos));
            } finally {
                publish(pos);
            }
            return true;
        }
        return false;
    }

    public long next() {
        return next0(-1);
    }

    public long tryNext() {
        return next0(TRY_COUNT_YIELD + 1);
    }

    private long next0(int timeoutCount) {
        if (streamState == STATE_RUNNING) {
            return nextPos(null, produceStep, 1, timeoutCount);
        }
        return DESTROY_POS;
    }

    public T get(long pos) {
        return (T) references[(int) (pos & mask)];
    }

    public void publish(long pos) {
        nextStep(produceStep, pos, true);
    }

    private long nextPos(StepStream<?> stream, StateStep step, int size, int timeoutCount) {//NOSONAR
        final AtomicLong readPos = step.readPos, limitPos = step.limitPos, maxPos = steps[step.prevState].readPos;
        long rPos, nPos, lPos = limitPos.get(), newLPos;
        for (int tryCount = 0; ; ) {
            if ((rPos = readPos.get()) < lPos || rPos < (lPos = limitPos.get())) {
                if ((nPos = rPos + size) <= lPos || (tryCount > (TRY_COUNT_YIELD - 1) && (nPos = lPos) > 0)) {
                    if (readPos.compareAndSet(rPos, nPos)) {
                        if (stream != null) { stream.setEndPos(nPos); }
                        return rPos;
                    } else {
                        continue;
                    }
                }
            }
            newLPos = nextLimitPos(step, limitPos, maxPos, size);
            if (newLPos > lPos) {
                lPos = newLPos;
            } else if (lPos == newLPos) {
                final int state = streamState;
                if (state == STATE_DESTROYED) {
                    if (rPos >= produceStep.readPos.get()) { return DESTROY_POS; }
                } else if (state == STATE_DESTROYED_NOW) {
                    return DESTROY_POS;
                }
                if (tryCount < Integer.MAX_VALUE) { ++tryCount; }
                if (timeoutCount < 0 || tryCount < timeoutCount) {
                    step.tryWait(tryCount);
                } else {
                    break;
                }
            } else {
                Thread.yield();
            }
        }
        return FAILURE_POS;
    }

    private long nextLimitPos(final StateStep step, AtomicLong limitPos, AtomicLong maxPos, int size) {
        if (step.tryLock()) {
            final long oldLPos = limitPos.get();
            final long max = maxPos.get() + step.offset;
            long lPos = oldLPos;
            final byte[] localStates = states;
            final byte targetState = step.state;
            final int localMask = mask;
            int len = size < 0x3F ? 0x7F : (size + (size >>> 1));
            for (int i = 0; lPos < max && i < len; ++i) {
                byte state = (byte) STATES_AA.getVolatile(localStates, (int) (lPos & localMask));
                if (state == targetState) {
                    ++lPos;
                } else {
                    break;
                }
            }
            if (lPos > oldLPos) { limitPos.compareAndSet(oldLPos, lPos); }
            step.unLock();
            return lPos;
        }
        return -1;
    }

    private void nextStep(final StateStep step, final long pos, final boolean notifyNextStep) {
        final int idx = (int) (pos & mask);
        final byte nextState = step.nextState;
        if (nextState == 0) {
            try {
                references[idx].release();
            } catch (Throwable ignore) { }
        }
        STATES_AA.setVolatile(states, idx, nextState);
        if (notifyNextStep) { steps[nextState].tryNotify(); }
    }

    public void consume(int stepIdx, Consumer<T> consumer) {
        new StepStream<>(this, steps[stepIdx + 1]).consume(consumer);
    }

    private static class StateStep {
        private final AtomicLong readPos;
        private final AtomicLong limitPos;
        private final int offset;
        private final AtomicInteger lockFlag;
        private volatile int waitThread;
        private final byte state;
        private final byte prevState;
        private final byte nextState;


        private StateStep(int state, int capacity, int stepCount) {
            this.readPos = new AtomicLong(0L);
            this.limitPos = new AtomicLong(state == 0 ? capacity : 0);
            this.lockFlag = new AtomicInteger(0);
            this.offset = state == 0 ? capacity : 0;
            this.waitThread = 0;
            this.state = (byte) state;
            this.prevState = (byte) (state == 0 ? stepCount : state - 1);
            this.nextState = (byte) (state == stepCount ? 0 : (state + 1));
        }

        private boolean tryLock() {
            int l = lockFlag.get();
            return l == 0 && lockFlag.compareAndSet(0, 1);
        }

        private void unLock() {
            lockFlag.set(0);
        }

        private int tryWait(int count) {
            if (count < TRY_COUNT_CAL) {
                return count + 1;
            } else if (count < TRY_COUNT_YIELD) {
                Thread.yield();
            } else if (count < TRY_COUNT_PARK) {
                LockSupport.parkNanos(10000);
            } else {
                synchronized (this) {
                    ++waitThread;
                    try {
                        this.wait(1);
                    } catch (Throwable ignored) {
                    } finally {
                        --waitThread;
                    }
                }
            }
            return count;
        }

        private void tryNotify() {
            if (waitThread > 0) { synchronized (this) { this.notify(); } }
        }
    }

    private static class StepStream<T extends Releasable> {
        private static final int NOTIFY_COUNT = (1 << 8) - 1;
        private final RingArrayStream<T> stream;
        private final StateStep step;
        private long endPos;

        private StepStream(RingArrayStream<T> stream, StateStep step) {
            this.stream = stream;
            this.step = step;
        }

        private void consume(final Consumer<T> consumer) {
            final RingArrayStream<T> localStream = this.stream;
            final StateStep localStep = this.step;
            final Object[] references = localStream.references;
            final int mask = localStream.mask;
            Object value;
            for (long pos, size = 0, take = 0, end; ; ) {
                pos = localStream.nextPos(this, localStep, getDynamicConsumeSize(size, take), TRY_COUNT_MAX);
                if (pos > FAILURE_POS) {
                    end = endPos;
                    size = end - pos;
                    take = System.currentTimeMillis();
                    //
                    for (; pos < end; ++pos) {
                        try {
                            consumer.accept((T) references[(int) (mask & pos)]);
                        } catch (Throwable e) {
                            LOGGER.error("Data consume error!", e);
                        } finally {
                            localStream.nextStep(step, pos, (pos & NOTIFY_COUNT) == NOTIFY_COUNT);
                        }
                    }
                    take = System.currentTimeMillis() - take;
                } else if (pos == FAILURE_POS) {
                    size = 0;
                } else {
                    break;
                }
            }
        }

        private void setEndPos(long endPos) {
            this.endPos = endPos;
        }

        // 动态消费数量，让整体生产消费处于合理平衡
        private static int getDynamicConsumeSize(long size, long take) {
            if (size < 4) { return 4; }
            if (take < 2) {
                size <<= 1;
                return size < MAX_CONSUME_SIZE ? (int) size : MAX_CONSUME_SIZE;
            } else {
                size = MAX_CONSUME_SIZE / take;
                return size < 4 ? 4 : (int) size;
            }
        }
    }
}
