package org.zj2.common.uac.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户日志表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uac_user_log")
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户日志id
     */
    @TableId(value = "user_log_id", type = IdType.ASSIGN_ID)
    private String userLogId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 日志事件
     */
    private String logEvent;

    /**
     * 日志相关类型
     */
    private Integer logReferenceType;

    /**
     * 日志相关id
     */
    private String logReferenceId;

    /**
     * 日志相关日期
     */
    private LocalDateTime logReferenceTime;

    /**
     * 日志时间
     */
    private LocalDateTime logTime;

    /**
     * 地址ip
     */
    private String logAddrIp;

    /**
     * 设备
     */
    private String logDevice;

    /**
     * 客户端
     */
    private String logClientCode;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 机构编码
     */
    private String orgCode;

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


}
