package org.zj2.lite.service.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * AuthenticationSign
 *
 * @author peijie.ye
 * @date 2022/12/8 2:19
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthorizationServerSign implements Serializable {
    private static final long serialVersionUID = 20221208021937L;
    /**
     * appCode
     */
    private String appCode;
    /**
     * 到期时间
     */
    private long timestamp;
    /**
     * 服务应用
     */
    private String serviceName;
    /**
     * 用户token
     */
    private String sign;

    @Override
    public String toString() {
        return "{appCode=" + appCode + ", timestamp=" + timestamp + ", sign=" + sign + '}';
    }
}
