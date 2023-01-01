package org.zj2.lite.helper.chain;


import org.zj2.lite.helper.entity.TimeConsuming;
import org.zj2.lite.helper.handler.BizFunc;

public class ChainBizFuncStack extends TimeConsuming {
    private static final long serialVersionUID = -2706247459007284321L;
    private final transient ChainBizFuncWrapper bizFuncWrapper;
    private final transient Object context;

    ChainBizFuncStack(ChainBizFuncWrapper bizFuncWrapper, Object context) {
        super(bizFuncWrapper.instanceName());
        this.bizFuncWrapper = bizFuncWrapper;
        this.context = context;
    }

    ChainBizFuncWrapper bizFuncWrapper() {
        return bizFuncWrapper;
    }

    public Object context() {
        return context;
    }

    public BizFunc handler() {
        return bizFuncWrapper.instance();
    }

    @Override
    protected void finish() {//NOSONAR
        super.finish();
    }
}