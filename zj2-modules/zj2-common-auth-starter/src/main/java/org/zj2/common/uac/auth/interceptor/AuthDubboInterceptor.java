package org.zj2.common.uac.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * DubboAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Activate(group = CommonConstants.PROVIDER, order = -1999)
public class AuthDubboInterceptor extends AbstractAuthInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        ServiceRequestContext requestContext = ServiceRequestContext.current();
        if (!requestContext.isFiltered()) {
            authenticateSign(requestContext, AuthenticationContext.current());
        }
        return invoker.invoke(invocation);
    }
}

