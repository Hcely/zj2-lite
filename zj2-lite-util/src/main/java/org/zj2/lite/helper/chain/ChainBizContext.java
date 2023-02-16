package org.zj2.lite.helper.chain;

import org.slf4j.Logger;
import org.zj2.lite.helper.entity.TimeConsuming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ChainBizContext extends TimeConsuming {
    private static final long serialVersionUID = -8251440919834942863L;
    private final transient ChainBizContext parent;
    private final transient ChainBizHandler chainHandler;
    private final transient List<ChainBizFuncStack> handlerStacks;
    private final transient Object parentContext;
    private transient Object currentContext;

    ChainBizContext(ChainBizContext parent, ChainBizHandler chainHandler, Object context) {
        super(parent == null ? chainHandler.handlerName() : parent.getName());
        this.parent = parent;
        this.chainHandler = chainHandler;
        this.handlerStacks = new ArrayList<>(Math.max(chainHandler.getInitializeStackCapcity(), 0));
        this.parentContext = context;
        this.currentContext = context;
    }

    public Logger logger() {
        ChainBizContext cxt = this;
        while (cxt.parent != null) { cxt = cxt.parent; }
        return cxt.chainHandler.logger();
    }

    public ChainBizHandler chainHandler() {
        return chainHandler;
    }

    public List<ChainBizFuncStack> handlerStacks() {
        return Collections.unmodifiableList(handlerStacks);
    }

    ChainBizFuncStack startStack(ChainBizFuncWrapper wrapper, Object context) {
        ChainBizFuncStack stack = new ChainBizFuncStack(wrapper, context);
        handlerStacks.add(stack);
        return stack;
    }

    public ChainBizContext parent() {
        return parent;
    }

    public <T> T rootContext() {
        ChainBizContext cxt = this;
        while (cxt.parent != null) { cxt = cxt.parent; }
        return (T) cxt.parentContext;
    }

    public <T> T parentContext() {
        return (T) parentContext;
    }

    public <T> T currentContext() {
        return (T) currentContext;
    }

    <T> T setCurrentContext(Object newContext) {
        Object oldContext = currentContext;
        currentContext = newContext == null ? currentContext : newContext;
        return (T) oldContext;
    }

    @Override
    protected void finish() {//NOSONAR
        super.finish();
    }
}