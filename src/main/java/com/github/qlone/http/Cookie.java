package com.github.qlone.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-11-25 11:04
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Cookie {
    String value();
}
