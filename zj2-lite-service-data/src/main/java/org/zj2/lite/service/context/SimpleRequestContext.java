package org.zj2.lite.service.context;

public class SimpleRequestContext extends RequestContext {
    @Override
    public Object request() {
        return null;
    }

    @Override
    public Object getRequestParam(String key) {
        return null;
    }

    @Override
    public void setRequestParam(String key, Object param) {
        // NOTHING
    }
}