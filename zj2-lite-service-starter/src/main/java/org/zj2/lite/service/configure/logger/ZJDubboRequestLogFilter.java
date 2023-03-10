package org.zj2.lite.service.configure.logger;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.lite.common.util.StrUtil;

@Activate(group = CommonConstants.PROVIDER, order = -10000)
public class ZJDubboRequestLogFilter extends AbstractRequestLogFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Throwable error = null;
        final RequestLogContext context = getContext(invoker, invocation);
        try {
            logRequest(context);
            return invoker.invoke(invocation);
        } catch(Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if(context.getLogState() != RequestLogContext.STATE_COMPLETED) {
                if(context.getLogState() != RequestLogContext.STATE_RESPONSE) { context.response(null, null, error); }
                context.completed(error == null ? 200 : 500);
                logResponse(context);
            }
        }
    }

    private RequestLogContext getContext(Invoker<?> invoker, Invocation invocation) {
        String uri = StrUtil.concat(invoker.getInterface().getSimpleName(), '.', invocation.getMethodName());
        return RequestLogContext.setContext("DUBBO", null, uri);
    }

}
