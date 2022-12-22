package org.zj2.common.sys.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.zj2.common.sys.base.dto.SequenceNextContext;
import org.zj2.common.sys.base.dto.SysSequenceDTO;
import org.zj2.common.sys.base.dto.req.SequenceNextReq;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.entity.SysSequence;
import org.zj2.common.sys.base.mapper.SysSequenceMapper;
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

    @Override
    public SequenceNo next(SequenceNextReq rule) {
        SequenceNextContext context = new SequenceNextContext(rule);
        nextHelper.handle(context);
        return context.getSequenceNo();
    }

    @Override
    public void setSequenceNum(SequenceNextReq rule, long num) {
        if (num < 0) {return;}
        SequenceNextContext context = new SequenceNextContext(rule);
        context.setSequenceNum(num);
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