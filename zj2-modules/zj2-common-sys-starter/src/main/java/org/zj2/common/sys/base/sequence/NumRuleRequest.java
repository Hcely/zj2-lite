package org.zj2.common.sys.base.sequence;

import org.zj2.common.sys.base.dto.req.NumNextReq;

import java.io.Serializable;

/**
 *  NumRuleBuilder
 *
 * @author peijie.ye
 * @date 2022/12/24 22:31
 */
public class NumRuleRequest {
    private final NumNextReq req;

    public NumRuleRequest(NumNextReq req) {
        this.req = req;
    }

    public <T extends Serializable> NumRuleRequest param(String key, T value) {
        req.putParam(key, value);
        return this;
    }

    public String next() {
        return next(false);
    }

    public String next(boolean errorBack) {
        return NumRuleUtil.next(req, errorBack);
    }
}
