package org.zj2.common.uac.wx.access.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WXApp
 *
 * @author peijie.ye
 * @date 2023/2/17 1:04
 */
@Getter
@Setter
@NoArgsConstructor
public class WXApp {
    private String wxAppName;
    private String wxAppId;
    private String wxAppSecret;
}
