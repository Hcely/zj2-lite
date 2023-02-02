package org.zj2.lite.service.auth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * JWToken
 *
 * @author peijie.ye
 * @date 2022/11/27 22:25
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationJWT implements Serializable {
    private static final long serialVersionUID = 20221127222539L;
    /**
     *
     */
    @JSONField(name = "tid")
    private String tokenId;
    /**
     * 用户id
     */
    @JSONField(name = "uid")
    private String userId;

    /**
     * 用户名称
     */
    @JSONField(name = "uname")
    private String userName;

    /**
     * appCode
     */
    @JSONField(name = "app")
    private String appCode;

    /**
     * orgCode
     */
    @JSONField(name = "org")
    private String orgCode;

    /**
     * 到期时间
     */
    @JSONField(name = "iat")
    private long expireAt;

    /**
     * 唯一token
     */
    @JSONField(name = "ns")
    private String namespace;

    @JSONField(name = "at")
    private Integer authorityType;

    /**
     * 用户token
     */
    @JSONField(serialize = false, deserialize = false)
    private String token;

    @Override
    public String toString() {
        return "{userId=" + userId + ", userName=" + userName + ", appCode=" + appCode + ", orgCode=" + orgCode
                + ", expireAt=" + expireAt + ", namespace=" + namespace + '}';
    }
}
