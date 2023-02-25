package org.zj2.common.wx.app.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.common.wx.WXBaseResp;

/**
 * WXAccessTokenResp
 *
 * @author peijie.ye
 * @date 2023/2/23 22:59
 */
@Getter
@Setter
@NoArgsConstructor
public class WXAccessTokenResp extends WXBaseResp {
    private static final long serialVersionUID = 4600919025119299185L;
    //    {"access_token":"ACCESS_TOKEN","expires_in":7200}
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
