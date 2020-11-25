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
    private String relativeUrl;
    private Connection.Method method;
    private Proxy proxy;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> data;
    private @Nullable
    String requestBody;

    ConnectionBuilder(String method, String baseUrl, String relativeUrl, Proxy proxy) {
        this.method = Connection.Method.valueOf(method);
        this.baseUrl = baseUrl;
        this.relativeUrl = relativeUrl;
        this.proxy = proxy;
        headers = new HashMap<>();
        cookies = new HashMap<>();
        data= new HashMap<>();
    }

    protected void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    protected void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    protected void addHeaders(HashMap<String,String> headers){
        this.headers.putAll(headers);
    }

    protected void addCookie(String name, String value) {
        cookies.put(name,value);
    }

    protected void addCookies(HashMap<String,String> cookies){
        this.cookies.putAll(cookies);
    }

    protected void addData(String name, Object value){

        String string = value.toString();
        this.data.put(name,string);
    }

    protected void addDatas(HashMap<String,String> datas){
        this.data.putAll(datas);
    }

    protected void setUrl(String url){
        this.baseUrl = url;
        this.relativeUrl = "";
    }

    protected void setRelativeUrl(String relativeUrl){
        this.relativeUrl =relativeUrl;
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
        connection.data(data);
        return connection;
    }

}
