package org.zj2.lite.service.auth;

import org.zj2.lite.service.context.TokenType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationRequired {
    TokenType[] requiredType() default {TokenType.JWT};
}
