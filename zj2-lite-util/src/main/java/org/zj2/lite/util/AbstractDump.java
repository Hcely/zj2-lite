package org.zj2.lite.util;

import org.zj2.lite.common.util.NumUtil;

import java.util.Arrays;

/**
 * Dump
 *
 * @author peijie.ye
 * @date 2023/2/20 16:40
 */
@SuppressWarnings("all")
public class AbstractDump<E extends Comparable> {
    protected Object[] dump;
    protected int size = 0;

    public AbstractDump() {
    }

    public AbstractDump(int initializeCapacity) {
        ensureCapacity(initializeCapacity);
    }

    public void ensureCapacity(int capacity) {
        capacity = NumUtil.plus.ceilMultipleOf(capacity, 10);
        if (dump == null) {
            dump = new Object[capacity];
        } else {
            dump = Arrays.copyOf(dump, capacity, Object[].class);
        }
    }

    protected boolean addEnum(E e) {
        if (e == null) { return false; }
        int idx = size;
        ensureCapacity(idx + 1);
        dump[idx] = e;
        ++size;
        for (; idx > 0; ) {
            int pIdx = (idx - 1) >>> 1;
            E pEnum = (E) dump[pIdx];
            if (lte(pEnum, e)) {
                break;
            } else {
                dump[pIdx] = e;
                dump[idx] = pEnum;
                idx = pIdx;
            }
        }
        return true;
    }

    protected E pollEnum() {
        if (size > 0 && dump != null) {
            E value = (E) dump[0];
            int lastIdx = --size;
            if (lastIdx == 0) {
                dump[0] = null;
            } else {
                dump[0] = dump[lastIdx];
                dump[lastIdx] = null;
                updateHeader();
            }
            return value;
        }
        return null;
    }

    protected E headerEnum() {
        return size > 0 && dump != null ? (E) dump[0] : null;
    }

    public int size() {
        return size;
    }

    protected void updateHeader() {
        if (size <= 1 || dump == null) { return; }
        Object[] dump = this.dump;
        E value = (E) dump[0];
        for (int pIdx = 0, cIdx1 = 1, cIdx2, len = size; cIdx1 < len; ) {
            E c1 = (E) dump[cIdx1];
            cIdx2 = cIdx1 + 1;
            E c2 = cIdx2 < len ? (E) dump[cIdx2] : null;
            if (gt(value, c1)) {
                if (c2 == null || gt(c2, c1)) {
                    dump[cIdx1] = value;
                    dump[pIdx] = c1;
                    pIdx = cIdx1;
                } else {
                    dump[cIdx2] = value;
                    dump[pIdx] = c2;
                    pIdx = cIdx2;
                }
            } else if (c2 != null && gt(value, c2)) {
                dump[cIdx2] = value;
                dump[pIdx] = c2;
                pIdx = cIdx2;
            } else {
                break;
            }
            cIdx1 = 1 + (pIdx << 1);
        }
    }

    private boolean gt(E e1, E e2) {
        return !lte(e1, e2);
    }

    protected boolean lte(E e1, E e2) {
        if (e1 == null) { return false; }
        if (e2 == null) { return true; }
        return e1.compareTo(e2) < 1;
    }
}
