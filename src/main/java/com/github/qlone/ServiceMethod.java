package com.github.qlone;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-10-23 17:56
 */
abstract class ServiceMethod<T> {

    static <T> ServiceMethod<T> parseAnnotations(RetrofitCrawler retrofitCrawler, Method method){
        ConnectionFactory requestFactory = ConnectionFactory.parseAnnotations(retrofitCrawler, method);
        Type returnType = method.getGenericReturnType();
        if (Utils.hasUnresolvableType(returnType)) {
            throw Utils.methodError(
                    method,
                    "Method return type must not include a type variable or wildcard: %s",
                    returnType);
        }
        if (returnType == void.class) {
            throw Utils.methodError(method, "Service methods cannot return void.");
        }
        return JsoupServiceMethod.parseAnnotations(retrofitCrawler,method,requestFactory);
    }

    abstract T invoke(Object[] args);
}
