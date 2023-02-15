package org.zj2.lite.service.configure.context;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.lite.common.context.ZContexts;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.util.ServiceUriUtil;

@Activate(group = CommonConstants.PROVIDER, order = -99999)
public class DubboContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            initRequestContext(invoker, invocation);
            return invoker.invoke(invocation);
        } finally {
            ZContexts.clearContext();
        }
    }

    private void initRequestContext(Invoker<?> invoker, Invocation invocation) {
        DubboRequestContext requestContext = new DubboRequestContext(invoker, invocation);
        requestContext.setUri(ServiceUriUtil.getMethodName(invoker.getInterface(), invocation.getMethodName(),
                invocation.getParameterTypes()));
        requestContext.setRootUri(invocation.getAttachment(ServiceConstants.REQUEST_ROOT_URI));
        requestContext.setAttrIp(invocation.getAttachment(ServiceConstants.REQUEST_ATTR_IP));
        requestContext.setDevice(invocation.getAttachment(ServiceConstants.REQUEST_DEVICE));
        RequestContext.set(requestContext);
    }
}
