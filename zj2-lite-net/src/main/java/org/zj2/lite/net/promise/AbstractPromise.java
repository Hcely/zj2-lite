package org.zj2.lite.net.promise;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * AbstractPromise
 *
 * @author peijie.ye
 * @date 2023/3/2 15:53
 */
public abstract class AbstractPromise implements Promise {
    private static final AtomicIntegerFieldUpdater<AbstractPromise> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractPromise.class,
            "state");
    private static final AtomicReferenceFieldUpdater<AbstractPromise, PromiseListenerNode> HEADER_UPDATER = AtomicReferenceFieldUpdater.newUpdater(
            AbstractPromise.class, PromiseListenerNode.class, "listenerHeader");
    private static final PromiseListenerNode NONE_NODE = new PromiseListenerNode(null);
    public static final int STATE_INIT = 0;
    public static final int STATE_COMPLETING = 199;
    public static final int STATE_SUCCESS = 200;
    public static final int STATE_FAILURE = 400;
    private volatile int state;
    private int completedState;
    private Object flag;

    public AbstractPromise() {
    }

    public AbstractPromise(Object flag) {
        this.flag = flag;
    }

    @Override
    public Promise flag(Object value) {
        this.flag = value;
        return this;
    }

    @Override
    public <T> T flag() {
        //noinspection unchecked
        return (T)flag;
    }

    private volatile int waitCount;
    private volatile PromiseListenerNode listenerHeader;//NOSONAR

    public int getState() {
        return state;
    }

    protected final int setState(int newState) {
        return STATE_UPDATER.getAndSet(this, newState);
    }

    protected final boolean casState(int oldState, int newState) {
        return state == oldState && STATE_UPDATER.compareAndSet(this, oldState, newState);
    }

    protected final boolean tryCompleted(int completedState) {
        if(completedState <= STATE_COMPLETING) { return false; }
        for(int s = state; s < STATE_COMPLETING; s = state) {
            if(STATE_UPDATER.compareAndSet(this, s, STATE_COMPLETING)) {
                this.completedState = completedState;
                return true;
            }
        }
        return false;
    }

    protected final void completed() {
        if(state == STATE_COMPLETING && STATE_UPDATER.compareAndSet(this, STATE_COMPLETING, completedState)) {
            PromiseListenerNode listenerNode = tryGetListeners();
            notifyWaiters();
            callback(listenerNode);
        }
    }


    private PromiseListenerNode tryGetListeners() {
        for(PromiseListenerNode result = listenerHeader; ; result = listenerHeader) {
            if(HEADER_UPDATER.compareAndSet(this, result, NONE_NODE)) {
                return result;
            }
        }
    }

    private void notifyWaiters() {
        if(waitCount > 0) { synchronized(this) { if(waitCount > 0) { this.notifyAll(); } } }
    }

    private void callback(PromiseListenerNode listenerNode) {
        while(listenerNode != null) {
            try {
                listenerNode.listener.onCompleted(this);
            } catch(Throwable ignored) {//NOSONAR
            }
            listenerNode = listenerNode.next;
        }
    }


    @Override
    public boolean isCompleted() {
        return state >= STATE_SUCCESS;
    }

    @Override
    public Promise sync() {
        sync0(true, Long.MAX_VALUE);
        return this;
    }

    @Override
    public Promise sync(long timeout) {
        sync0(false, System.currentTimeMillis() + timeout);
        return this;
    }

    @SneakyThrows
    private void sync0(boolean loop, long expireAt) {
        while(!isCompleted() && (loop || System.currentTimeMillis() < expireAt)) {
            synchronized(this) {
                waitCount = 1;
                if(!isCompleted()) {
                    long timeout = loop ? 100 : Math.min(expireAt - System.currentTimeMillis(), 100);
                    this.wait(timeout);
                }
            }
        }
    }

    @Override
    public Promise addListener(PromiseListener listener) {
        if(listener == null) { return this; }
        if(!addListener0(listener)) { listener.onCompleted(this); }
        return this;
    }

    private boolean addListener0(PromiseListener listener) {
        if(isCompleted()) { return false; }
        PromiseListenerNode header;
        PromiseListenerNode node = new PromiseListenerNode(listener);
        while(!isCompleted() && (header = listenerHeader) != NONE_NODE) {
            node.next = header;
            if(HEADER_UPDATER.compareAndSet(this, header, node)) { return true; }
        }
        return false;
    }

    private static class PromiseListenerNode {
        private final PromiseListener listener;
        private PromiseListenerNode next;

        private PromiseListenerNode(PromiseListener listener) {
            this.listener = listener;
        }
    }
}
