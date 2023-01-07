package org.zj2.lite.helper.chain;


import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.helper.handler.BizContextConverter;
import org.zj2.lite.helper.handler.BizFunc;
import org.zj2.lite.helper.handler.BizHandler;
import org.zj2.lite.helper.handler.BizRespHandler;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.helper.handler.BizValidator;

@SuppressWarnings("all")
interface ChainBizFuncExecutors {
    ChainBizFuncExecutor BIZ_FILTER_EXECUTOR = new ChainBizFuncExecutor() {
        @Override
        public boolean execute(ChainBizContext chainContext, BizFunc handler, Object context) {
            ((BizVHandler) handler).handle(context);
            return true;
        }

        @Override
        public boolean supports(BizFunc obj) {
            return obj instanceof BizVHandler;
        }
    };
    ChainBizFuncExecutor BIZ_HANDLER_EXECUTOR = new ChainBizFuncExecutor() {
        @Override
        public boolean execute(ChainBizContext chainContext, BizFunc handler, Object context) {
            return ((BizHandler) handler).handle(context);
        }

        @Override
        public boolean supports(BizFunc obj) {
            return obj instanceof BizHandler;
        }
    };
    ChainBizFuncExecutor BIZ_RESP_EXECUTOR = new ChainBizFuncExecutor() {
        @Override
        public boolean execute(ChainBizContext chainContext, BizFunc handler, Object context) {
            Object resp = ((BizRespHandler) handler).handle(context);
            return !(resp instanceof Boolean) || ((Boolean) resp);
        }

        @Override
        public boolean supports(BizFunc obj) {
            return obj instanceof BizRespHandler;
        }
    };
    ChainBizFuncExecutor BIZ_VALIDATOR_EXECUTOR = new ChainBizFuncExecutor() {
        @Override
        public boolean execute(ChainBizContext chainContext, BizFunc handler, Object context) {
            ZRBuilder hr = new ZRBuilder();
            ((BizValidator) handler).valid(context, hr);
            if (hr.hasError()) { throw hr.buildError(); }
            return true;
        }

        @Override
        public boolean supports(BizFunc obj) {
            return obj instanceof BizValidator;
        }
    };
    ChainBizFuncExecutor BIZ_CONVERTER_EXECUTOR = new ChainBizFuncExecutor() {
        @Override
        public boolean execute(ChainBizContext chainContext, BizFunc handler, Object context) {
            Object newContext = ((BizContextConverter) handler).convert(chainContext);
            if (newContext != null) { chainContext.setCurrentContext(newContext); }
            return true;
        }

        @Override
        public boolean supports(BizFunc obj) {
            return obj instanceof BizContextConverter;
        }
    };
}