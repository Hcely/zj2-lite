package org.zj2.common.sys.base.dto.req;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *  SequenceRule
 *
 * @author peijie.ye
 * @date 2022/12/10 3:13
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SequenceNextReq implements Serializable {
    private static final long serialVersionUID = 20221210031326L;
    private String appCode;
    private String sequenceRuleCode;
    private String seqNoFormat;
    private String seqIncKeyFormat;
    private JSONObject params;

    public SequenceNextReq(String sequenceRuleCode, String seqNoFormat) {
        this.sequenceRuleCode = sequenceRuleCode;
        this.seqNoFormat = seqNoFormat;
    }

    public SequenceNextReq(String sequenceRuleCode, String seqNoFormat, String seqIncKeyFormat) {
        this.sequenceRuleCode = sequenceRuleCode;
        this.seqNoFormat = seqNoFormat;
        this.seqIncKeyFormat = seqIncKeyFormat;
    }

    public <T extends Serializable> void addParam(String key, T value) {
        if (params == null) {params = new JSONObject();}
        params.put(key, value);
    }
}
