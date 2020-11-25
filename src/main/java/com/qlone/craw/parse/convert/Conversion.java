package com.qlone.craw.parse.convert;

import com.qlone.craw.Converter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heweinan
 * @date 2020-11-24 17:20
 */
public class Conversion {
    private static final Map<Type, ResponseConverter<Object,?>> TYPE_HANDLERS = new HashMap<>();


    static {
        TYPE_HANDLERS.put(Integer.class,new IntegerConverter());
        TYPE_HANDLERS.put(int.class,new IntegerConverter());
        TYPE_HANDLERS.put(Long.class,new LongConverter());
        TYPE_HANDLERS.put(long.class,new LongConverter());
        TYPE_HANDLERS.put(Double.class,new DoubleConverter());
        TYPE_HANDLERS.put(double.class,new DoubleConverter());
        TYPE_HANDLERS.put(Float.class,new FloatConverter());
        TYPE_HANDLERS.put(float.class,new FloatConverter());
        TYPE_HANDLERS.put(Boolean.class,new BooleanConverter());
        TYPE_HANDLERS.put(boolean.class,new BooleanConverter());
        TYPE_HANDLERS.put(Date.class,new DateConverter());
        TYPE_HANDLERS.put(BigDecimal.class,new BigDecimalConverter());
        TYPE_HANDLERS.put(String.class,new StringConverter());
    }

    public static  ResponseConverter<Object,?> findConverter(Type type){
        return TYPE_HANDLERS.get(type);
    }


    public static class IntegerConverter implements ResponseConverter<Object,Integer>{

        @Override
        public Integer convert(Object value) {
            return Integer.valueOf(value.toString());
        }
    }

    public static class LongConverter implements ResponseConverter<Object,Long>{

        @Override
        public Long convert(Object value) {
            return Long.valueOf(value.toString());
        }
    }

    public static class FloatConverter implements ResponseConverter<Object,Float>{

        @Override
        public Float convert(Object value) {
            return Float.valueOf(value.toString());
        }
    }

    public static class DoubleConverter implements ResponseConverter<Object,Double>{

        @Override
        public Double convert(Object value) {
            return Double.valueOf(value.toString());
        }
    }

    public static class BooleanConverter implements ResponseConverter<Object,Boolean>{

        @Override
        public Boolean convert(Object value) {
            return Boolean.valueOf(value.toString());
        }
    }

    public static class DateConverter implements ResponseConverter<Object, Date>{

        @Override
        public Date convert(Object value) {
            try {
                return convert(value,"yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public Date convert(Object src, String format) throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(src.toString());
        }
    }

    public static class BigDecimalConverter implements ResponseConverter<Object, BigDecimal>{

        @Override
        public BigDecimal convert(Object value) {
            if(value == null){
                return null;
            }
            return new BigDecimal(value.toString());
        }
    }

    public static class StringConverter implements ResponseConverter<Object,String>{

        @Override
        public String convert(Object value) {
            return value.toString();
        }
    }
}
