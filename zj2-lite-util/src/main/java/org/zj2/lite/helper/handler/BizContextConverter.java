package org.zj2.lite.helper.handler;


import org.zj2.lite.helper.chain.ChainBizContext;

/**
 *
 * <br>CreateDate 十月 08,2022
 * @author peijie.ye
 */
public interface BizContextConverter extends BizFunc {
    Object convert(ChainBizContext chainContext);
}
