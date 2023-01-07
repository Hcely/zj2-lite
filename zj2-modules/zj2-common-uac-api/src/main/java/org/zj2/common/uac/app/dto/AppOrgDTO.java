package org.zj2.common.uac.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class AppOrgDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用机构id
     */
    private String appOrgId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 可用标识
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
