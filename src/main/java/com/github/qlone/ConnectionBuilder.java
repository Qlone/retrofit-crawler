package com.github.qlone;

import com.github.qlone.selenium.SeleniumDriverBuilder;
import com.github.qlone.selenium.SeleniumConnection;
import com.github.qlone.selenium.SeleniumScript;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author heweinan
 * @date 2020-10-27 10:27
 */
final class ConnectionBuilder {

    private String baseUrl;
    private String relativeUrl;
    private SeleniumDriverBuilder seleniumBuilder;
    private Connection.Method method;
    private Proxy proxy;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> data;
    private List<SeleniumScript> seleniumScripts;
    private String requestBody;

    ConnectionBuilder(String method, String baseUrl, String relativeUrl, Proxy proxy, SeleniumDriverBuilder webDriver) {
        this.method = Connection.Method.valueOf(method);
        this.baseUrl = baseUrl;
        this.relativeUrl = relativeUrl;
        this.proxy = proxy;
        headers = new HashMap<>();
        cookies = new HashMap<>();
        data= new HashMap<>();
        seleniumScripts = new ArrayList<>();
        this.seleniumBuilder = webDriver;
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

    protected void addJavaScript(String javascript,Object argument, int blockTime, TimeUnit blockTimeUnit){
        Collection<?> arguments;
        if(Collections.class.isAssignableFrom(argument.getClass())){
            arguments = (Collection) argument;
        }else {
            Object[] objects = {argument};
            arguments = Arrays.asList(objects);
        }
        this.addJavaScripts(javascript,arguments,blockTime,blockTimeUnit);
    }

    private void addJavaScripts(String javascript, Collection<?> arguments, int blockTime, TimeUnit blockTimeUnit){
        this.seleniumScripts.add(new SeleniumScript(javascript,arguments,blockTime,blockTimeUnit));
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
        Connection connection;

        String url = (relativeUrl == null ? baseUrl : baseUrl + relativeUrl);
        if(seleniumBuilder != null){
            connection = new SeleniumConnection(seleniumBuilder);
            connection.url(url);
            ((SeleniumConnection)connection).addJavaScript(this.seleniumScripts);

        }else {
            connection = Jsoup.connect(url);
            connection.method(method);
            if (proxy != null) {
                connection.proxy(proxy);
            }
            if (method.hasBody()) {
                connection.requestBody(requestBody);
            }
            connection.headers(headers);
            connection.data(data);
        }

        connection.cookies(cookies);
        return connection;
    }

}
