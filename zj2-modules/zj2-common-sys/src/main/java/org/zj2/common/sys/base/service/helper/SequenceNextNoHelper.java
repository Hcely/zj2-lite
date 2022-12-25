package org.zj2.common.sys.base.service.helper;

import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.lite.helper.BizReference;
import org.zj2.lite.helper.CommonBizHelper;

/**
 *  SysSequenceNextHelper
 *
 * @author peijie.ye
 * @date 2022/12/11 13:34
 */
@BizReference({//
        SequencePrepareHandler.class,//
        SequenceSeqKeyHandler.class,//
        SequenceBackNoHandler.class,//
        SequenceIncNumHandler.class,//
        SequenceBuildNoHandler.class,//
})
public class SequenceNextNoHelper extends CommonBizHelper<NumNextContext> {
}
