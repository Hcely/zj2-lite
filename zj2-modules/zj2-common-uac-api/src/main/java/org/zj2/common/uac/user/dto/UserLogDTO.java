package org.zj2.common.uac.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户日志表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户日志id
     */
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
     * 日志备注
     */
    private String logRemark;

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
