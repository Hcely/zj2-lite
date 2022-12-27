package org.zj2.lite.service.configure.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
    private final Logger log = LoggerFactory.getLogger(ZJRequestLogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Throwable error = null;
        RequestLogContext context = null;
        try {
            context = setContext(request);
            chain.doFilter(request, response);
        } catch (Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if (context != null && context.getLogState() != RequestLogContext.RESPONSE) {
                final int responseStatus = getResponseStatus(response);
                context.setResponse(responseStatus, null, error);
                logResult(context);
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

    private void logResult(RequestLogContext context) {
        long take = context.getEndTime() - context.getStartTime();
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(192);
        sb.append("http resp[").append(context.getMethod()).append("]-").append(context.getUri()).append('(')
                .append(take).append("ms) status:").append(context.getResponseStatus());
        String message = sb.toString();
        if (context.getError() == null) {
            log.info(message);
        } else {
            log.error(message, context.getError());
        }
    }
}
