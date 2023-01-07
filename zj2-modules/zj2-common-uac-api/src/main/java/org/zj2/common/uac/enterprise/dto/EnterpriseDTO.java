package org.zj2.common.uac.enterprise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class EnterpriseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 企业编码
     */
    private String enterpriseCode;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 社会信用码
     */
    private String enterpriseSocialCreditCode;

    /**
     * 企业状态，100-启用，1000-禁用
     */
    private Integer enterpriseStatus;

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
