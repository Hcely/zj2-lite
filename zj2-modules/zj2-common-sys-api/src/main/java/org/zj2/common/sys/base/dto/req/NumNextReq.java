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
public class NumNextReq implements Serializable {
    private static final long serialVersionUID = 20221210031326L;
    private String appCode;
    private String numRuleCode;
    private String numRuleFormat;
    private String seqIncKeyFormat;
    private JSONObject params;

    public NumNextReq(String numRuleCode, String numRuleFormat) {
        this.numRuleCode = numRuleCode;
        this.numRuleFormat = numRuleFormat;
    }

    public NumNextReq(String numRuleCode, String numRuleFormat, String seqIncKeyFormat) {
        this.numRuleCode = numRuleCode;
        this.numRuleFormat = numRuleFormat;
        this.seqIncKeyFormat = seqIncKeyFormat;
    }

    public <T extends Serializable> void putParam(String key, T value) {
        if (params == null) {params = new JSONObject();}
        params.put(key, value);
    }
}
