package org.zj2.common.sys.base.sequence;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.lite.service.context.AuthenticationContext;

import java.io.Serializable;

/**
 *  SequenceRule
 *
 * @author peijie.ye
 * @date 2022/12/23 17:48
 */
@Getter
public class NumRule {
    private final String numRuleCode;
    private final String numRuleFormat;
    private final String seqIncKeyFormat;

    public NumRule(String numRuleCode, String numRuleFormat) {
        this(numRuleCode, numRuleFormat, numRuleFormat);
    }

    public NumRule(String numRuleCode, String numRuleFormat, String seqIncKeyFormat) {
        this.numRuleCode = numRuleCode;
        this.numRuleFormat = numRuleFormat;
        this.seqIncKeyFormat = seqIncKeyFormat;
    }

    public <T extends Serializable> NumRuleRequest param(String key, T value) {
        NumRuleRequest request = new NumRuleRequest(buildReq());
        request.param(key, value);
        return request;
    }

    public NumRuleRequest orgCode(String orgCode) {
        NumRuleRequest request = new NumRuleRequest(buildReq());
        request.orgCode(orgCode);
        return request;
    }

    public NumRuleRequest appCode(String appCode) {
        NumRuleRequest request = new NumRuleRequest(buildReq());
        request.appCode(appCode);
        return request;
    }

    public String next() {
        return next(false);
    }

    public String next(boolean errorBack) {
        NumNextReq req = buildReq();
        return NumRuleUtil.next(req, errorBack);
    }

    protected NumNextReq buildReq() {
        NumNextReq req = new NumNextReq();
        req.setAppCode(AuthenticationContext.currentAppCode());
        req.setNumRuleCode(numRuleCode);
        SysNumRuleDTO rule = NumRuleUtil.getNumRule(numRuleCode);
        if (rule == null) {
            req.setNumRuleFormat(numRuleFormat);
            req.setSeqIncKeyFormat(seqIncKeyFormat);
        } else {
            req.setNumRuleFormat(StringUtils.defaultIfEmpty(rule.getNumRuleFormat(), numRuleFormat));
            req.setSeqIncKeyFormat(StringUtils.defaultIfEmpty(rule.getSeqIncKeyFormat(), seqIncKeyFormat));
        }
        return req;
    }
}
