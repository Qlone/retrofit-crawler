package com.qlone.craw.parse.convert;

import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-11-23 11:12
 */
public class DefaultResponseConverterFactory implements ResponseConverter.ResponseConverterFactory{
    @Override
    public ResponseConverter<Object, ?> get(Type paramType) {
        return Conversion.findConverter(paramType);
    }
}
