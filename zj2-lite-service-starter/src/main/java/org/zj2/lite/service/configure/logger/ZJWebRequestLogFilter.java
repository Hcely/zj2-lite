package org.zj2.lite.service.configure.logger;

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
public class ZJWebRequestLogFilter extends AbstractRequestLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Throwable error = null;
        final RequestLogContext context = getContext(request);
        try {
            logRequest(context);
            chain.doFilter(request, response);
        } catch (Throwable e) {//NOSONAR
            error = e;
            throw e;
        } finally {
            if (context.getLogState() != RequestLogContext.STATE_COMPLETED) {
                if (context.getLogState() != RequestLogContext.STATE_RESPONSE) { context.response(null, null, error); }
                final int responseStatus = getResponseStatus(response);
                context.completed(responseStatus);
                logResponse(context);
            }
        }
    }

    private RequestLogContext getContext(ServletRequest request) {
        String method = null;
        String uri = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            method = httpReq.getMethod();
            uri = httpReq.getRequestURI();
        }
        return RequestLogContext.setContext("HTTP", method, uri);
    }

    private int getResponseStatus(ServletResponse response) {
        if (response instanceof HttpServletResponse) {
            return ((HttpServletResponse) response).getStatus();
        }
        return -1;
    }
}
