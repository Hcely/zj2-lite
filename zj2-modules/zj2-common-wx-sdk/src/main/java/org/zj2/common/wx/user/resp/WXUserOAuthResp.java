package org.zj2.common.wx.user.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.common.wx.WXBaseResp;

/**
 * UserOAuthResp
 * {
 * "access_token":"ACCESS_TOKEN",
 * "expires_in":7200,
 * "refresh_token":"REFRESH_TOKEN",
 * "openid":"OPENID",
 * "scope":"SCOPE"
 * }
 *
 * @author peijie.ye
 * @date 2023/2/26 20:03
 */
@Getter
@Setter
@NoArgsConstructor
public class WXUserOAuthResp extends WXBaseResp {
    private static final long serialVersionUID = -3410254398615085854L;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("openid")
    private String openid;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("is_snapshotuser")
    private Integer isSnapshotUser;
    @JsonProperty("unionid")
    private String unionid;
}
