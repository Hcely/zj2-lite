package org.zj2.lite.service.auth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.zj2.lite.service.constant.ServiceConstants;

import java.io.Serializable;

/**
 * JWToken
 *
 * @author peijie.ye
 * @date 2022/11/27 22:25
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthorizationJWT implements Serializable {
    private static final long serialVersionUID = 20221127222539L;
    /**
     *
     */
    @JSONField(name = ServiceConstants.JWT_TOKEN_ID)
    private String tokenId;
    /**
     * 用户id
     */
    @JSONField(name = ServiceConstants.JWT_USER_ID)
    private String userId;

    /**
     * 用户名称
     */
    @JSONField(name = ServiceConstants.JWT_USERNAME)
    private String userName;

    /**
     * appCode
     */
    @JSONField(name = ServiceConstants.JWT_APP_CODE)
    private String appCode;

    /**
     * orgCode
     */
    @JSONField(name = ServiceConstants.JWT_ORG_CODE)
    private String orgCode;

    /**
     * clientCode
     */
    @JSONField(name = ServiceConstants.JWT_CLIENT_CODE)
    private String clientCode;

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

    /**
     * 用户token
     */
    @JSONField(serialize = false, deserialize = false)
    private String token;
}
