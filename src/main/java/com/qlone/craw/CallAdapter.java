package com.qlone.craw;

import com.sun.istack.internal.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-10-26 17:47
 */
public interface CallAdapter<R, T> {
    Type responseType();

    T adapt(Call<R> call);

    //工厂
    abstract class Factory {
        public abstract @Nullable
        CallAdapter<?, ?> get(
                Type returnType, Annotation[] annotations, RetrofitCrawler retrofitCrawler);
    }
}
