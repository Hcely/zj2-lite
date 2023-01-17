package org.zj2.lite.net.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.common.Destroyable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 *  RingArrayStream
 *
 * @author peijie.ye
 * @date 2023/1/17 12:40
 */
@SuppressWarnings("all")
public class RingArrayStream<T> implements Destroyable {
    private static int normalizeCapacity(int capacity) {
        if (capacity < 16) { return 16; }
        return 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RingArrayStream.class);
    private static final int TRY_COUNT_CAL = 10;
    private static final int TRY_COUNT_YIELD = TRY_COUNT_CAL + 100;
    private static final int TRY_COUNT_PARK = TRY_COUNT_YIELD + 100;
    private static final int TRY_COUNT_MAX = 1 << 14;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_DESTROYED = 10;
    private static final int STATE_DESTROYED_NOW = 11;
    private static final long FAILURE_POS = -1;
    private static final long DESTROY_POS = -2;
    //
    private final int capacity;
    private final int mask;
    private final StateStep produceStep;
    private final StateStep[] steps;
    private volatile int streamState;
    private final AtomicIntegerArray states;
    private final AtomicReferenceArray<T> references;


    public RingArrayStream(int capacity) {
        this(capacity, 1);
    }

    public RingArrayStream(int capacity, int stepCount) {
        capacity = normalizeCapacity(capacity);
        this.capacity = capacity;
        this.mask = capacity - 1;
        this.produceStep = new StateStep(capacity);
        this.steps = new StateStep[stepCount + 1];
        this.streamState = STATE_RUNNING;
        this.states = new AtomicIntegerArray(capacity);
        this.references = new AtomicReferenceArray<>(capacity);
        steps[0] = produceStep;
        for (int i = 0; i < stepCount; ) {
            ++i;
            steps[i] = new StateStep(i, i == stepCount);
        }
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

    public boolean add(T value) {
        return add0(value, -1);
    }

    public boolean tryAdd(T value) {
        return add0(value, TRY_COUNT_YIELD);
    }

    private boolean add0(T value, int timeoutCount) {
        if (streamState == STATE_RUNNING) {
            final long pos = nextPos(null, produceStep, 1L, timeoutCount);
            if (pos > FAILURE_POS) {
                references.set((int) (mask & pos), value);
                release(produceStep, pos, true);
                return true;
            }
        }
        return false;
    }

    private long nextPos(StepStream<?> stream, StateStep step, long size, int timeoutCount) {//NOSONAR
        final AtomicLong readPos = step.readPos, limitPos = step.limitPos, maxLimitPos = steps[step.nextState].recyclePos;
        long rPos, nPos, lPos = limitPos.get(), newLPos;
        int tryCount = 0;
        while (timeoutCount < 0 || tryCount < timeoutCount) {
            if ((rPos = readPos.get()) < lPos || rPos < (lPos = limitPos.get())) {
                if ((nPos = rPos + size) <= lPos || (tryCount > TRY_COUNT_YIELD - 1 && (nPos = lPos) > 0)) {
                    if (readPos.compareAndSet(rPos, nPos)) {
                        if (stream != null) { stream.setPos(rPos, nPos); }
                        return rPos;
                    } else {
                        continue;
                    }
                }
            }
            newLPos = nextLimitPos(step, limitPos, maxLimitPos);
            if (newLPos > lPos) {
                lPos = newLPos;
            } else if (lPos == newLPos) {
                final int state = streamState;
                if (state == STATE_DESTROYED) {
                    if (lPos >= produceStep.readPos.get()) {
                        return DESTROY_POS;
                    }
                } else if (state == STATE_DESTROYED_NOW) {
                    return DESTROY_POS;
                }
                step.tryWait(tryCount);
                if (tryCount < Integer.MAX_VALUE) { ++tryCount; }
            } else {
                Thread.yield();
            }
        }
        return FAILURE_POS;
    }

    private long nextLimitPos(final StateStep step, AtomicLong limitPos, AtomicLong maxLimitPos) {
        if (step.tryLock()) {
            final long oldLPos = limitPos.get();
            final long maxLPos = maxLimitPos.get();
            long lPos = oldLPos;
            final AtomicIntegerArray localStates = states;
            final int targetState = step.state;
            final int localMask = mask;
            for (int i = 0; lPos < maxLPos && i < 10000; ++i) {
                if (localStates.get((int) (lPos & localMask)) == targetState) {
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

    private void release(final StateStep step, final long pos, final boolean notifyNextStep) {
        final int idx = (int) (pos & mask);
        final int nextState = step.nextState;
        if (nextState == 0) { references.set(idx, null); }
        step.recyclePos.incrementAndGet();
        states.set(idx, nextState);
        if (notifyNextStep) { steps[nextState].tryNotify(); }
    }

    public void consume(int stepIdx, Consumer<T> consumer) {
        new StepStream<>(this, steps[stepIdx + 1]).consume(consumer);
    }

    private static class StateStep {
        private final AtomicLong recyclePos;
        private final AtomicLong readPos;
        private final AtomicLong limitPos;
        private final AtomicInteger lockFlag;
        private volatile int waitThread;
        private final int state;
        private final int nextState;

        private StateStep(int capacity) {
            this(0, false);
            this.limitPos.set(capacity);
        }

        private StateStep(int state, boolean lastStep) {
            this.recyclePos = new AtomicLong(0L);
            this.readPos = new AtomicLong(0L);
            this.limitPos = new AtomicLong(0L);
            this.lockFlag = new AtomicInteger(0);
            this.waitThread = 0;
            this.state = state;
            this.nextState = lastStep ? 0 : (state + 1);
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

    private static class StepStream<T> {
        private final RingArrayStream<T> stream;
        private final StateStep step;
        private long startPos;
        private long endPos;
        private long startTime;
        private long endTime;

        private StepStream(RingArrayStream<T> stream, StateStep step) {
            this.stream = stream;
            this.step = step;
        }

        private void consume(final Consumer<T> consumer) {
            final RingArrayStream<T> localStream = this.stream;
            final StateStep localStep = this.step;
            final AtomicReferenceArray<T> references = localStream.references;
            final int mask = localStream.mask;
            long pos;
            for (; ; ) {
                if ((pos = localStream.nextPos(this, localStep, getDynamicSize(), TRY_COUNT_MAX)) > FAILURE_POS) {
                    startTime = System.currentTimeMillis();
                    for (long end = endPos; pos < end; ++pos) {
                        try {
                            T value = references.get((int) (mask & pos));
                            if (value != null) { consumer.accept(value); }
                        } catch (Throwable e) {
                            LOGGER.error("Data consume error!", e);
                        } finally {
                            localStream.release(step, pos, (pos & 255) == 255);
                        }
                    }
                    endTime = System.currentTimeMillis();
                } else if (pos == DESTROY_POS) {
                    break;
                }
            }
        }

        private void setPos(long startPos, long endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        private long getDynamicSize() {
            long size = endPos - startPos;
            if (size < 1) { return 10L; }
            long take = endTime - startTime;
            if (take < 1) {
                size <<= 1;
                return size < 10000L ? size : 10000L;
            } else {
                size = 10000L / take;
                return size < 10L ? 10L : size;
            }
        }
    }
}
