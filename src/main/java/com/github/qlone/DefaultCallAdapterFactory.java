package com.github.qlone;

import java.io.IOException;
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
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
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
