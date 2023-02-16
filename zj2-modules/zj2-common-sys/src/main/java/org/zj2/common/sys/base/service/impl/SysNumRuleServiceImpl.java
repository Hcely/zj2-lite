package org.zj2.common.sys.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.SysNumRuleQuery;
import org.zj2.common.sys.base.entity.SysNumRule;
import org.zj2.common.sys.base.mapper.SysNumRuleMapper;
import org.zj2.common.sys.base.sequence.NumRuleUtil;
import org.zj2.common.sys.base.service.SysNumRuleService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.util.PatternUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.util.ZRBuilder;

/**
 * SysNumRuleServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/24 21:00
 */
@Service
public class SysNumRuleServiceImpl extends BaseServiceImpl<SysNumRuleMapper, SysNumRule, SysNumRuleDTO>
        implements SysNumRuleService {
    @Override
    public SysNumRuleDTO saveRule(SysNumRuleDTO rule) {
        SysNumRuleDTO exist = getRule(rule.getNumRuleCode());
        if (exist != null) {
            rule.setNumRuleId(exist.getNumRuleId());
        } else {
            if (StringUtils.isEmpty(rule.getNumRuleCode())) {
                throw ZRBuilder.failureErr("单号规则编码不能为空");
            }
            if (PatternUtil.isWord(rule.getNumRuleCode())) {
                throw ZRBuilder.failureErr("单号规则编码不合法");
            }
        }
        save(rule);
        CacheUtil.sendCacheSign(SysNumRuleDTO.getRuleCacheKey(rule.getNumRuleCode()));
        return rule;
    }

    @Override
    public SysNumRuleDTO getRule(String numRuleCode) {
        return getOne(wrapper().eq(SysNumRuleDTO::getNumRuleCode, numRuleCode));
    }

    @Override
    public void removeRule(String numRuleCode) {
        delete(wrapper().eq(SysNumRuleDTO::getNumRuleCode, numRuleCode));
    }

    @Override
    public ZListResp<SysNumRuleDTO> pageQuery(SysNumRuleQuery query) {
        return pageQuery(query, e -> query(wrapper(true).like(SysNumRuleDTO::getNumRuleCode, e.getNumRuleCode())
                .like(SysNumRuleDTO::getNumRuleName, e.getNumRuleName())));

    }
}
