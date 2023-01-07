package org.zj2.common.sys.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.SysSequenceDTO;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.common.sys.base.entity.SysSequence;
import org.zj2.common.sys.base.mapper.SysSequenceMapper;
import org.zj2.common.sys.base.service.SysNumRuleService;
import org.zj2.common.sys.base.service.SysSequenceService;
import org.zj2.common.sys.base.service.helper.SequenceNextNoHelper;
import org.zj2.common.sys.base.service.helper.SequenceSetNumHelper;
import org.zj2.lite.service.BaseServiceImpl;

/**
 *  SysConfigServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/10 3:00
 */
@Service
public class SysSequenceServiceImpl extends BaseServiceImpl<SysSequenceMapper, SysSequence, SysSequenceDTO>
        implements SysSequenceService {
    @Autowired
    SequenceNextNoHelper nextHelper;
    @Autowired
    SequenceSetNumHelper sequenceSetNumHelper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private SysNumRuleService sysNumRuleService;

    @Override
    public SysNumRuleDTO getRule(String numRuleCode) {
        return sysNumRuleService.getRule(numRuleCode);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SequenceNo next(NumNextReq rule) {
        NumNextContext context = new NumNextContext(rule);
        nextHelper.handle(context);
        return context.getSequenceNo();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void setSequenceNum(NumNextReq rule, long num) {
        if (num < 0) { return; }
        NumNextContext context = new NumNextContext(rule, num);
        sequenceSetNumHelper.handle(context);
    }

    @Override
    public SysSequenceDTO getByKey(String sequenceKey) {
        return getOne(wrapper().eq(SysSequenceDTO::getSequenceKey, sequenceKey).forUpdate(true));
    }

    @Override
    public boolean update(String sequenceKey, Long oldNum, Long newNum) {
        return update(updateWrapper().set(SysSequenceDTO::getSequenceNum, newNum)
                .eq(SysSequenceDTO::getSequenceKey, sequenceKey).eq(SysSequenceDTO::getSequenceNum, oldNum)) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void back(SequenceNo sequenceNo) {
        SysSequenceDTO sequence = getOne(
                wrapper().eq(SysSequenceDTO::getSequenceRuleCode, sequenceNo.getSequenceRuleCode())
                        .orderByDesc(SysSequenceDTO::getSequenceId));
        if (sequence != null && !StringUtils.equalsIgnoreCase(sequence.getSequenceKey(), sequenceNo.getSequenceKey())) {
            redisTemplate.opsForList().rightPush(sequenceNo.getSequenceKey(), sequenceNo.getSequenceNo());
        }
    }

    @Override
    public String getBackSequenceNo(String sequenceKey) {
        return redisTemplate.opsForList().leftPop(sequenceKey);
    }
}