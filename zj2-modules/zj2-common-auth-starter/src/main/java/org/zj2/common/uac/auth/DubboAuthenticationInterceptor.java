package org.zj2.common.uac.auth;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.service.context.TokenType;

/**
 *  DubboAuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Activate(group = CommonConstants.PROVIDER, order = -1999)
public class DubboAuthenticationInterceptor extends AbstractAuthenticationInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        authenticate();
        return invoker.invoke(invocation);
    }

    protected void authenticate() {
        ServiceRequestContext context = ServiceRequestContext.current();
        if (StringUtils.isEmpty(context.getToken())) {throw unAuthenticationErr("缺失认证信息");}
        TokenType type = context.getTokenType();
        if (type == TokenType.JWT) {
            throw unAuthenticationErr("无效签名");
        }
        authenticateSign(context);
        context.setAuthenticated(true);
    }
}

