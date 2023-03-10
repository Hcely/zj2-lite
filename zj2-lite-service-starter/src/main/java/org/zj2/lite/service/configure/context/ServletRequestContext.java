package org.zj2.lite.service.configure.context;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.BaseRequestContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequestAttrView
 *
 * @author peijie.ye
 * @date 2023/2/10 14:20
 */
public class ServletRequestContext extends BaseRequestContext {
    @Getter
    protected final ServletRequest request;

    protected ServletRequestContext(ServletRequest request) {
        this.request = request;
    }

    @Override
    public Object rawRequest() {
        return request;
    }

    @Override
    public Object getRequestParam(String key) {
        Object value = super.getRequestParam(key);
        if(value != null) { return value; }
        //
        value = request.getAttribute(key);
        if(value != null) { return value; }
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String valueStr = httpRequest.getHeader(key);
            if(StringUtils.isNotEmpty(valueStr)) { return valueStr; }
            if(StringUtils.equalsIgnoreCase(key, ServiceConstants.AUTHORIZATION)) {
                return getAuthorization(httpRequest);
            }
        }
        return null;
    }

    private String getAuthorization(HttpServletRequest httpRequest) {
        Cookie[] cookies = httpRequest.getCookies();
        String value = null;
        if(CollUtil.isNotEmpty(cookies)) {
            for(Cookie c : cookies) {
                if(StringUtils.equalsIgnoreCase(c.getName(), ServiceConstants.AUTHORIZATION)) {
                    value = c.getValue();
                    break;
                }
            }
            if(StringUtils.isNotEmpty(value)) { return value; }
        }
        if(!StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), httpRequest.getMethod())) {
            value = httpRequest.getParameter(ServiceConstants.AUTHORIZATION);
            if(StringUtils.isNotEmpty(value)) { return value; }
        }
        return null;
    }
}
