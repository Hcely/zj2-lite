package org.zj2.common.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * WXClient
 *
 * @author peijie.ye
 * @date 2023/2/22 15:36
 */
public class WXClient {
    @Autowired
    protected RestTemplate restTemplate;

    protected String appId;

}
