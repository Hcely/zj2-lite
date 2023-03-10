package org.zj2.lite.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * json内部字段
 * 在BeanUtil 把数据库的DO 转换成对外的对象
 * <br>CreateDate 九月 05,2022
 *
 * @author peijie.ye
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JProperty {
    String json();

    String property() default "";
}
