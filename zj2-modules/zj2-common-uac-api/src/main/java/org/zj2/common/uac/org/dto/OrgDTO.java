package org.zj2.common.uac.org.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class OrgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

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
     * 机构状态
     */
    private Integer enableFlag;

    /**
     * 启用时间
     */
    private LocalDateTime enabledTime;

    /**
     * 机构状态
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

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
