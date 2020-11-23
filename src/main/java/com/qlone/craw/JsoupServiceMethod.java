package com.qlone.craw;

import com.sun.istack.internal.Nullable;
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

        return new CallAdapted<>(connectionFactory,callAdapter,documentConverter);
    }

    ReturnT invoke(Object[] args) {
        Call<ResponseT>  call = new JsoupCall<>(connectionFactory,args,documentConverter);
        return adapt(call,args);
    }

    protected abstract @Nullable
    ReturnT adapt(Call<ResponseT> call, Object[] args);

    private ConnectionFactory connectionFactory;
    private Converter<Document,ResponseT> documentConverter;

    public JsoupServiceMethod(ConnectionFactory connectionFactory,Converter<Document,ResponseT> documentConverter) {
        this.connectionFactory = connectionFactory;
        this.documentConverter = documentConverter;
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

        public CallAdapted(ConnectionFactory connectionFactory,CallAdapter<ResponseT, ReturnT> callAdapter,Converter<Document,ResponseT> documentConverter) {
            super(connectionFactory,documentConverter);
            this.callAdapter = callAdapter;
        }

        @Override
        protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
            return callAdapter.adapt(call);
        }
    }
}