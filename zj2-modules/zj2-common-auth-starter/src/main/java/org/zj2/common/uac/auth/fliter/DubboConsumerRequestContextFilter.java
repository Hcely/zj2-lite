package org.zj2.common.uac.auth.fliter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.common.uac.auth.util.ServerSignUtil;

/**
 *  DubboConsumerContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
@Activate(group = CommonConstants.CONSUMER, order = -2000)
public class DubboConsumerRequestContextFilter extends AbsRequestContextClientInterceptor<Invocation> implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String uri = ServerSignUtil.getUri(invoker.getInterface(), invocation.getMethodName(),
                invocation.getParameterTypes());
        setContext(invocation, DubboProviderRequestContextFilter.DUBBO_METHOD, uri);
        return invoker.invoke(invocation);
    }

    @Override
    protected void setValue(Invocation request, String key, String value) {
        request.setAttachment(key, value);
    }
}
