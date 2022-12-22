package org.zj2.lite.service.configure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.zj2.lite.util.CryptUtil;

import javax.annotation.PostConstruct;

/**
 *  ZJCryptConfiguration
 *
 * @author peijie.ye
 * @date 2022/12/21 16:08
 */
@Configuration
public class ZJCryptConfiguration {
    @Value("${zj2.crypt.secret:}")
    private String cryptSecret;

    @PostConstruct
    public void init() {
        if (StringUtils.isNotEmpty(cryptSecret)) {CryptUtil.setDefCryptProvider(cryptSecret);}
    }
}
