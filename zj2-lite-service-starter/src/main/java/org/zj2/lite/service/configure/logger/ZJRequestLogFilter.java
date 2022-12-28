package org.zj2.lite.service.configure.logger;

import org.apache.commons.text.TextStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.entity.result.ZStatusMsg;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  ZJRequestLogFilter
 *
 * @author peijie.ye
 * @date 2022/12/27 22:06
 */
@Component
@Order(-10000)
public class ZJRequestLogFilter implements Filter {
    private static final long SHOW_RESPONSE_THRESHOLD = 5000;
    private final Logger log = LoggerFactory.getLogger(ZJRequestLogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Throwable error = null;
        final RequestLogContext context = setContext(request);
        try {
            logRequest(context);
            chain.doFilter(request, response);
        } catch (Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if (context.getLogState() != RequestLogContext.STATE_COMPLETED) {
                if (context.getLogState() != RequestLogContext.STATE_RESPONSE) {context.response(null, null, error);}
                final int responseStatus = getResponseStatus(response);
                context.completed(responseStatus);
                logResponse(context);
            }
        }
    }

    private RequestLogContext setContext(ServletRequest request) {
        String method = null;
        String uri = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            method = httpReq.getMethod();
            uri = httpReq.getRequestURI();
        }
        return RequestLogContext.setContext(method, uri);
    }

    private int getResponseStatus(ServletResponse response) {
        if (response instanceof HttpServletResponse) {
            return ((HttpServletResponse) response).getStatus();
        }
        return -1;
    }

    private void logRequest(RequestLogContext context) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(192);
        sb.append("http req[").append(context.getMethod()).append("]-").append(context.getUri());
        String message = sb.toString();
        log.info(message);
    }

    private void logResponse(RequestLogContext context) {
        long take = context.getEndTime() - context.getStartTime();
        long executeTake = context.getExecuteEndTime() - context.getExecuteStartTime();
        TextStringBuilder sb = new TextStringBuilder(256);
        sb.append("http resp[").append(context.getMethod()).append("]-").append(context.getUri()).append('(')
                .append(take).append('|').append(executeTake);
        sb.append(executeTake > SHOW_RESPONSE_THRESHOLD ? "ms SLOW)" : "ms)");
        sb.append(",resp:").append(context.getResponseStatus());
        final Object result = context.getResult();
        final Throwable error = context.getError();
        final Object[] params = context.getParams();
        boolean resultError = false;
        if (error != null) {
            if (error instanceof ZStatusMsg) {
                appendStatusMsg(sb, (ZStatusMsg) error);
            } else {
                resultError = true;
                appendParams(sb, params);
            }
        } else if (result == null) {
            sb.append(",result:null");
        } else if (result instanceof ZStatusMsg) {
            appendStatusMsg(sb, (ZStatusMsg) result);
        } else if (BeanUtils.isSimpleValueType(result.getClass())) {
            sb.append(",result:").append(result);
        } else {
            sb.append(",result:unknown(").append(result.getClass().getSimpleName()).append(')');
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

    private void appendParams(TextStringBuilder sb, Object[] params) {
        sb.append(",reqParams:[");
        if (params != null && params.length > 0) {
            int i = 0;
            sb.ensureCapacity(1024 * 4);
            for (Object arg : params) {
                if (++i > 1) {sb.append(',');}
                sb.append(arg);
            }
        }
        sb.append(']');
    }
}
