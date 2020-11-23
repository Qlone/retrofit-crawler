package com.qlone.craw.parse.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-11-06 15:27
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface OuterHtml {
    String value();
    int skip() default 0;
    int size() default Integer.MAX_VALUE;

}
