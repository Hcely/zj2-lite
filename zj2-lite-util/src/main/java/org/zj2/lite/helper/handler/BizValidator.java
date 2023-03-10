package org.zj2.lite.helper.handler;


import org.zj2.lite.util.ZRBuilder;

/**
 * BizHandler
 * <br>CreateDate 一月 10,2022
 *
 * @author peijie.ye
 * @since 1.0
 */
public interface BizValidator<C> extends BizFunc {
    void valid(C context, ZRBuilder hr);
}
