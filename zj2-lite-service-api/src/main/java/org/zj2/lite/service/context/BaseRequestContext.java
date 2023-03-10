package org.zj2.lite.service.context;

import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
public class BaseRequestContext extends RequestContext {
    protected Map<String, Object> params;

    public BaseRequestContext(RequestContext context) {
        this.rootUri = context.rootUri;
        this.method = context.method;
        this.uri = context.uri;
        this.attrIp = context.attrIp;
        this.device = context.device;
    }

    @Override
    public void setRootUri(String rootUri) {
        super.setRootUri(rootUri);
    }

    @Override
    public void setMethod(String method) {
        super.setMethod(method);
    }

    @Override
    public void setUri(String uri) {
        super.setUri(uri);
    }

    @Override
    public void setAttrIp(String attrIp) {
        super.setAttrIp(attrIp);
    }

    @Override
    public void setDevice(String device) {
        super.setDevice(device);
    }

    @Override
    public Object rawRequest() {
        return null;
    }

    @Override
    public Object getRequestParam(String key) {
        return params == null ? null : params.get(key);
    }

    @Override
    public void setRequestParam(String key, Object param) {
        if(param == null) {
            if(params != null) { params.remove(key); }
        } else {
            if(params == null) { params = new LinkedHashMap<>(); }
            params.put(key, param);
        }
    }
}