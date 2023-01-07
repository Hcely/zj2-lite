package org.zj2.common.uac.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.zj2.lite.common.annotation.JProperty;
import org.zj2.lite.common.util.StrUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class AppDTO implements Serializable {
    public static final String COMMON_APP_CODE = "COMMON";
    protected static final String APP_CONFIG = "appConfig";
    private static final long serialVersionUID = 1L;

    public static String getCacheKey(String appCode) {
        return StrUtil.concat("APP:", appCode);
    }

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 应用配置
     */
    @JProperty(json = APP_CONFIG)
    private Integer allowAllUser;

    /**
     * 单点登录
     */
    @JProperty(json = APP_CONFIG)
    private Integer singleSignOn;

    /**
     * token过期时间
     */
    @JProperty(json = APP_CONFIG)
    private Long tokenTimeout;

    /**
     * 应用状态
     */
    private Integer enableFlag;

    /**
     * 启用时间
     */
    private LocalDateTime enabledTime;

    /**
     * 禁用时间
     */
    private LocalDateTime disabledTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建用户名称
     */
    private String createUserName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新用户
     */
    private String updateUser;

    /**
     * 更新用户名称
     */
    private String updateUserName;

}
