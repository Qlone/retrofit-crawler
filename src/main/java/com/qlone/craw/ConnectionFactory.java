package com.qlone.craw;

import com.sun.istack.internal.Nullable;
import com.qlone.craw.http.DELETE;
import com.qlone.craw.http.GET;
import com.qlone.craw.http.POST;
import org.jsoup.Connection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Proxy;

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
    private final @Nullable String relativeUrl;

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
        @Nullable String relativeUrl;

        final RetrofitCrawler retrofit;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;
        @Nullable ParameterHandler<?>[] parameterHandlers;

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
//            for (int p = 0, lastParameter = parameterCount - 1; p < parameterCount; p++) {
//                parameterHandlers[p] =
//                        parseParameter(p, parameterTypes[p], parameterAnnotationsArray[p], p == lastParameter);
//            }

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
    }
}
