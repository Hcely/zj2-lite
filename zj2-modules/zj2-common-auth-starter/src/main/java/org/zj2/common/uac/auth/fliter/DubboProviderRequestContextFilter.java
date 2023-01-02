package org.zj2.common.uac.auth.fliter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.service.context.ServiceRequestContext;

/**
 *  DubboContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/9 0:43
 */
@Activate(group = CommonConstants.PROVIDER, order = -2000)
public class DubboProviderRequestContextFilter extends AbsContextFilter<Invocation> implements Filter {
    static final String DUBBO_METHOD = "DUBBO";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String uri = ServerSignUtil.getUri(invoker.getInterface(), invocation.getMethodName(),
                invocation.getParameterTypes());
        setContext(invocation, DUBBO_METHOD, uri);
        return invoker.invoke(invocation);
    }

    @Override
    protected String getValue(Invocation request, String key) {
        return request.getAttachment(key);
    }

    @Override
    protected String getAttrIp(Invocation request) {
        return request.getAttachment(ServiceRequestContext.ATTR_IP);
    }

    @Override
    protected String getDevice(Invocation request) {
        return request.getAttachment(ServiceRequestContext.DEVICE);
    }
}
