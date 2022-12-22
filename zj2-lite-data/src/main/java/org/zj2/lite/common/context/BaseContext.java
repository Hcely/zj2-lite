package org.zj2.lite.common.context;

import lombok.SneakyThrows;

/**
 *  IContext
 *
 * @author peijie.ye
 * @date 2022/12/8 18:04
 */
public abstract class BaseContext implements Cloneable {
    protected static int nextIdx() {
        return ZContext.nextIdx();
    }

    @Override
    @SneakyThrows
    public BaseContext clone() {//NOSONAR
        return (BaseContext) super.clone();
    }
}
