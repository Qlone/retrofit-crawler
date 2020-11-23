package com.qlone.craw.parse;

import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-11-11 16:15
 */
public interface ResponseConverter<F,T> {
    T convert(F value);

    interface ResponseConverterFactory<RestultT>{
        ResponseConverter<String,RestultT> get(Type paramType);
    }
}
