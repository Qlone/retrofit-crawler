package com.qlone.craw.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author heweinan
 * @date 2020-11-25 11:06
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface CookieMap {
}