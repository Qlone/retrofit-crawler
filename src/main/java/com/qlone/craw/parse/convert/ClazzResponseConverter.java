package com.qlone.craw.parse.convert;

import com.qlone.craw.parse.DocumentObject;
import org.jsoup.nodes.Element;

import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-11-24 16:51
 */
public class ClazzResponseConverter implements ResponseConverter<Element,Object>,  ResponseConverter.ResponseConverterFactory<Object> {

    @Override
    public ResponseConverter<Element, Object> get(Type rawType) {
        return new ClazzResponseConverter(rawType);
    }

    private Type rawType;

    public ClazzResponseConverter(Type rawType) {
        this.rawType = rawType;
    }

    @Override
    public Object convert(Element value) {
        try {
            return DocumentObject.parseObject(value,rawType);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
