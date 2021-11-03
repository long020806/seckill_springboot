package com.study.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    int second() default 5;
    int maxCount() default 5;
    boolean needLogin() default true;
}
