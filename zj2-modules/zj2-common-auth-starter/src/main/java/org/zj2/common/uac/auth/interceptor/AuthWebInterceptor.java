package org.zj2.common.uac.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.zj2.common.uac.auth.authorize.AuthorizeBeanPropertyHandler;
import org.zj2.common.uac.auth.authorize.AuthorizeUriHandler;
import org.zj2.common.uac.auth.util.AuthUtil;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.auth.AuthenticationIgnored;
import org.zj2.lite.service.auth.AuthenticationRequired;
import org.zj2.lite.service.auth.AuthorityResource;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.service.context.TokenType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *  AuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Aspect
@Component
public class AuthWebInterceptor extends AbstractAuthInterceptor {
    private static final String ZJ2_PACKAGE = "org.zj2";
    private static final TokenType[] DEFAULT_TOKEN_TYPE = {TokenType.JWT};
    @Autowired
    private AuthorizeUriHandler authorizeUriHandler;
    @Autowired
    private AuthorizeBeanPropertyHandler authorizeBeanPropertyHandler;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
    private void pointcut() {
        // not thing
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {// NOSONAR
        final ServiceRequestContext context = ServiceRequestContext.current();
        final boolean filtered = !context.isFiltered();
        if (filtered) {
            context.setFiltered(true);
            final Method method = getMethod(joinPoint);
            authenticate(context, method);// 认证
            authoriseMethod(context, method);// 授权
        }
        Object result = joinPoint.proceed();
        if (filtered) { authoriseProperties(context, result); }
        return result;
    }

    private void authenticate(ServiceRequestContext context, Method method) {
        if (method == null) { return; }
        Class<?> type = method.getDeclaringClass();
        // 无需认证
        if (method.getAnnotation(AuthenticationIgnored.class) != null) { return; }
        AuthenticationRequired required = method.getAnnotation(AuthenticationRequired.class);
        if (required == null) {
            // 没指定认证，父级指定无需认证
            if (method.getDeclaringClass().getAnnotation(AuthenticationIgnored.class) != null) {
                return;
            }
            // 读取父级的认证要求
            required = method.getDeclaringClass().getAnnotation(AuthenticationRequired.class);
        }
        if (required == null) {
            // 非本身服务，无需处理
            if (!StringUtils.startsWithIgnoreCase(type.getName(), ZJ2_PACKAGE)) { return; }
        }
        TokenType[] types = required == null ? DEFAULT_TOKEN_TYPE : required.allowToken();
        authenticate(context, types.length == 0 ? DEFAULT_TOKEN_TYPE : types);
    }

    protected void authenticate(ServiceRequestContext context, TokenType[] types) {
        if (StringUtils.isEmpty(context.getToken())) { throw AuthUtil.unAuthenticationErr("缺失认证信息"); }
        TokenType type = context.getTokenType();
        if (!CollUtil.contains(types, type)) { throw AuthUtil.unAuthenticationErr("非法认证信息"); }
        if (type == TokenType.JWT) {
            authenticateJWT(context);
        } else {
            authenticateSign(context);
        }
        context.setAuthenticated(true);
    }

    protected void authoriseMethod(ServiceRequestContext context, Method method) {
        if (method == null) { return; }
        if (!context.isAuthenticated()) { return; }
        if (context.getTokenType() != TokenType.JWT) { return; }
        AuthorityResource resource = method.getAnnotation(AuthorityResource.class);
        if (resource == null) {
            resource = method.getDeclaringClass().getAnnotation(AuthorityResource.class);
            if (resource == null) { return; }
        }
        final String authorityResource = StrUtil.formatObj(resource.value(), getPathParams(context));
        authorizeUriHandler.authorize(authorityResource);
    }

    protected void authoriseProperties(ServiceRequestContext context, Object data) {
        if (context.isAuthenticated() && context.getTokenType() == TokenType.JWT) {
            authorizeBeanPropertyHandler.authorize(data);
        }
    }


    private static Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        return signature instanceof MethodSignature ? ((MethodSignature) signature).getMethod() : null;
    }

    private static Map<String, String> getPathParams(ServiceRequestContext requestContext) {
        Object request = requestContext.getRequest();
        if (request instanceof HttpServletRequest) {
            Object pathParams = ((HttpServletRequest) request).getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            //noinspection unchecked
            return pathParams instanceof Map ? (Map<String, String>) pathParams : CollUtil.emptyMap();
        }
        return CollUtil.emptyMap();
    }
}

