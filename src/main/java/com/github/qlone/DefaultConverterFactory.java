package com.github.qlone;

import com.github.qlone.parse.DocumentObject;
import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-10-29 16:00
 */
public class DefaultConverterFactory extends Converter.Factory{

    @Override
    public Converter<Document,?> documentConverter(Type type, Annotation[] annotations, RetrofitCrawler retrofit){
        return new DocumentConverter(type);
    }

    public static class DocumentConverter implements Converter<Document,Object>{
        private Type type;

        public DocumentConverter(Type type) {
            this.type = type;
        }

        @Override
        public Object convert(Document value){
            try {
                return DocumentObject.parseObject(value,type);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
