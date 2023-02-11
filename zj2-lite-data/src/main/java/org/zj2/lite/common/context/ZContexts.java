package org.zj2.lite.common.context;

import java.util.function.Supplier;

/**
 * ZContext
 *
 * @author peijie.ye
 * @date 2022/12/3 7:11
 */
public class ZContexts {
    private static final ZContext[] EMPTY_CONTEXT = {};
    private static final ThreadLocal<ZContexts> CONTEXT_TL = ThreadLocal.withInitial(ZContexts::new);//NOSONAR
    private ZContext[] contexts;

    private ZContexts() {
        contexts = EMPTY_CONTEXT;
    }

    private ZContexts(ZContexts context) {
        ZContext[] tmp = context.contexts;
        if (tmp != null && tmp.length > 0) {
            int len = tmp.length;
            contexts = new ZContext[len];
            for (int i = 0; i < len; ++i) {
                ZContext c = tmp[i];
                contexts[i] = c == null ? null : c.copy();
            }
        } else {
            contexts = EMPTY_CONTEXT;
        }
    }

    public static ZContexts current() {
        ZContexts context = CONTEXT_TL.get();
        if (context == null) { CONTEXT_TL.set(context = new ZContexts()); }//NOSONAR
        return context;
    }

    public static ZContexts copyContexts() {
        return new ZContexts(current());
    }

    public static ZContexts setContexts(ZContexts context) {
        ZContexts old = CONTEXT_TL.get();
        CONTEXT_TL.set(context);
        return old;
    }

    public static void clearContext() {
        ZContexts context = CONTEXT_TL.get();
        if (context != null) {
            ZContext[] contexts = context.contexts;
            for (int i = 0, len = contexts.length; i < len; ++i) { contexts[i] = null; }
        }
    }


    static <T extends ZContext> T getContext(int idx, Supplier<T> supplier) {
        ZContexts zcontext = current();
        ZContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            if (supplier == null) { return null; }
            zcontext.contexts = tmp = createNewContexts(tmp, idx + 1);
        }
        ZContext result = tmp[idx];
        if (result == null && supplier != null) {
            tmp[idx] = result = supplier.get();
        }
        //noinspection unchecked
        return (T) result;
    }

    static <T extends ZContext> T setContext(int idx, T context) {
        ZContexts zcontext = current();
        ZContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            if (context == null) { return null; }
            zcontext.contexts = tmp = createNewContexts(tmp, idx + 1);
        }
        ZContext old = tmp[idx];
        tmp[idx] = context;
        //noinspection unchecked
        return (T) old;
    }

    static <T extends ZContext> T clearContext(int idx) {
        ZContexts zcontext = CONTEXT_TL.get();
        if (zcontext == null) { return null; }
        ZContext[] tmp = zcontext.contexts;
        if (tmp == null || idx >= tmp.length) {
            return null;
        } else {
            ZContext old = tmp[idx];
            tmp[idx] = null;
            //noinspection unchecked
            return (T) old;
        }
    }

    private static ZContext[] createNewContexts(ZContext[] array, int capacity) {
        int newCap = capacity < 1 ? 16 : ((capacity >>> 3) << 3);
        if (newCap < capacity) { newCap += 8; }
        newCap = Math.max(newCap, 16);
        ZContext[] newArray = new ZContext[newCap];
        if (array != null) { System.arraycopy(array, 0, newArray, 0, array.length); }
        return newArray;
    }


}
