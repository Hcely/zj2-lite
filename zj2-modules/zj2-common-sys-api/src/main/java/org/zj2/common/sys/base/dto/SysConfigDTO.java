package org.zj2.common.sys.base.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.common.util.NumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-10
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class SysConfigDTO implements Serializable {
    public static final String COMMON_APP_CODE = "common";
    private static final long serialVersionUID = 1L;

    public static String getConfigKey(String appCode, String configCode) {
        if(StringUtils.isEmpty(appCode)) { appCode = COMMON_APP_CODE; }
        StringBuilder sb = new StringBuilder(StringUtils.length(appCode) + StringUtils.length(configCode) + 16);
        sb.append("SYS_CONFIG:").append(appCode);
        if(StringUtils.isNotEmpty(configCode)) {
            sb.append(':' ).append(configCode);
        }
        return sb.toString();
    }

    /**
     * 系统配置id
     */
    private String sysConfigId;

    /**
     * 系统配置编码
     */
    private String configCode;

    /**
     * 系统配置名称
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置额外值0
     */
    private String configExtValue0;

    /**
     * 配置额外值1
     */
    private String configExtValue1;

    /**
     * 应用编码
     */
    private String appCode;

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

    public String valueStr() {
        return valueStr(null);
    }

    public String valueStr(String defaultValue) {
        return StringUtils.defaultIfEmpty(configValue, defaultValue);
    }

    public Integer valueInt() {
        return valueInt(null);
    }

    public Integer valueInt(Integer defaultValue) {
        return NumUtil.parseInt(configValue, defaultValue);
    }

    public Boolean valueBoolean() {
        return BooleanUtil.parse(configValue, null);
    }

    public Boolean valueBoolean(Boolean defaultValue) {
        return BooleanUtil.parse(configValue, defaultValue);
    }

    public BigDecimal valueNum() {
        return valueNum(null);
    }

    public BigDecimal valueNum(Number defaultValue) {
        return NumUtil.parse(configValue, defaultValue);
    }

    public LocalDateTime valueDate() {
        return DateUtil.parse(configValue);
    }

    public LocalDateTime valueDate(String defaultValue) {
        LocalDateTime time = DateUtil.parse(configValue);
        if(time == null) { time = DateUtil.parse(defaultValue); }
        return time;
    }
}
