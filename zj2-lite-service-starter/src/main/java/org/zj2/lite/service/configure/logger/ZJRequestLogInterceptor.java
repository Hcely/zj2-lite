package org.zj2.lite.service.configure.logger;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.entity.result.ZStatusMsg;
import org.zj2.lite.service.util.SafeLogUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *  ZJWebLogIn
 *
 * @author peijie.ye
 * @date 2022/12/26 19:12
 */
@Aspect
@Component
public class ZJRequestLogInterceptor {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
    private void pointcut() {
        // not thing
    }

    @Around("pointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {// NOSONAR
        Throwable error = null;
        RequestLogContext context = null;
        Object result = null;
        try {
            context = getContext();
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if (context != null) {
                setResponse(context, joinPoint, result, error);
            }
        }
    }

    private RequestLogContext getContext() {
        RequestLogContext context = RequestLogContext.current();
        if (context != null && context.request()) {
            return context;
        } else {
            return null;
        }
    }

    private void setResponse(RequestLogContext context, ProceedingJoinPoint joinPoint, Object result, Throwable error) {
        Object[] params = error != null && !(error instanceof ZStatusMsg) ? buildArgs(joinPoint.getArgs()) : null;
        context.response(result, params, error);
    }

    private Object[] buildArgs(Object[] args) {
        if (args == null || args.length == 0) { return ArrayUtils.EMPTY_OBJECT_ARRAY; }
        int i = 0;
        Object[] params = new Object[args.length];
        for (Object arg : args) {
            if (arg == null) {
                params[i] = "null";
            } else if (BeanUtils.isSimpleValueType(arg.getClass())) {
                params[i] = (arg);
            } else if (arg instanceof ServletRequest) {
                params[i] = "ServletRequest";
            } else if (arg instanceof ServletResponse) {
                params[i] = "ServletResponse";
            } else if (arg instanceof InputStreamSource) {
                params[i] = "StreamSource";
            } else {
                params[i] = SafeLogUtil.toJSONStr(arg);
            }
            ++i;
        }
        return params;
    }
}
