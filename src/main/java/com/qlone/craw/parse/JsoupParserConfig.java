package com.qlone.craw.parse;

import com.qlone.craw.parse.convert.ClazzResponseConverter;
import com.qlone.craw.parse.convert.DefaultResponseConverterFactory;
import com.qlone.craw.parse.convert.ResponseConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-25 09:43
 */
public final class JsoupParserConfig {
    private static List<ResponseConverter.ResponseConverterFactory> converterFactories = new ArrayList<>();
    static {
        converterFactories.add(new DefaultResponseConverterFactory());
    }

    public static void addConvertFactory(ResponseConverter.ResponseConverterFactory factory){
        converterFactories.add(factory);
    }

    public static ResponseConverter getResponseConverter(Type rawType){
        return nextResponseConverter(rawType);
    }

    private static ResponseConverter nextResponseConverter(Type rawType){
        for(int i = 0; i < converterFactories.size(); i++){
            ResponseConverter responseConverter = converterFactories.get(i).get(rawType);
            if(responseConverter != null){
                return responseConverter;
            }
        }
        //return default
        return new ClazzResponseConverter(rawType);
    }
}
