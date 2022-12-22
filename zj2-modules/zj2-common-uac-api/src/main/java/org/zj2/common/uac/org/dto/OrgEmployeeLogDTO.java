package org.zj2.common.uac.org.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工日志表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrgEmployeeLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工日志id
     */
    private String orgEmployeeLogId;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 员工id
     */
    private String employeeId;

    /**
     * 员工编码
     */
    private String employeeNo;

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
