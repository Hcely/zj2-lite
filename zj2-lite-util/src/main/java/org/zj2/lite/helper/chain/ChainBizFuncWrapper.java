package org.zj2.lite.helper.chain;

import org.slf4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.zj2.lite.common.Supportable;
import org.zj2.lite.common.text.StrFormatter;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.helper.entity.BizInterceptorError;
import org.zj2.lite.helper.handler.BizFunc;

@SuppressWarnings("all")
final class ChainBizFuncWrapper implements BizFunc {
    private static final StrFormatter EXECUTING_MSG = StrUtil.getFormatter("Chain handler[{}] executing");
    private static final StrFormatter EXECUTE_ERROR_MSG = StrUtil.getFormatter("Chain handler[{}] executing error");
    private static final StrFormatter COMPLETED_MSG = StrUtil.getFormatter("Chain handler[{}] on completed");
    private static final StrFormatter COMPLETED_ERROR_MSG = StrUtil.getFormatter(
            "Chain handler[{}] on completed error");
    private final ChainBizFuncExecutor executor;
    private final String instanceName;
    private final BizFunc instance;
    private final boolean supportable;
    private final boolean listenable;

    ChainBizFuncWrapper(BizFunc instance) {
        this.executor = ChainBizHandlerUtil.getExecutor(instance);
        this.instanceName = AopUtils.getTargetClass(instance).getSimpleName();
        this.instance = instance;
        this.supportable = instance instanceof Supportable;
        this.listenable = instance instanceof ChainCompletedListener;
    }

    String instanceName() {
        return instanceName;
    }

    BizFunc instance() {
        return instance;
    }

    boolean execute(ChainBizContext chainContext) {
        Object context = chainContext.currentContext();
        if (executor == null || !isSupport(context)) { return true; }
        final ChainBizFuncStack stack = chainContext.startStack(this, context);
        Logger logger = chainContext.logger();
        try {
            logger.info(EXECUTING_MSG.format(instanceName));
            return executor.execute(chainContext, instance, context);
        } catch (BizInterceptorError e) {
            return false;
        } catch (Throwable e) {
            logger.error(EXECUTE_ERROR_MSG.format(instanceName), e);
            throw e;
        } finally {
            stack.finish();
        }
    }

    private boolean isSupport(Object context) {
        if (supportable) {
            try {
                return ((Supportable) instance).supports(context);
            } catch (Throwable ignored) { }
            return false;
        } else {
            return true;
        }
    }

    void onCompleted(ChainBizContext chainContext, Object context, Throwable error, boolean interrupted) {
        if (listenable) {
            Logger logger = chainContext.logger();
            try {
                logger.info(COMPLETED_MSG.format(instanceName));
                ((ChainCompletedListener) instance).onCompleted(context, error, interrupted);
            } catch (Throwable e) {
                logger.error(COMPLETED_ERROR_MSG.format(instanceName), e);
            }
        }
    }
}