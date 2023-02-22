package org.zj2.common.wx.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * WXAccessToken
 *
 * @author peijie.ye
 * @date 2023/2/22 8:43
 */
@Getter
@Setter
@NoArgsConstructor
public class WXAccessToken implements Serializable {
    private static final long serialVersionUID = -4970545663149657595L;
    private String wxAppId;
    private String accessToken;
    private long createTime;
    private long expireTime;
}
