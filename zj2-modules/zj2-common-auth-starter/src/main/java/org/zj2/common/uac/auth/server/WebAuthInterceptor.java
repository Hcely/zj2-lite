package org.zj2.common.uac.auth.server;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.util.UriResourceManager;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;

/**
 * AuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Aspect
@Component
public class WebAuthInterceptor extends AbstractAuthInterceptor {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
    private void pointcut() {
        // not thing
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {// NOSONAR
        RequestContext requestContext = RequestContext.current();
        AuthContext authContext = initAuthContext(requestContext);
        if (authContext != null) {
            authContext.setUriResource(UriResourceManager.get(joinPoint));
            authenticate(requestContext, authContext);// 认证
            authorizeBefore(requestContext, authContext);// 授权
        }
        Object result = joinPoint.proceed();
        if (authContext != null) { authorizeAfter(requestContext, authContext, result); }
        return result;
    }
}

