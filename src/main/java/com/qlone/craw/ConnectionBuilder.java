package com.qlone.craw;

import com.sun.istack.internal.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;


/**
 * @author heweinan
 * @date 2020-10-27 10:27
 */
final class ConnectionBuilder {

    private String baseUrl;
    private Connection.Method method;
    private String relativeUrl;

    private Proxy proxy;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private @Nullable
    String requestBody;

    ConnectionBuilder(String method, String baseUrl, String relativeUrl, Proxy proxy) {
        this.method = Connection.Method.valueOf(method);
        this.baseUrl = baseUrl;
        this.relativeUrl = relativeUrl;
        this.proxy = proxy;
        headers = new HashMap<>();
        cookies = new HashMap<>();
    }

    protected void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    protected void addHeader(String name, String value) {
        headers.put(name, value);
    }

    protected void addCookie(String name, String value) {
        cookies.put(name,value);
    }

    Connection get() {
        //TODO:先简单拼接
        Connection connection = Jsoup.connect(baseUrl + relativeUrl);
        connection.method(method);

        if (proxy != null) {
            connection.proxy(proxy);
        }

        if (method.hasBody()) {
            connection.requestBody(requestBody);
        }
        connection.headers(headers);
        connection.cookies(cookies);

        return connection;
    }

}
