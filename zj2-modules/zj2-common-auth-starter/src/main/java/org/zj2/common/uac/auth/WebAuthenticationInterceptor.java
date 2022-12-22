package org.zj2.common.uac.auth;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.annotation.AuthenticationIgnored;
import org.zj2.common.uac.auth.annotation.AuthenticationRequired;
import org.zj2.common.uac.auth.service.JWTokenApi;
import org.zj2.common.uac.auth.util.AppUtil;
import org.zj2.common.uac.auth.util.JWTUtil;
import org.zj2.common.uac.auth.util.ServerSignUtil;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.service.context.TokenType;

import java.lang.reflect.Method;

/**
 *  AuthenticationInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 2:19
 */
@Aspect
@Component
public class WebAuthenticationInterceptor {
    private static final String ZJ2_PACKAGE = "org.zj2";
    private static final long SIGN_TIMEOUT = 60000L * 5;
    private static final TokenType[] DEFAULT_TOKEN_TYPE = {TokenType.CLIENT};
    @Autowired
    private JWTokenApi jwtokenApi;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
    private void pointcut() {
        // not thing
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {// NOSONAR
        authenticate(joinPoint);
        return joinPoint.proceed();
    }

    private void authenticate(ProceedingJoinPoint joinPoint) {
        // 已认证，无效处理
        if (ServiceRequestContext.currentAuthenticated()) {return;}
        Method method = getMethod(joinPoint);
        // 没找到方法忽略
        if (method == null) {return;}
        //
        Class<?> type = method.getDeclaringClass();
        // 无需认证
        if (method.getAnnotation(AuthenticationIgnored.class) != null) {
            return;
        }
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
            if (!StringUtils.startsWithIgnoreCase(type.getName(), ZJ2_PACKAGE)) {return;}
        }
        TokenType[] types = required == null ? DEFAULT_TOKEN_TYPE : required.required();
        authenticate(types.length == 0 ? DEFAULT_TOKEN_TYPE : types);
    }

    protected void authenticate(TokenType[] types) {
        ServiceRequestContext context = ServiceRequestContext.currentContext();
        if (StringUtils.isEmpty(context.getToken())) {throw unAuthenticationErr("缺失认证信息");}
        TokenType type = context.getTokenType();
        if (!CollUtil.contains(types, type)) {throw unAuthenticationErr("非法认证信息");}
        if (type == TokenType.CLIENT) {
            authenticateJWT(context);
        } else {
            authenticateSign(context);
        }
        context.setAuthenticated(true);
    }

    protected void authenticateJWT(ServiceRequestContext context) {
        if (context.getTokenTime() < System.currentTimeMillis() - 1000) {
            throw unAuthenticationErr("Token过期");
        }
        AppDTO app = getApp();
        if (!JWTUtil.valid(app.getAppSecret(), context.getToken())) {
            throw unAuthenticationErr("Token无效");
        }
        if (StringUtils.isNotEmpty(context.getNamespace())) {
            String errMsg = jwtokenApi.validToken(app.getAppCode(), AuthenticationContext.currentUserId(),
                    context.getNamespace(), context.getToken());
            if (StringUtils.isNotEmpty(errMsg)) {throw unAuthenticationErr(errMsg);}
        }
    }

    protected void authenticateSign(ServiceRequestContext context) {
        long timestamp = context.getTokenTime();
        long now = System.currentTimeMillis();
        if (timestamp > now - 1000 || timestamp < now - SIGN_TIMEOUT) {throw unAuthenticationErr("请求过期");}
        AppDTO app = getApp();
        String sign = ServerSignUtil.buildSign(app.getAppCode(), app.getAppSecret(), context.getTokenTime(),
                context.getMethod(), context.getUri());
        if (!StringUtils.equalsIgnoreCase(sign, context.getToken())) {throw unAuthenticationErr("非法请求");}
    }

    protected AppDTO getApp() {
        AppDTO app = AppUtil.getApp();
        if (app == null || BooleanUtil.isFalse(app.getEnableFlag())) {throw unAuthenticationErr("请求应用无效");}
        return app;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        return signature instanceof MethodSignature ? ((MethodSignature) signature).getMethod() : null;
    }

    private ZError unAuthenticationErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(401);
    }

}

