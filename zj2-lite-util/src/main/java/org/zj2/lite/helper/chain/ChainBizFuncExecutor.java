package org.zj2.lite.helper.chain;


import org.zj2.lite.common.Supportable;
import org.zj2.lite.helper.handler.BizFunc;

public interface ChainBizFuncExecutor extends Supportable<BizFunc> {
    boolean execute(ChainBizContext chainContext, BizFunc handler, Object context);
}