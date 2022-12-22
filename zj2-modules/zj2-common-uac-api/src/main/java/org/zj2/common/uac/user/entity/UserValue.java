package org.zj2.common.uac.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.zj2.lite.common.annotation.SensitiveProperty;

/**
 * <p>
 * 用户编码表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uac_user_value")
public class UserValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户值id
     */
    @TableId(value = "user_value_id", type = IdType.ASSIGN_ID)
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
    @SensitiveProperty
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
