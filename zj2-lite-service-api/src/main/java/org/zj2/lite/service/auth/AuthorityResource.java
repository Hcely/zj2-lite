package org.zj2.lite.service.auth;

import org.zj2.lite.common.entity.Ternary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthorityRequired
 *
 * @author peijie.ye
 * @date 2023/1/2 18:22
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorityResource {
    String value() default "";

    String name() default "";

    Ternary requiredUriAuthority() default Ternary.DEFAULT;

    Ternary requiredPropertyAuthority() default Ternary.DEFAULT;

    String[] propertyAuthority() default {};

    Ternary requiredDataAuthority() default Ternary.DEFAULT;

    String dataAuthority() default "";

    String[] tags() default {};
}
