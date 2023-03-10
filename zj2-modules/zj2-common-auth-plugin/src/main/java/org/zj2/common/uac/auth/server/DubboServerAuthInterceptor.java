package org.zj2.common.uac.auth.server;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.common.uac.auth.util.UriResourceManager;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 * DubboAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Activate(group = CommonConstants.PROVIDER, order = -1999)
public class DubboServerAuthInterceptor extends AbstractAuthInterceptor implements Filter {
    private static final TokenType[] REQUIRED_TYPE = {TokenType.SERVER_SIGN};

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RequestContext requestContext = RequestContext.current();
        AuthContext authContext = initAuthContext(requestContext);
        if(authContext != null) {
            authContext.setUriResource(UriResourceManager.get(invoker.getInterface(), invocation.getMethodName(), invocation.getParameterTypes()));
            authenticate(requestContext, authContext, REQUIRED_TYPE);
            onAuthenticated(requestContext, authContext);
        }
        Result result = invoker.invoke(invocation);
        if(authContext != null) {
            onCompleted(requestContext, authContext, result == null ? null : result.getValue());
        }
        return result;
    }
}

