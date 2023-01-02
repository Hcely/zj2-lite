package org.zj2.lite.helper.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多业务处理器标识
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BizMulti {
}
