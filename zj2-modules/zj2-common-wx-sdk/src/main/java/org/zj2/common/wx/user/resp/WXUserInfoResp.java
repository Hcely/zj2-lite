package org.zj2.common.wx.user.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.common.wx.WXBaseResp;
import org.zj2.lite.service.entity.StrList;

/**
 * WXUserInfoResp
 * {
 * "openid": "OPENID",
 * "nickname": NICKNAME,
 * "sex": 1,
 * "province":"PROVINCE",
 * "city":"CITY",
 * "country":"COUNTRY",
 * "headimgurl":"https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
 * "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
 * }
 *
 * @author peijie.ye
 * @date 2023/2/26 20:59
 */
@Getter
@Setter
@NoArgsConstructor
public class WXUserInfoResp extends WXBaseResp {
    private static final long serialVersionUID = 6962192132156875347L;
    @JsonProperty("openid")
    private String openid;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("headimgurl")
    private String headimgurl;
    @JsonProperty("privilege")
    private StrList privilege;
    @JsonProperty("unionid")
    private String unionid;
}
