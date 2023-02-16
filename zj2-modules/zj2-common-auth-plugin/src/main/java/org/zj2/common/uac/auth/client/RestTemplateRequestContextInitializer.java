package org.zj2.common.uac.auth.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.stereotype.Component;

/**
 * WebContextTemplateInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 1:57
 */
@Component
@Order(-2000)
public class RestTemplateRequestContextInitializer extends AbsClientRequestSignInterceptor<HttpRequest>
        implements ClientHttpRequestInitializer {

    @Override
    protected void setValue(HttpRequest request, String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            request.getHeaders().add(key, value);
        }
    }

    @Override
    public void initialize(ClientHttpRequest request) {
        String uri = request.getURI().getPath();
        setRequestContext(request, StringUtils.upperCase(request.getMethodValue()), uri);
    }
}
