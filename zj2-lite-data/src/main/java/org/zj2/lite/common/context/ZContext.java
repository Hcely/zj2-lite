package org.zj2.lite.common.context;

import lombok.SneakyThrows;

/**
 * BaseContext
 *
 * @author peijie.ye
 * @date 2022/12/8 18:04
 */
public abstract class ZContext implements Cloneable {
    @SneakyThrows
    protected ZContext copy() {
        return (ZContext) clone();
    }
}
