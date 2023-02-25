package org.zj2.common.wx.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zj2.common.wx.WXClient;
import org.zj2.common.wx.app.resp.WXAccessTokenResp;

import java.util.Map;

/**
 * WXAppClient
 *
 * @author peijie.ye
 * @date 2023/2/23 14:21
 */
@Component
public class WXAppClient extends WXClient {
    private static final String GRANT_TYPE = "client_credential";
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    public WXAccessTokenResp getAccessToken(String wxAppId, String wxAppSecret) {
        ResponseEntity<WXAccessTokenResp> response = restTemplate.getForEntity(GET_ACCESS_TOKEN_URL,
                WXAccessTokenResp.class, Map.of("grant_type", GRANT_TYPE, "appid", wxAppId, "secret", wxAppSecret));
        return response.getBody();
    }


}
