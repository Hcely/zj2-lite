package org.zj2.common.wx;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.zj2.common.wx.app.WXAppManager;
import org.zj2.lite.util.ZRBuilder;

/**
 * WXClient
 *
 * @author peijie.ye
 * @date 2023/2/22 15:36
 */
public class WXClient {
    protected final RestTemplate restTemplate;

    public WXClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected String getAppAccessToken(String wxAppId) {
        String accessToken = WXAppManager.getWXAccessToken(wxAppId);
        if (StringUtils.isEmpty(accessToken)) {
            throw ZRBuilder.failureErr("缺失WX AccessToken");
        }
        return accessToken;
    }
}
