package org.zj2.common.uac.auth.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.lite.service.constant.RequestMethods;
import org.zj2.lite.service.util.ServiceUriUtil;

/**
 * DubboConsumerContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
@Activate(group = CommonConstants.CONSUMER, order = -2000)
public class DubboConsumerRequestContextFilter extends AbsClientRequestSignInterceptor<Invocation> implements Filter {
    @Override
    protected void setValue(Invocation request, String key, String value) {
        if(StringUtils.isNotEmpty(value)) {
            request.setAttachment(key, value);
        }
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String uri = ServiceUriUtil.getMethodName(invoker.getInterface(), invocation.getMethodName(), invocation.getParameterTypes());
        setRequestContext(invocation, RequestMethods.DUBBO, uri);
        return invoker.invoke(invocation);
    }
}
