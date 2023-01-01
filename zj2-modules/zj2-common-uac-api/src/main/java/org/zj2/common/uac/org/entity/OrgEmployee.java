package org.zj2.common.uac.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uac_org_employee")
public class OrgEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @TableId(value = "employee_id", type = IdType.ASSIGN_ID)
    private String employeeId;

    /**
     * 员工编号
     */
    private String employeeNo;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 是否可见，1-可见，0-不可见
     */
    private Integer visibleFlag;

    /**
     * 员工状态
     */
    private Integer employeeStatus;

    /**
     * 可用标识
     */
    private Integer enableFlag;

    /**
     * 可用时间
     */
    private LocalDateTime enabledTime;

    /**
     * 无效时间
     */
    private LocalDateTime disabledTime;

    /**
     * 入职时间
     */
    private LocalDateTime entryTime;

    /**
     * 离职时间
     */
    private LocalDateTime quitTime;

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
    @TableLogic
    private Integer isDeleted;
}
