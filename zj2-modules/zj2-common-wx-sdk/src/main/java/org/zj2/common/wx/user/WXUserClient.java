package org.zj2.common.wx.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.zj2.common.wx.WXClient;
import org.zj2.common.wx.app.WXApp;
import org.zj2.common.wx.app.WXAppManager;
import org.zj2.common.wx.app.resp.WXAccessTokenResp;
import org.zj2.common.wx.user.resp.WXUserOAuthResp;
import org.zj2.lite.util.ZRBuilder;

import java.util.Map;

/**
 * WXUserClient
 *
 * @author peijie.ye
 * @date 2023/2/22 15:35
 */
public class WXUserClient extends WXClient {
    private static final String SNS_OAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public WXUserClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public WXUserOAuthResp oauth(String appid, String code) {
        WXApp app = WXAppManager.getApp(appid);
        if (app == null) { throw ZRBuilder.failureErr("缺失WX app"); }
        ResponseEntity<WXUserOAuthResp> response = restTemplate.getForEntity(SNS_OAUTH_URL, WXUserOAuthResp.class,
                Map.of("appid", appid, "secret", app.getWxAppSecret(), "code", code, "grant_type",
                        "authorization_code"));
        return response.getBody();
    }
}
