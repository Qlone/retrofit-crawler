package com.github.qlone;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-10-28 17:27
 */
//结果转换
public interface Converter<F,T>{
    T convert(F value) throws IOException;

    abstract class Factory {
        public Converter<Document,?> documentConverter(Type type, Annotation[] annotations, RetrofitCrawler retrofit){
            return null;
        }
    }
}
