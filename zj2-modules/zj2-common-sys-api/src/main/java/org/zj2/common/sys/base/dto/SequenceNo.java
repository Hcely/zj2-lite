package org.zj2.common.sys.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  SequenceNo
 *
 * @author peijie.ye
 * @date 2022/12/10 3:20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequenceNo implements Serializable {
    private static final long serialVersionUID = 20221210032035L;
    private String sequenceRuleCode;
    private String sequenceKey;
    private String sequenceNo;
}
