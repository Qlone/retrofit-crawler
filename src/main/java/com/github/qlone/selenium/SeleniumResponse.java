package com.github.qlone.selenium;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author heweinan
 * @date 2020-12-02 11:47
 */
public class SeleniumResponse implements Connection.Response {

    private String html;

    public SeleniumResponse(String html) {
        this.html = html;
    }

    @Override
    public int statusCode() {
        return 200;
    }

    @Override
    public String statusMessage() {
        return "success";
    }

    @Override
    public String charset() {
        return null;
    }

    @Override
    public Connection.Response charset(String charset) {
        return this;
    }

    @Override
    public String contentType() {
        return null;
    }

    @Override
    public Document parse() throws IOException {
        return Jsoup.parse(this.html);
    }

    @Override
    public String body() {
        return this.html;
    }

    @Override
    public byte[] bodyAsBytes() {
        return new byte[0];
    }

    @Override
    public Connection.Response bufferUp() {
        return null;
    }

    @Override
    public BufferedInputStream bodyStream() {
        return null;
    }

    @Override
    public URL url() {
        return null;
    }

    @Override
    public Connection.Response url(URL url) {
        return null;
    }

    @Override
    public Connection.Method method() {
        return null;
    }

    @Override
    public Connection.Response method(Connection.Method method) {
        return null;
    }

    @Override
    public String header(String name) {
        return null;
    }

    @Override
    public List<String> headers(String name) {
        return null;
    }

    @Override
    public Connection.Response header(String name, String value) {
        return null;
    }

    @Override
    public Connection.Response addHeader(String name, String value) {
        return null;
    }

    @Override
    public boolean hasHeader(String name) {
        return false;
    }

    @Override
    public boolean hasHeaderWithValue(String name, String value) {
        return false;
    }

    @Override
    public Connection.Response removeHeader(String name) {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public Map<String, List<String>> multiHeaders() {
        return null;
    }

    @Override
    public String cookie(String name) {
        return null;
    }

    @Override
    public Connection.Response cookie(String name, String value) {
        return this;
    }

    @Override
    public boolean hasCookie(String name) {
        return false;
    }

    @Override
    public Connection.Response removeCookie(String name) {
        return this;
    }

    @Override
    public Map<String, String> cookies() {
        return null;
    }
}
