package org.zj2.common.sys.base.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.entity.SysNumRule;
import org.zj2.common.sys.base.mapper.SysNumRuleMapper;
import org.zj2.common.sys.base.sequence.NumRuleUtil;
import org.zj2.common.sys.base.service.SysNumRuleService;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.service.cache.CacheUtil;

/**
 *  SysNumRuleServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/24 21:00
 */
@Service
public class SysNumRuleServiceImpl extends BaseServiceImpl<SysNumRuleMapper, SysNumRule, SysNumRuleDTO>
        implements SysNumRuleService {
    @Override
    public SysNumRuleDTO edit(SysNumRuleDTO rule) {
        SysNumRuleDTO exist = getRule(rule.getNumRuleCode());
        if (exist != null) {rule.setNumRuleId(exist.getNumRuleId());}
        save(rule);
        CacheUtil.sendCacheSign(NumRuleUtil.getRuleCacheKey(rule.getNumRuleCode()));
        return rule;
    }

    @Override
    public SysNumRuleDTO getRule(String numRuleCode) {
        return getOne(wrapper().eq(SysNumRuleDTO::getNumRuleCode, numRuleCode));
    }
}
