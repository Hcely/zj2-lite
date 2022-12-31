package org.zj2.common.uac.auth.request;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 *  WebContextTemplateInterceptor
 *
 * @author peijie.ye
 * @date 2022/12/9 1:57
 */
public class WebContextTemplateInterceptor extends AbsContextInterceptor<HttpRequest>
        implements ClientHttpRequestInterceptor {
    @SuppressWarnings("NullableProblems")
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        String uri = request.getURI().getPath();
        String method = StringUtils.upperCase(request.getMethodValue());
        setContext(request, method, uri);
        return execution.execute(request, body);
    }

    @Override
    protected void setValue(HttpRequest request, String key, String value) {
        request.getHeaders().add(key, value);
    }
}
