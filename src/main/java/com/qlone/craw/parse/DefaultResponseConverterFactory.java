package com.qlone.craw.parse;

import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-11-23 11:12
 */
public class DefaultResponseConverterFactory implements ResponseConverter.ResponseConverterFactory<String>{
    @Override
    public ResponseConverter<String, String> get(Type paramType) {
        return value -> value;
    }
}
