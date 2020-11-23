package com.qlone.craw.parse;

import java.util.List;
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
    private List<ResponseConverter.ResponseConverterFactory> converterFactories;


    public JsoupParser(List<ResponseConverter.ResponseConverterFactory> converterFactories) {
        this.converterFactories = converterFactories;
        if(converterFactories == null){
            converterFactories = new ArrayList<>();
        }
        converterFactories.add(new DefaultResponseConverterFactory());
    }

    public void addConvertFactory(ResponseConverter.ResponseConverterFactory factory){
        converterFactories.add(factory);
    }

    public <RestultT> ResponseConverter<String,RestultT> getResponseConverter(Type type){
        return nextResponseConverter(type);
    }

    private <RestultT> ResponseConverter<String,RestultT> nextResponseConverter(Type type){
        for(int i = 0; i < converterFactories.size(); i++){
            ResponseConverter<String,RestultT> responseConverter = converterFactories.get(i).get(type);
            if(responseConverter != null){
                return responseConverter;
            }
        }
        //return default
        throw new IllegalArgumentException("no ResponseConverter found : " + type.getTypeName());
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
