package org.zj2.lite.service.configure.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.context.ZContexts;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.RequestContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * WebContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/27 22:26
 */
@Component
@Order(-99999)
public class WebContextFilter implements Filter {
    private static final String[] IP_HEADERS = {"x-forwarded-for", "X-Forwarded-Host", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            initRequestContext(request);
            chain.doFilter(request, response);
        } finally {
            ZContexts.clearContext();
        }
    }

    private void initRequestContext(ServletRequest request) {
        ServletRequestContext requestContext = new ServletRequestContext(request);
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            requestContext.setRootUri(httpRequest.getHeader(ServiceConstants.REQUEST_ROOT_URI));
            requestContext.setMethod(httpRequest.getMethod());
            requestContext.setUri(httpRequest.getRequestURI());
            requestContext.setAttrIp(getAttrIp(httpRequest));
            requestContext.setDevice(httpRequest.getHeader(ServiceConstants.REQUEST_DEVICE));
        }
        RequestContext.set(requestContext);
    }

    private String getAttrIp(HttpServletRequest request) {
        for(String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if(StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase("unknown", ip)) { return ip; }
        }
        return request.getRemoteAddr();
    }
}
