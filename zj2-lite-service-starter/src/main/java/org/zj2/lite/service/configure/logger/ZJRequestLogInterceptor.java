package org.zj2.lite.service.configure.logger;

import org.apache.commons.text.TextStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(ZJRequestLogInterceptor.class);

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
            if (context != null) {logRequest(context);}
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if (context != null) {
                context.setResponse(error == null ? 200 : 500, result, error);
                logResponse(joinPoint, context);
            }
        }
    }

    private RequestLogContext getContext() {
        RequestLogContext context = RequestLogContext.current();
        if (context != null && context.getLogState() == RequestLogContext.INITIALIZED) {
            context.setLogState(RequestLogContext.REQUESTING);
            return context;
        } else {
            return null;
        }
    }

    private void logRequest(RequestLogContext context) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(192);
        sb.append("http req[").append(context.getMethod()).append("]-").append(context.getUri());
        String message = sb.toString();
        log.info(message);
    }

    private void logResponse(ProceedingJoinPoint joinPoint, RequestLogContext context) {
        long take = context.getEndTime() - context.getStartTime();
        TextStringBuilder sb = new TextStringBuilder(256);
        sb.append("http resp[").append(context.getMethod()).append("]-").append(context.getUri()).append('(')
                .append(take).append("ms)");
        final Object result = context.getResult();
        final Throwable error = context.getError();
        boolean resultError = false;
        if (error != null) {
            if (error instanceof ZStatusMsg) {
                appendStatusMsg(sb, (ZStatusMsg) error);
            } else {
                resultError = true;
                sb.ensureCapacity(1024 * 4);
                sb.append(",reqParams:[");
                appendArgs(sb, joinPoint.getArgs());
                sb.append(']');
            }
        } else if (result == null) {
            sb.append(",null result");
        } else if (result instanceof ZStatusMsg) {
            appendStatusMsg(sb, (ZStatusMsg) result);
        } else if (BeanUtils.isSimpleValueType(result.getClass())) {
            sb.append(",result:").append(result);
        } else {
            sb.append(",unknown result(").append(result.getClass().getSimpleName()).append(')');
        }
        String message = sb.toString();
        if (resultError) {
            log.error(message, error);
        } else {
            log.info(message);
        }
    }

    private void appendStatusMsg(TextStringBuilder sb, ZStatusMsg statusMsg) {
        sb.append(",success:").append(statusMsg.isSuccess());
        sb.append(",status:").append(statusMsg.getStatus());
        sb.append(",msg:").append(statusMsg.getMsg());
    }

    private void appendArgs(TextStringBuilder sb, Object[] args) {
        if (args == null || args.length == 0) {return;}
        int i = 0;
        for (Object arg : args) {
            if (++i > 1) {sb.append(',');}
            if (arg == null) {
                sb.append("null");
            } else if (BeanUtils.isSimpleValueType(arg.getClass())) {
                sb.append(arg);
            } else if (arg instanceof ServletRequest) {
                sb.append("ServletRequest");
            } else if (arg instanceof ServletResponse) {
                sb.append("ServletResponse");
            } else if (arg instanceof InputStreamSource) {
                sb.append("StreamSource");
            } else {
                sb.append(SafeLogUtil.toJSONStr(arg));
            }
        }
    }
}
