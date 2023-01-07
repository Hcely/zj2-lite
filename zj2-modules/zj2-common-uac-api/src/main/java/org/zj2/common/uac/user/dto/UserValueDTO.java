package org.zj2.common.uac.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户编码表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class UserValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户值id
     */
    private String userValueId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户值类型
     */
    private String userValueType;

    /**
     * 用户值
     */
    private String userValue;

    /**
     * 用户额外值
     */
    private String userExtValue;

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
