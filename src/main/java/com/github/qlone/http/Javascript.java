package com.github.qlone.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-12-02 16:07
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Javascript {
    String value();
    String[] argument() default {};
    int blockTime() default 0;
    TimeUnit blockTimeUnit() default TimeUnit.MILLISECONDS;
}
