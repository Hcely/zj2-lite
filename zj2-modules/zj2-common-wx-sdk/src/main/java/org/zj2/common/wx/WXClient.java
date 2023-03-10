package org.zj2.common.wx;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.zj2.common.wx.app.WXAppManager;
import org.zj2.lite.util.ZRBuilder;

import java.util.Map;

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
        if(StringUtils.isEmpty(accessToken)) {
            throw ZRBuilder.failureErr("缺失WX AccessToken");
        }
        return accessToken;
    }

    protected <T extends WXBaseResp> T doGet(Class<T> respType, String url, Map<String, Object> params) {
        T resp = restTemplate.getForObject(url, respType, params);
        return handleResponse(resp);
    }

    protected <T extends WXBaseResp> T doPost(Class<T> respType, String url, Map<String, Object> params) {
        T resp = restTemplate.postForObject(url, params, respType);
        return handleResponse(resp);
    }

    private <T extends WXBaseResp> T handleResponse(T resp) {
        if(resp == null) { throw ZRBuilder.failureErr("NO WX Response"); }
        if(!resp.isSuccess()) { throw ZRBuilder.failureErr(resp.getErrmsg()); }
        return resp;
    }

}
