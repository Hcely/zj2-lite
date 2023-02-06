package org.zj2.common.uac.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.zj2.lite.service.auth.AuthorityResource;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户账号名称
     */
    private String userAccountName;

    /**
     * 用户手机区号
     */
    private String userMobileAreaCode;

    /**
     * 用户手机
     */
    @AuthorityResource
    private String userMobile;

    /**
     * 用户邮件
     */
    @AuthorityResource
    private String userEmail;

    /**
     * 用户密码
     */
    @JsonIgnore
    private String userPassword;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 第一次登录时间
     */
    private LocalDateTime firstLoginTime;

    /**
     * 最新一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最新一次登录客户端
     */
    private String lastLoginClientCode;

    /**
     * 最新一次登录应用
     */
    private String lastLoginAppCode;

    /**
     * 停用到期时间
     */
    private LocalDateTime forbiddenExpireTime;

    /**
     * 有效期时间
     */
    private LocalDateTime validExpireTime;

    /**
     * 激活标识
     */
    private Integer activateFlag;

    /**
     * 激活时间
     */
    private LocalDateTime activatedTime;

    /**
     * 用户状态
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

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
