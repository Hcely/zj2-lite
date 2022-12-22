package org.zj2.lite.helper.chain;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.helper.handler.BizFunc;
import org.zj2.lite.helper.handler.BizHandler;
import org.zj2.lite.helper.handler.BizMultiFunc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * LinkedBizHandler
 * <br>CreateDate 一月 11,2022
 * @author peijie.ye
 * @since 1.0
 */
@SuppressWarnings("all")
public class ChainBizHandler<T> implements BizHandler<T> {
    private static final Logger DEF_LOGGER = LoggerFactory.getLogger(ChainBizHandler.class);
    private static final ChainBizFuncWrapper[] EMPTY_HANDLERS = {};

    public static <T> BizHandler<T> chain(BizFunc... handlers) {
        if (ArrayUtils.isEmpty(handlers)) {return BizHandler.EMPTY_HANDLER;}
        ChainBizFuncWrapper[] wrappers = new ChainBizFuncWrapper[handlers.length];
        for (int i = 0, len = handlers.length; i < len; ++i) {
            wrappers[i] = new ChainBizFuncWrapper(handlers[i]);
        }
        return new ChainBizHandler<>(wrappers);
    }

    @SafeVarargs
    public static <T> BizHandler<T> chain(Class<? extends BizFunc>... handlerTypes) {
        if (ArrayUtils.isEmpty(handlerTypes)) {return BizHandler.EMPTY_HANDLER;}
        Builder<T> builder = new Builder<>();
        for (int i = 0, len = handlerTypes.length; i < len; ++i) {
            builder.addHandler(handlerTypes[i]);
        }
        return builder.build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String handlerName;
        private Logger logger;
        private List<ChainBizFuncWrapper> handlers;

        protected Builder() {
        }

        public Builder<T> addHandler(BizHandler<T> handler) {
            return addHandler((BizFunc) handler);
        }

        public Builder<T> addHandler(BizVHandler<T> handler) {
            return addHandler((BizFunc) handler);
        }

        public Builder<T> addHandler(Class<? extends BizFunc> handlerType) {
            if (BizMultiFunc.class.isAssignableFrom(handlerType)) {
                Collection<? extends BizFunc> funcs = ChainBizHandlerUtil.getHandlers(handlerType);
                if (funcs != null && !funcs.isEmpty()) {
                    for (BizFunc func : funcs) {addHandler(func);}
                }
            } else {
                addHandler(ChainBizHandlerUtil.getHandler(handlerType));
            }
            return this;
        }

        public Builder<T> addHandler(BizFunc handler) {
            if (handler != null) {
                if (handlers == null) {handlers = new ArrayList<>(10);}
                handlers.add(new ChainBizFuncWrapper(handler));
            }
            return this;
        }

        public Builder<T> handlerName(String handlerName) {
            this.handlerName = handlerName;
            return this;
        }

        public Builder<T> logger(Logger logger) {
            this.logger = logger;
            return this;
        }


        public ChainBizHandler<T> build() {
            ChainBizFuncWrapper[] wrappers =
                    handlers == null ? null : handlers.toArray(new ChainBizFuncWrapper[handlers.size()]);
            return new ChainBizHandler<>(handlerName, logger, wrappers);
        }
    }

    private final String handlerName;
    private final Logger logger;
    private final ChainBizFuncWrapper[] bizNodeWrappers;

    private ChainBizHandler(ChainBizFuncWrapper[] handlers) {
        this(null, null, handlers);
    }

    private ChainBizHandler(String handlerName, Logger logger, ChainBizFuncWrapper[] handlers) {
        this.handlerName = handlerName;
        this.logger = logger == null ? DEF_LOGGER : logger;
        this.bizNodeWrappers = ArrayUtils.isEmpty(handlers) ? EMPTY_HANDLERS : handlers;
    }

    public String handlerName() {
        return handlerName;
    }

    public Logger logger() {
        return logger;
    }

    int getInitializeStackCapcity() {
        return bizNodeWrappers.length;
    }

    @Override
    public boolean handle(T context) {
        final ChainBizFuncWrapper[] handlers = this.bizNodeWrappers;
        if (handlers == null || handlers.length == 0) {return true;}
        //
        boolean interrupted = false;
        Throwable error = null;
        ChainBizContext chainContext = ChainBizHandlerUtil.startChain(this, context);
        try {
            for (int i = 0, len = handlers.length; i < len; ++i) {
                if (!handlers[i].execute(chainContext)) {
                    interrupted = true;
                    return false;
                }
            }
            return true;
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            completed(chainContext, error, interrupted);
            ChainBizHandlerUtil.completeChain(chainContext.parent());
            chainContext.finish();
        }
    }

    private void completed(ChainBizContext chainContext, Throwable error, boolean interrupted) {
        List<ChainBizFuncStack> stacks = chainContext.handlerStacks();
        for (int idx = stacks.size() - 1; idx > -1; --idx) {
            ChainBizFuncStack stack = stacks.get(idx);
            stack.bizFuncWrapper().onCompleted(chainContext, stack.context(), error, interrupted);
        }
    }
}
