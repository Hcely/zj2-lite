package org.zj2.common.sys.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 序号
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sequence")
public class SysSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号id
     */
    @TableId(value = "sequence_id", type = IdType.ASSIGN_ID)
    private String sequenceId;

    /**
     * 序号编码
     */

    private String sequenceKey;

    /**
     * 序号规则编码
     */
    private String sequenceRuleCode;

    /**
     * 序号值
     */
    private Long sequenceNum;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
