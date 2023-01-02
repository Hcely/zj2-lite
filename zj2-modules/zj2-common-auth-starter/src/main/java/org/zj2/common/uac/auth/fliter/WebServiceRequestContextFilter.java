package org.zj2.common.uac.auth.fliter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.zj2.lite.service.context.ServiceRequestContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *  AuthenticationWebFilter
 *
 * @author peijie.ye
 * @date 2022/12/4 14:25
 */
@Component
@Order(-2000)
public class WebServiceRequestContextFilter extends AbsContextFilter<HttpServletRequest> implements Filter {
    private static final String[] IP_HEADERS = {"x-forwarded-for", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String uri = httpServletRequest.getRequestURI();
            String method = StringUtils.upperCase(httpServletRequest.getMethod());
            setContext(httpServletRequest, method, uri);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected String getValue(HttpServletRequest request, String key) {
        String value = request.getHeader(key);
        if (StringUtils.isEmpty(value) && ServiceRequestContext.AUTHORIZATION.equalsIgnoreCase(key)
                && HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            value = request.getParameter(key);
            if (StringUtils.isEmpty(value)) {value = request.getParameter(StringUtils.lowerCase(key));}
        }
        return value;
    }

    @Override
    protected String getAttrIp(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase("unknown", ip)) {return ip;}
        }
        return request.getRemoteAddr();
    }

    @Override
    protected String getDevice(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
