package com.github.qlone;

import com.github.qlone.http.*;
import org.jsoup.Connection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.Map;

/**
 * @author heweinan
 * @date 2020-10-27 10:18
 */
public class ConnectionFactory {
    protected static ConnectionFactory parseAnnotations(RetrofitCrawler retrofitCrawler, Method method) {
        return new Builder(retrofitCrawler, method).build();
    }

    private String baseUrl;
    private final Proxy proxy;
    private final String httpMethod;
    private final String relativeUrl;

    private final ParameterHandler<?>[] parameterHandlers;

    public ConnectionFactory(Builder builder) {
        this.parameterHandlers = builder.parameterHandlers;
        this.baseUrl = builder.retrofit.baseUrl;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.proxy = builder.retrofit.proxy;
    }

    Connection create(Object[] args) throws IOException {
        @SuppressWarnings("unchecked")
        ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;

        int argumentCount = args.length;
        if (argumentCount != handlers.length) {
            throw new IllegalArgumentException(
                    "Argument count ("
                            + argumentCount
                            + ") doesn't match expected count ("
                            + handlers.length
                            + ")");
        }
        //TODO:添加参数
        ConnectionBuilder connectionBuilder =
                new ConnectionBuilder(
                        httpMethod,
                        baseUrl,
                        relativeUrl,
                        proxy);

        for (int p = 0; p < argumentCount; p++) {
            handlers[p].apply(connectionBuilder, args[p]);
        }

        return connectionBuilder.get();
    }


    static final class Builder{

        String httpMethod;
        String relativeUrl;

        final RetrofitCrawler retrofit;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;
        ParameterHandler<?>[] parameterHandlers;

        Builder(RetrofitCrawler retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            methodAnnotations = method.getDeclaredAnnotations();
            parameterAnnotationsArray = method.getParameterAnnotations();
            parameterTypes = method.getParameterTypes();
        }

        ConnectionFactory build(){
            for(Annotation annotation: methodAnnotations){
                parseMethodAnnotation(annotation);
            }

            if(httpMethod == null){
                throw Utils.methodError(method,"HTTP method annotation is required (e.g., @GET, @POST, etc.).");
            }

            int parameterCount = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler<?>[parameterCount];
            for (int p = 0; p < parameterCount; p++) {
                parameterHandlers[p] =
                        parseParameter(p, parameterTypes[p], parameterAnnotationsArray[p]);
            }

            return new ConnectionFactory(this);
        }

