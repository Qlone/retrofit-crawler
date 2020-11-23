package com.qlone.craw.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-10-27 15:41
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DELETE {
    String value() default "";
}
