package org.zj2.common.sys.base.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class SysSequenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号id
     */
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
