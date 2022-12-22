package org.zj2.common.sys.base.util;

import org.zj2.common.sys.base.dto.req.SequenceNextReq;
import org.zj2.common.sys.base.dto.SequenceNo;
import org.zj2.common.sys.base.service.SysSequenceApi;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.spring.SpringBeanRef;
import org.zj2.lite.util.AsyncUtil;
import org.zj2.lite.util.TransactionSyncUtil;

/**
 *  SequenceNoUtil
 *
 * @author peijie.ye
 * @date 2022/12/12 2:30
 */
public class SequenceNoUtil {
    private static final SpringBeanRef<SysSequenceApi> SEQUENCE_API_REF = new SpringBeanRef<>(SysSequenceApi.class);

    public static String nextNoIfErrorBack(SequenceNextReq rule) {
        return next0(rule, true);
    }

    public static String nextNo(SequenceNextReq rule) {
        return next0(rule, false);
    }

    private static String next0(SequenceNextReq rule, boolean errorBack) {
        final SysSequenceApi api = SEQUENCE_API_REF.get();
        if (api == null) {throw ZRBuilder.builder("没有序号服务").buildError();}
        final SequenceNo sequenceNo = api.next(rule);
        if (sequenceNo == null) {throw ZRBuilder.builder("生成序号异常").buildError();}
        if (errorBack) {
            TransactionSyncUtil.afterRollback(() -> AsyncUtil.execute(() -> api.back(sequenceNo)));
        }
        return sequenceNo.getSequenceNo();
    }
}
