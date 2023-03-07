package org.zj2.lite.common.util;

import org.zj2.lite.common.context.ZContexts;

/**
 * ZThread
 *
 * @author peijie.ye
 * @date 2023/3/5 13:12
 */
public class ZThread extends Thread {
    private ZContexts contexts;

    public ZThread() {
    }

    public ZThread(Runnable target) {
        super(target);
    }

    public ZThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public ZThread(String name) {
        super(name);
    }

    public ZThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public ZThread(Runnable target, String name) {
        super(target, name);
    }

    public ZThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name, 0);
    }

    public ZContexts getContexts() {
        return contexts;
    }

    public void setContexts(ZContexts contexts) {
        this.contexts = contexts;
    }
}
