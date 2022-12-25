package org.zj2.common.sys.base.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 单号规则
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysNumRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单号规则id
     */
    private String numRuleId;

    /**
     * 单号规则编码
     */
    private String numRuleCode;

    /**
     * 单号名称
     */
    private String numRuleName;

    /**
     * 单号格式
     */
    private String numRuleFormat;

    /**
     * 自增序号格式
     */
    private String seqIncKeyFormat;

    /**
     * 应用编码
     */
    private String appCode;

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
