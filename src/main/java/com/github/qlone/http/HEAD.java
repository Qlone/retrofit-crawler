package com.github.qlone.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-10-29 17:45
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface HEAD {
    String value() default "";
}
