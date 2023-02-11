package org.zj2.lite.service.configure.context;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.RequestContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequestAttrView
 *
 * @author peijie.ye
 * @date 2023/2/10 14:20
 */
public class ServletRequestContext extends RequestContext {
    @Getter
    protected final ServletRequest request;

    protected ServletRequestContext(ServletRequest request) {
        this.request = request;
    }

    @Override
    public void setRootUri(String rootUri) {
        this.rootUri = rootUri;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void setAttrIp(String attrIp) {
        this.attrIp = attrIp;
    }

    @Override
    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public Object request() {
        return request;
    }

    @Override
    public Object getRequestParam(String key) {
        Object value = request.getAttribute(key);
        if (value != null) { return value; }
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String valueStr = httpRequest.getHeader(key);
            if (StringUtils.isNotEmpty(valueStr)) { return valueStr; }
            if (StringUtils.equalsIgnoreCase(key, ServiceConstants.AUTHORIZATION) && !StringUtils.equalsIgnoreCase(
                    HttpMethod.GET.name(), httpRequest.getMethod())) {
                valueStr = httpRequest.getParameter(ServiceConstants.AUTHORIZATION);
                if (StringUtils.isNotEmpty(valueStr)) { return valueStr; }
            }
        }
        return null;
    }

    @Override
    public void setRequestParam(String key, Object param) {
        request.setAttribute(key, param);
    }
}
