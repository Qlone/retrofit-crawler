package com.qlone.craw;

import com.sun.istack.internal.Nullable;
import org.jsoup.Connection;

import java.util.Objects;

/**
 * @author heweinan
 * @date 2020-10-26 17:35
 */
public class Response<T> {

    public static <T> Response<T> success(@Nullable T body, Connection.Response rawResponse) {
        Objects.requireNonNull(rawResponse, "rawResponse == null");
        int code = rawResponse.statusCode();
        if (code < 200 || code >= 300) {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        return new Response<>(rawResponse, body);
    }

    public static <T> Response<T> error(Connection.Response rawResponse) {
        Objects.requireNonNull(rawResponse, "body == null");
        int code = rawResponse.statusCode();
        if (code < 400) throw new IllegalArgumentException("code < 400: " + code);
        return new Response<>(rawResponse, null);
    }

    private Connection.Response response;
    private @Nullable T body;

    private Response(Connection.Response response, T body) {
        this.response = response;
        this.body = body;
    }

    public int code() {
        return response.statusCode();
    }

    public @Nullable T body(){
        return body;
    }

}
