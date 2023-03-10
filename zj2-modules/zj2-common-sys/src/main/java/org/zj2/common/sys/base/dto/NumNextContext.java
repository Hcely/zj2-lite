package org.zj2.common.sys.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.lite.common.util.BeanUtil;
import org.zj2.lite.common.util.DateUtil;

import java.time.LocalDateTime;

/**
 * SequenceNextContext
 *
 * @author peijie.ye
 * @date 2022/12/11 13:53
 */
@Getter
@Setter
public class NumNextContext extends NumNextReq {
    private static final long serialVersionUID = -1494663056758414636L;
    private final LocalDateTime date;
    private String sequenceKey;
    private long sequenceNum;
    private SequenceNo sequenceNo;

    public NumNextContext(NumNextReq rule) {
        this(rule, null, -1L);
    }

    public NumNextContext(NumNextReq rule, long sequenceNum) {
        this(rule, null, sequenceNum);
    }

    public NumNextContext(NumNextReq rule, LocalDateTime date) {
        this(rule, date, -1L);
    }

    public NumNextContext(NumNextReq rule, LocalDateTime date, long sequenceNum) {
        BeanUtil.copyProperties(rule, this);
        this.date = date == null ? DateUtil.now() : date;
        this.sequenceNum = sequenceNum;
    }
}
