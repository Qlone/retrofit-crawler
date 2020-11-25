package com.qlone.craw.parse.convert;

import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-11-11 16:15
 */
public interface ResponseConverter<F,T> {
    T convert(F value);

    interface ResponseConverterFactory<RestultT>{
        ResponseConverter<?,RestultT> get(Type rawType);
    }
}
