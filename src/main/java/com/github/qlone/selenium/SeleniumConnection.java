package com.github.qlone.selenium;

import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author heweinan
 * @date 2020-12-02 11:04
 */
public class SeleniumConnection implements Connection {
    private URL url;
    private int pageLoadTimeout;
    private SeleniumDriverBuilder seleniumBuilder;
    private List<SeleniumScript> seleniumScriptList;
    private Map<String, String> cookies = new HashMap<>();

    public SeleniumConnection(SeleniumDriverBuilder seleniumBuilder) {
        this.seleniumBuilder = seleniumBuilder;
        this.seleniumScriptList = new ArrayList<>();
    }

    @Override
    public Connection url(URL url) {
        this.url = url;
        return this;
    }

    @Override
    public Connection url(String url) {
        Validate.notEmpty(url, "Must supply a valid URL");
        try {
            this.url = new URL(encodeUrl(url));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + url, e);
        }
        return this;
    }

    private static String encodeUrl(String url) {
        try {
            URL u = new URL(url);
            return encodeUrl(u).toExternalForm();
        } catch (Exception e) {
            return url;
        }
    }

    static URL encodeUrl(URL u) {
        try {
            //  odd way to encode urls, but it works!
            String urlS = u.toExternalForm(); // URL external form may have spaces which is illegal in new URL() (odd asymmetry)
            urlS = urlS.replace(" ", "%20");
            final URI uri = new URI(urlS);
            return new URL(uri.toASCIIString());
        } catch (URISyntaxException | MalformedURLException e) {
            // give up and return the original input
            return u;
        }
    }

    @Override
    public Connection proxy(Proxy proxy) {
        return this;
    }

    @Override
    public Connection proxy(String host, int port) {
        return this;
    }

    @Override
    public Connection userAgent(String userAgent) {
        return this;
    }

    @Override
    public Connection timeout(int millis) {
        pageLoadTimeout = millis;
        return this;
    }

    @Override
    public Connection maxBodySize(int bytes) {
        return this;
    }

    @Override
    public Connection referrer(String referrer) {
        return this;
    }

    @Override
    public Connection followRedirects(boolean followRedirects) {
        return this;
    }

    @Override
    public Connection method(Method method) {
        return this;
    }

    @Override
    public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
        return this;
    }

    @Override
    public Connection ignoreContentType(boolean ignoreContentType) {
        return this;
    }

    @Override
    public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        return this;
    }

    @Override
    public Connection data(String key, String value) {
        return this;
    }

    @Override
    public Connection data(String key, String filename, InputStream inputStream) {
        return this;
    }

    @Override
    public Connection data(String key, String filename, InputStream inputStream, String contentType) {
        return this;
    }

    @Override
    public Connection data(Collection<KeyVal> data) {
        return this;
    }

    @Override
    public Connection data(Map<String, String> data) {
        return this;
    }

    @Override
    public Connection data(String... keyvals) {
        return this;
    }

    @Override
    public KeyVal data(String key) {
        return null;
    }

    @Override
    public Connection requestBody(String body) {
        return null;
    }

    @Override
    public Connection header(String name, String value) {
        return this;
    }

    @Override
    public Connection headers(Map<String, String> headers) {
        return this;
    }

    @Override
    public Connection cookie(String name, String value) {
        this.cookies.put(name,value);
        return this;
    }

    @Override
    public Connection cookies(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
        return this;
    }

    @Override
    public Connection parser(Parser parser) {
        return this;
    }

    @Override
    public Connection postDataCharset(String charset) {
        return this;
    }

    @Override
    public Document get() throws IOException {
        Response execute = execute();
        return execute.parse();
    }

    @Override
    public Document post() throws IOException {
        return null;
    }

    @Override
    public Response execute() throws IOException {
        WebDriver webDriver = seleniumBuilder.get();
        if(pageLoadTimeout > 0){
            webDriver.manage().timeouts().pageLoadTimeout(this.pageLoadTimeout, TimeUnit.MILLISECONDS);
        }
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            webDriver.manage().addCookie(new Cookie(entry.getKey(), entry.getValue()));
        }

        webDriver.get(url.toString());
        for(SeleniumScript seleniumScript: this.seleniumScriptList){
            ((JavascriptExecutor) webDriver).executeScript(seleniumScript.getJavascript(),seleniumScript.getArgument());
            if(seleniumScript.getBlockTime() > 0){
                try {
                    seleniumScript.getBlockTimUnit().sleep(seleniumScript.getBlockTime());
                } catch (InterruptedException e) {
                    //TODO: log something
                    e.printStackTrace();
                }
            }
        }
        String pageSource = webDriver.getPageSource();
        return new SeleniumResponse(pageSource);
    }

    @Override
    public Request request() {
        return null;
    }

    @Override
    public Connection request(Request request) {
        return null;
    }

    @Override
    public Response response() {
        return null;
    }

    @Override
    public Connection response(Response response) {
        return null;
    }

    public Connection addJavaScript(SeleniumScript seleniumScript){
        this.seleniumScriptList.add(seleniumScript);
        return this;
    }

    public Connection addJavaScript(List<SeleniumScript> seleniumScripts){
        this.seleniumScriptList.addAll(seleniumScripts);
        return this;
    }
}
