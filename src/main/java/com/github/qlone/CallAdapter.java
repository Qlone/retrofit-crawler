package com.github.qlone;


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
        public abstract
        CallAdapter<?, ?> get(
                Type returnType, Annotation[] annotations, RetrofitCrawler retrofitCrawler);
    }
}