        //解析方法注解
        private void parseMethodAnnotation(Annotation annotation){
            if(annotation instanceof DELETE){
                parseHttpMethodAndPath("DELETE",((DELETE) annotation).value());
            }else if(annotation instanceof GET){
                parseHttpMethodAndPath("GET",((GET) annotation).value());
            }else if(annotation instanceof POST){
                parseHttpMethodAndPath("POST",((POST) annotation).value());
            }else if(annotation instanceof HEAD){
                parseHttpMethodAndPath("HEAD",((HEAD) annotation).value());
            }else if(annotation instanceof OPTIONS){
                parseHttpMethodAndPath("OPTIONS",((OPTIONS) annotation).value());
            }else if(annotation instanceof PATCH){
                parseHttpMethodAndPath("PATCH",((PATCH) annotation).value());
            }else if(annotation instanceof PUT){
                parseHttpMethodAndPath("PATCH",((PUT) annotation).value());
            }else if(annotation instanceof TRACE){
                parseHttpMethodAndPath("PATCH",((TRACE) annotation).value());
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value){
            if (this.httpMethod != null) {
                throw Utils.methodError(
                        method,
                        "Only one HTTP method is allowed. Found: %s and %s.",
                        this.httpMethod,
                        httpMethod);
            }
            this.httpMethod = httpMethod;
            if (value.isEmpty()) {
                return;
            }
            //TODO:添加url正则处理
            this.relativeUrl = value;
        }

        //解析参数
        private ParameterHandler<?> parseParameter(
                int p, Type parameterType, Annotation[] annotations) {
            ParameterHandler<?> result = null;
            if(annotations != null){
                for(Annotation annotation: annotations){
                    ParameterHandler<?> annotationAction =  parseParameterAnnotation(p,parameterType,annotations,annotation);

                    if(annotationAction == null){
                        continue;
                    }
                    if(result != null){
                        throw Utils.parameterError(
                                method, p, "Multiple RetrofitCrawler annotations found, only one allowed.");
                    }
                    result = annotationAction;
                }
            }
            if(result == null){
                throw Utils.parameterError(method, p, "No RetrofitCrawler annotation found.");
            }

            return result;
        }

        private ParameterHandler<?> parseParameterAnnotation(
                int p, Type type, Annotation[] annotations, Annotation annotation){
            if(annotation instanceof RelativeUrl){
                if(type == String.class){
                    return new ParameterHandler.RelativeUrl();
                }else {
                   throw Utils.parameterError(method,p,"@RelativeUrl must be String");
                }
            }else if(annotation instanceof Path){
                if(type == String.class){
                    return new ParameterHandler.Path();
                }else {
                    throw Utils.parameterError(method,p,"@Path must be String");
                }
            }else if(annotation instanceof Query){
                Query anno = (Query) annotation;
                return new ParameterHandler.Query(anno.value());
            }else if(annotation instanceof QueryMap){
                Class<?> rawParamType = Utils.getRawType(type);
                if(!Map.class.isAssignableFrom(rawParamType)){
                    throw Utils.parameterError(method, p, "@QueryMap parameter type must be Map.");
                }
                if(!(type instanceof ParameterizedType)){
                    throw Utils.parameterError(method,p,"Map must include generic types (e.g., Map<String, String>)");
                }
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type keyType = Utils.getParameterUpperBound(0, parameterizedType);
                if(String.class != keyType){
                    throw Utils.parameterError(method, p, "@QueryMap keys must be of type String: " + keyType);
                }
                //should convert value for next version
                return new ParameterHandler.QueryMap();
            }else if(annotation instanceof Header){
                Header anno = (Header)annotation;
                if(type == String.class){
                    return new ParameterHandler.Header(anno.value());
                }else {
                    throw Utils.parameterError(method,p,"@Header must be String");
                }
            }else if(annotation instanceof HeaderMap){
                Class<?> rawParamType = Utils.getRawType(type);
                if(!Map.class.isAssignableFrom(rawParamType)){
                    throw Utils.parameterError(method, p, "@HeaderMap parameter type must be Map.");
                }
                if(!(type instanceof ParameterizedType)){
                    throw Utils.parameterError(method,p,"Map must include generic types (e.g., Map<String, String>)");
                }
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type keyType = Utils.getParameterUpperBound(0, parameterizedType);
                if(String.class != keyType){
                    throw Utils.parameterError(method, p, "@HeaderMap keys must be of type String: " + keyType);
                }
                //should convert value for next version
                return new ParameterHandler.HeaderMap();
            }else if(annotation instanceof Cookie){
                Cookie anno = (Cookie)annotation;
                if(type == String.class){
                    return new ParameterHandler.Cookie(anno.value());
                }else {
                    throw Utils.parameterError(method,p,"@Cookie must be String");
                }
            }else if(annotation instanceof CookieMap){
                Class<?> rawParamType = Utils.getRawType(type);
                if(!Map.class.isAssignableFrom(rawParamType)){
                    throw Utils.parameterError(method, p, "@CookieMap parameter type must be Map.");
                }
                if(!(type instanceof ParameterizedType)){
                    throw Utils.parameterError(method,p,"Map must include generic types (e.g., Map<String, String>)");
                }
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type keyType = Utils.getParameterUpperBound(0, parameterizedType);
                if(String.class != keyType){
                    throw Utils.parameterError(method, p, "@CookieMap keys must be of type String: " + keyType);
                }
                //should convert value for next version
                return new ParameterHandler.CookieMap();
            }else if(annotation instanceof Body){
                return new ParameterHandler.Body();
            }

            //no annotation found
            return null;
        }
    }
}
