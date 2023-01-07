package org.zj2.lite.common.context;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 *  ZContext
 *
 * @author peijie.ye
 * @date 2022/12/3 7:11
 */
public class ZContext {
    private static final long serialVersionUID = 20221203071103L;
    private static final BaseContext[] EMPTY_CONTEXT = {};
    private static final ThreadLocal<ZContext> CONTEXT_TL = ThreadLocal.withInitial(ZContext::new);//NOSONAR
    private static final AtomicInteger contextIdx = new AtomicInteger(0);

    private BaseContext[] contexts;

    private ZContext() {
        contexts = EMPTY_CONTEXT;
    }

    private ZContext(ZContext context) {
        BaseContext[] tmp = context.contexts;
        if (tmp != null && tmp.length > 0) {
            int len = tmp.length;
            contexts = new BaseContext[len];
            for (int i = 0; i < len; ++i) {
                BaseContext c = tmp[i];
                contexts[i] = c == null ? null : c.clone();
            }
        } else {
            contexts = EMPTY_CONTEXT;
        }
    }

    static int nextIdx() {
        return contextIdx.getAndIncrement();
    }

    public static ZContext current() {
        ZContext context = CONTEXT_TL.get();
        if (context == null) { CONTEXT_TL.set(context = new ZContext()); }//NOSONAR
        return context;
    }

    public static ZContext copyContext() {
        return new ZContext(current());
    }

    public static ZContext setContext(ZContext context) {
        ZContext old = CONTEXT_TL.get();
        CONTEXT_TL.set(context);
        return old;
    }

    public static void clearContext() {
        ZContext context = CONTEXT_TL.get();
        if (context != null) {
            BaseContext[] contexts = context.contexts;
            for (int i = 0, len = contexts.length; i < len; ++i) { contexts[i] = null; }
        }
    }


    static <T extends BaseContext> T getSubContext(int idx, Supplier<T> supplier) {
        ZContext zcontext = current();
        BaseContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            if (supplier == null) { return null; }
            zcontext.contexts = tmp = createNewContexts(tmp, idx + 1);
        }
        BaseContext result = tmp[idx];
        if (result == null && supplier != null) {
            tmp[idx] = result = supplier.get();
        }
        //noinspection unchecked
        return (T) result;
    }

    static <T extends BaseContext> T setSubContext(int idx, T context) {
        ZContext zcontext = current();
        BaseContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            if (context == null) { return null; }
            zcontext.contexts = tmp = createNewContexts(tmp, idx + 1);
        }
        BaseContext old = tmp[idx];
        tmp[idx] = context;
        //noinspection unchecked
        return (T) old;
    }

    static <T extends BaseContext> T clearSubContext(int idx) {
        ZContext zcontext = CONTEXT_TL.get();
        if (zcontext == null) { return null; }
        BaseContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            return null;
        } else {
            BaseContext old = tmp[idx];
            tmp[idx] = null;
            //noinspection unchecked
            return (T) old;
        }
    }

    private static BaseContext[] createNewContexts(BaseContext[] array, int capacity) {
        int newCap = capacity < 1 ? 16 : ((capacity >>> 3) << 3);
        if (newCap < capacity) { newCap += 8; }
        BaseContext[] newArray = new BaseContext[newCap];
        if (array != null) { System.arraycopy(array, 0, newArray, 0, array.length); }
        return newArray;
    }

}
