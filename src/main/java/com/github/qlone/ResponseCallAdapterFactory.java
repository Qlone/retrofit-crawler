package com.github.qlone;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author heweinan
 * @date 2020-12-03 16:46
 */
public class ResponseCallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, RetrofitCrawler retrofitCrawler) {
        if (Utils.getRawType(returnType) != Response.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Response return type must be parameterized as Response<Foo> or Response<? extends Foo>");
        }
        Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);

        return new ResponseAdpater(responseType);
    }
    public static final class ResponseAdpater implements CallAdapter<Object,Response<?>>{

        private Type responseType;

        public ResponseAdpater(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Response<?> adapt(Call<Object> call) {
            try {
                return call.execute();
            } catch (IOException e) {
                //TODO: log something
                e.printStackTrace();
            }
            return Response.error(null);
        }
    }
}
