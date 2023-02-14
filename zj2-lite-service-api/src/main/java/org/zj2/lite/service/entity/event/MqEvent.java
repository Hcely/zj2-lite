package org.zj2.lite.service.entity.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MqEvent
 * <br>CreateDate 一月 16,2022
 * @author peijie.ye
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqEvent {
    String topic();

    String tag() default "";

    String[] type() default {"rocketMq"};
}
