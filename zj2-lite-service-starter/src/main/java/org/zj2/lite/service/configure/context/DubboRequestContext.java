package org.zj2.lite.service.configure.context;

import lombok.Getter;
import org.apache.dubbo.rpc.Invocation;
import org.zj2.lite.service.constant.RequestMethods;
import org.zj2.lite.service.context.RequestContext;

/**
 * DubboRequestContext
 *
 * @author peijie.ye
 * @date 2023/2/10 14:20
 */
public class DubboRequestContext extends RequestContext {
    @Getter
    protected final Invocation request;

    protected DubboRequestContext(Invocation request) {
        this.request = request;
        this.method = RequestMethods.DUBBO;
    }

    @Override
    protected void setRootUri(String rootUri) {
        super.setRootUri(rootUri);
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
        return request.getAttachment(key);
    }

    @Override
    public void setRequestParam(String key, Object param) {
        request.setAttachment(key, param);
    }
}
