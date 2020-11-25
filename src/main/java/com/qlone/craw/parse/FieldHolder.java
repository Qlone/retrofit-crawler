package com.qlone.craw.parse;

import com.qlone.craw.Utils;
import com.qlone.craw.parse.annotation.*;
import com.qlone.craw.parse.convert.ResponseConverter;
import com.qlone.craw.parse.element.AbstractElementAction;
import com.qlone.craw.parse.element.ElementAction;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 持有某个字段赋值的操作
 * @author heweinan
 * @date 2020-11-11 17:03
 */
public class FieldHolder {
    private Object object;
    private Field field;
    private Elements elements;
    private ElementAction<?> elementAction;
    private ResponseConverter<Object,Object> converter;

    public FieldHolder(Builder builder) {
        this.object = builder.object;
        this.field = builder.field;
        this.elementAction = builder.elementAction;
        this.converter = builder.converter;
        this.elements = builder.elements;
    }

    public void postValue() throws IllegalAccessException {
        if(elementAction == null){
            return;
        }
        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        List<?> message = elementAction.action(elements);
        List<Object> convertMessage = message
                .stream()
                .map((mes)-> this.converter.convert(mes))
                .collect(Collectors.toList());


        if(Collection.class.isAssignableFrom(field.getType())){
            field.set(this.object,convertMessage);
        }else if(convertMessage.size() > 0){
            field.set(this.object,convertMessage.get(0));
        }
    }

    public static class Builder{
        private Object object;
        private Field field;
        private Annotation[] annotations;
        private ResponseConverter<Object,Object> converter;
        private Elements elements;
        private ElementAction<?> elementAction;
        private JsoupParser jsoupParser;

        public Builder(Object object,
                       Field field,
                       JsoupParser jsoupParser
                       ){
            this.object = object;
            this.field = field;
            this.jsoupParser = jsoupParser;
            this.annotations = field.getAnnotations();
        }

        public Builder document(Element document){
            this.elements = new Elements(document);
            return this;
        }

        public Builder document(Elements elements){
            this.elements = elements;
            return this;
        }

        public FieldHolder build(){
            Objects.requireNonNull(elements);
            for(Annotation annotation: annotations){
                ElementAction<?> elementAction = parseElementorAnnotation(annotation);
                if(elementAction == null){
                    continue;
                }
                this.elementAction = elementAction;
            }

            Type rawType;
            Type fieldType = this.field.getGenericType();
            if(fieldType instanceof ParameterizedType){
                rawType = Utils.getParameterUpperBound(0,(ParameterizedType) fieldType);
            }else {
                rawType = Utils.getRawType(fieldType);
            }

            this.converter = jsoupParser.getResponseConverter(rawType);

            //return FieldHolder
            return new FieldHolder(this);
        }




        private ElementAction<?> parseElementorAnnotation(Annotation annotation){
            if(annotation instanceof Attr){
                Attr attr = (Attr) annotation;
                int size = attr.size();
                int skip = attr.skip();
                String attrValue = attr.attr();
                String query = attr.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.AttrAction(skip,size,attrValue);
            }else if(annotation instanceof Text){
                Text text = (Text) annotation;
                int size = text.size();
                int skip = text.skip();
                String query = text.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.TextAction(skip,size);
            }else if(annotation instanceof Html){
                Html html = (Html) annotation;
                int size = html.size();
                int skip = html.skip();
                String query = html.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.HtmlAction(skip,size);
            }else if (annotation instanceof OuterHtml){
                OuterHtml outerHtml = (OuterHtml)annotation;
                int size = outerHtml.size();
                int skip = outerHtml.skip();
                String query = outerHtml.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.OuterHtmlAction(skip,size);
            }else if(annotation instanceof Data){
                Data data = (Data) annotation;
                int size= data.size();
                int skip = data.skip();
                String query = data.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.DataAction(skip,size);
            } else if(annotation instanceof Select){
                Select data = (Select) annotation;
                int size= data.size();
                int skip = data.skip();
                String query = data.value();
                this.elements = elements.select(query);
                return new AbstractElementAction.SelectAction(skip,size);
            }

            //no JsoupParse annotation
            return null;
        }
    }
}
