package com.github.qlone;

import com.github.qlone.selenium.ConnectionManage;
import com.github.qlone.selenium.SeleniumDriverBuilder;
import com.github.qlone.selenium.SeleniumDriver;
import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * @author heweinan
 * @date 2020-10-23 17:54
 */
public final class RetrofitCrawler {
    private final Map<Method,ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();

    final String baseUrl;
    final java.net.Proxy proxy;
    final SeleniumDriverBuilder seleniumBuilder;
    final ConnectionManage connectionManage;
    //请求封装
    final List<CallAdapter.Factory> callAdapterFactories;
    final List<Converter.Factory> converterFactories;

    protected RetrofitCrawler(String baseUrl,
                              List<CallAdapter.Factory> callAdapterFactories,
                              List<Converter.Factory> converterFactories,
                              java.net.Proxy proxy,
                              SeleniumDriverBuilder seleniumBuilder,
                              ConnectionManage connectionManage){
        this.baseUrl = baseUrl;
        this.callAdapterFactories = callAdapterFactories;
        this.converterFactories = converterFactories;
        this.proxy = proxy;
        this.seleniumBuilder = seleniumBuilder;
        this.connectionManage = connectionManage;
    }

    //创建代理类
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> service){
        return (T) Proxy.newProxyInstance(
                service.getClassLoader(),
                new Class<?>[]{service},
                new InvocationHandler() {
                    private final Object[] emptyArgs = new Object[0];

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        args = args != null ? args : emptyArgs;
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        return loadServiceMethod(method).invoke(args);
                    }
                }
        );
    }

    ServiceMethod<?> loadServiceMethod(Method method){
        ServiceMethod<?> result = serviceMethodCache.get(method);
        if(result != null) return result;
        synchronized (serviceMethodCache){
            result = serviceMethodCache.get(method);
            if(result == null){
                result = ServiceMethod.parseAnnotations(this,method);
                serviceMethodCache.put(method,result);
            }
        }
        return result;
    }

    public SeleniumDriverBuilder webDriverBuilder(){
        if(seleniumBuilder.getDriverContent() == null){
            throw new IllegalStateException("webDriver required.");
        }
        return this.seleniumBuilder;
    }

    public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
        return nextCallAdapter(null, returnType, annotations);
    }
    //获取call适配器
    public CallAdapter<?,?> nextCallAdapter(
             CallAdapter.Factory skipPast, Type returnType, Annotation[] annotations){
        Objects.requireNonNull(returnType, "returnType == null");
        Objects.requireNonNull(annotations, "annotations == null");

        int start = callAdapterFactories.indexOf(skipPast) + 1;
        for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
            CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
            if (adapter != null) {
                return adapter;
            }
        }

        StringBuilder builder =
                new StringBuilder("Could not locate call adapter for ").append(returnType).append(".\n");
        if (skipPast != null) {
            builder.append("  Skipped:");
            for (int i = 0; i < start; i++) {
                builder.append("\n   * ").append(callAdapterFactories.get(i).getClass().getName());
            }
            builder.append('\n');
        }
        builder.append("  Tried:");
        for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
            builder.append("\n   * ").append(callAdapterFactories.get(i).getClass().getName());
        }
        throw new IllegalArgumentException(builder.toString());
    }

    //转换器
    @SuppressWarnings("unchecked")
    public <T> Converter<Document,T> documentTConverter(Type type, Annotation[] annotations){
        Objects.requireNonNull(type, "type == null");
        Objects.requireNonNull(annotations, "annotations == null");
        for (int i = 0, count = converterFactories.size(); i < count; i++) {
            Converter<Document, ?> converter =
                    converterFactories.get(i).documentConverter(type, annotations, this);
            if (converter != null) {
                //noinspection unchecked
                return (Converter<Document, T>) converter;
            }
        }

        return (Converter<Document, T>) new DefaultConverterFactory().documentConverter(type,annotations,this);
    }

    //构建者
    public static class Builder{
        private String baseUrl;
        private java.net.Proxy proxy;
        private int maxRequest = 5;
        private SeleniumDriverBuilder seleniumBuilder;
        private ConnectionManage connectionManage;
        private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private List<Converter.Factory> converterFactories = new ArrayList<>();

        public Builder() {
            seleniumBuilder = new SeleniumDriverBuilder();
        }

        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            callAdapterFactories.add(Objects.requireNonNull(factory, "factory == null"));
            return this;
        }

        public Builder proxy(java.net.Proxy proxy){
            this.proxy = proxy;
            return this;
        }

        public Builder seleniumDirver(SeleniumDriver seleniumDriver){
            this.seleniumBuilder.driver(seleniumDriver);
            return this;
        }

        public Builder seleniumDirverPath(String path){
            this.seleniumBuilder.driverPath(path);
            return this;
        }

        public Builder connectionManage(ConnectionManage connectionManage){
            Objects.requireNonNull(connectionManage);
            this.connectionManage = connectionManage;
            return this;
        }

        public Builder maxReuqest(int maxRequest){
            this.maxRequest = maxRequest;
            return this;
        }


        public RetrofitCrawler build(){
            if (baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            if(connectionManage == null){
                connectionManage = new ConnectionManage();
            }
            connectionManage.setMaxRequest(maxRequest);

            List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>(this.callAdapterFactories);
            callAdapterFactories.add(new DefaultCallAdapterFactory());

            return new RetrofitCrawler(baseUrl,unmodifiableList(callAdapterFactories),unmodifiableList(converterFactories),proxy,seleniumBuilder,connectionManage);
        }
    }
}
