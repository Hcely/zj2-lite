package org.zj2.common.sys.base.sequence;

import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.common.sys.base.service.SysNumRuleApi;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.spring.SpringBeanRef;
import org.zj2.lite.util.AsyncUtil;
import org.zj2.lite.util.TransactionSyncUtil;
import org.zj2.lite.util.ZRBuilder;

/**
 *  SequenceNoUtil
 *
 * @author peijie.ye
 * @date 2022/12/12 2:30
 */
public class NumRuleUtil {
    private static final SpringBeanRef<SysNumRuleApi> SEQUENCE_API_REF = new SpringBeanRef<>(SysNumRuleApi.class);

    public static String getRuleCacheKey(String numRuleCode) {
        return StrUtil.concat("NUM_RULE:", numRuleCode);
    }

    static SysNumRuleDTO getNumRule(String numRuleCode) {
        final SysNumRuleApi api = SEQUENCE_API_REF.get();
        if (api == null) { throw ZRBuilder.builder("没有序号服务").buildError(); }
        String cacheKey = getRuleCacheKey(numRuleCode);
        return CacheUtil.DEF_CACHE.getCache(cacheKey, numRuleCode, api::getRule, true);
    }

    static String next(NumNextReq req, boolean errorBack) {
        final SysNumRuleApi api = SEQUENCE_API_REF.get();
        if (api == null) { throw ZRBuilder.builder("没有序号服务").buildError(); }
        final SequenceNo sequenceNo = api.next(req);
        if (sequenceNo == null) { throw ZRBuilder.builder("生成序号异常").buildError(); }
        if (errorBack) {
            TransactionSyncUtil.afterRollback(() -> AsyncUtil.execute(() -> api.back(sequenceNo)));
        }
        return sequenceNo.getSequenceNo();
    }
}
