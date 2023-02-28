package org.zj2.common.wx.message;

import org.springframework.web.client.RestTemplate;
import org.zj2.common.wx.WXClient;

/**
 * WXMessageClient
 *
 * @author peijie.ye
 * @date 2023/2/28 21:58
 */
public class WXMessageClient extends WXClient {
    public WXMessageClient(RestTemplate restTemplate) {
        super(restTemplate);
    }
}
