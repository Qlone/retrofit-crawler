package com.github.qlone;

import com.github.qlone.selenium.ConnectionManage;
import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Jsoup方法处理类
 * @author heweinan
 * @date 2020-10-26 16:42
 */
abstract class JsoupServiceMethod<ResponseT, ReturnT> extends ServiceMethod<ReturnT>{

    static <ResponseT, ReturnT> JsoupServiceMethod<ResponseT, ReturnT> parseAnnotations(
            RetrofitCrawler retrofitCrawler, Method method,ConnectionFactory connectionFactory){
        Annotation[] annotations = method.getAnnotations();
        Type adapterType = method.getGenericReturnType();
        CallAdapter<ResponseT, ReturnT> callAdapter =
                createCallAdapter(retrofitCrawler, method, adapterType, annotations);

        Type ResponseType = callAdapter.responseType();
        Converter<Document, ResponseT> documentConverter = createDocumentConverter(retrofitCrawler, method, ResponseType);

        ConnectionManage client = retrofitCrawler.connectionManage;
        return new CallAdapted<>(connectionFactory,client,callAdapter,documentConverter);
    }

    ReturnT invoke(Object[] args) {
        Call<ResponseT>  call = new JsoupCall<>(connectionFactory,args,seleniumClient,documentConverter);
        return adapt(call,args);
    }

    protected abstract
    ReturnT adapt(Call<ResponseT> call, Object[] args);

    private ConnectionFactory connectionFactory;
    private Converter<Document,ResponseT> documentConverter;
    private ConnectionManage seleniumClient;

    public JsoupServiceMethod(ConnectionFactory connectionFactory, ConnectionManage seleniumClient, Converter<Document,ResponseT> documentConverter) {
        this.connectionFactory = connectionFactory;
        this.documentConverter = documentConverter;
        this.seleniumClient = seleniumClient;
    }

    private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
            RetrofitCrawler retrofit, Method method, Type returnType, Annotation[] annotations){
        try {
            //noinspection unchecked
            return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
        } catch (RuntimeException e) { // Wide exception range because factories are user code.
            throw Utils.methodError(method, e, "Unable to create call adapter for %s", returnType);
        }
    }

    private static <ResponseT>Converter<Document,ResponseT> createDocumentConverter(
            RetrofitCrawler retrofit, Method method, Type responseType){
        Annotation[] annotations = method.getAnnotations();
        try {
            return retrofit.documentTConverter(responseType, annotations);
        } catch (RuntimeException e) { // Wide exception range because factories are user code.
            throw Utils.methodError(method, e, "Unable to create converter for %s", responseType);
        }

    }

    //call适配代理
    static final class CallAdapted<ResponseT, ReturnT> extends JsoupServiceMethod<ResponseT, ReturnT>{
        private final CallAdapter<ResponseT, ReturnT> callAdapter;

        public CallAdapted(ConnectionFactory connectionFactory, ConnectionManage seleniumClient, CallAdapter<ResponseT, ReturnT> callAdapter, Converter<Document,ResponseT> documentConverter) {
            super(connectionFactory,seleniumClient,documentConverter);
            this.callAdapter = callAdapter;
        }

        @Override
        protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
            return callAdapter.adapt(call);
        }
    }
}
