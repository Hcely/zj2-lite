package org.zj2.common.sys.base.service.helper;

import org.zj2.common.sys.base.dto.SequenceNextContext;
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
        SequenceSetNumHandler.class,//
})
public class SequenceSetNumHelper extends CommonBizHelper<SequenceNextContext> {
}
