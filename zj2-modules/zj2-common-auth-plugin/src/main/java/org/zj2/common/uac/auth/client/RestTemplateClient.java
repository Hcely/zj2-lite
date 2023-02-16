package org.zj2.common.uac.auth.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * AuthRestTemplate
 *
 * @author peijie.ye
 * @date 2023/2/10 17:49
 */
@Component
public class RestTemplateClient extends RestTemplate implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        // 追加初始化
        List<ClientHttpRequestInitializer> initializers = getClientHttpRequestInitializers();
        initializers.add(new RestTemplateRequestContextInitializer());
    }
}
