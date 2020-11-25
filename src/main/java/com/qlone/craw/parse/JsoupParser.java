package com.qlone.craw.parse;

import java.util.List;

import com.qlone.craw.parse.convert.ClazzResponseConverter;
import com.qlone.craw.parse.convert.DefaultResponseConverterFactory;
import com.qlone.craw.parse.convert.ResponseConverter;
import org.jsoup.nodes.Element;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 *
 * 负责组装FieldHolder
 *
 *
 * @author heweinan
 * @date 2020-11-11 16:34
 */
public class JsoupParser {

    public static ResponseConverter getResponseConverter(Type rawType){
        return JsoupParserConfig.getResponseConverter(rawType);
    }

    public <T> T parse(Element element,Type type) throws IllegalAccessException, InstantiationException {
        Class<T> clazz;
        if(type instanceof Class){
            clazz = (Class) type;
        }else {
            return null;
        }
        T t = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<FieldHolder> fieldHolders = new ArrayList<>();
        for(Field field: declaredFields){
            FieldHolder.Builder builder = new FieldHolder.Builder(
                    t, field, this);
            builder.document(element);
            fieldHolders.add(builder.build());
        }

        for (FieldHolder fieldHolder: fieldHolders){
            fieldHolder.postValue();
        }

        return t;
    }

}
