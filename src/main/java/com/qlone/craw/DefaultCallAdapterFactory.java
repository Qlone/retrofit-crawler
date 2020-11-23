package com.qlone.craw;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-10-27 20:42
 */
final public class DefaultCallAdapterFactory extends CallAdapter.Factory{
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, RetrofitCrawler retrofitCrawler) {
        if (Utils.getRawType(returnType) != Call.class) {
            return null;
        }
        final Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);

        return new CallAdapter<Object, Call<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public Call<Object> adapt(Call<Object> call) {
                return call;
            }
        };
    }
}
