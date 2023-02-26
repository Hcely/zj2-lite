package org.zj2.common.wx.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WXApp
 *
 * @author peijie.ye
 * @date 2023/2/19 21:17
 */
@Getter
@Setter
@NoArgsConstructor
public class WXApp {
    private String wxAppName;
    private String wxAppId;
    private String wxAppSecret;
    private String aesSecret;


}
