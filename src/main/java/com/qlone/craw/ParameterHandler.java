package com.qlone.craw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author heweinan
 * @date 2020-10-27 10:04
 */
abstract class ParameterHandler<T> {
    abstract void apply(ConnectionBuilder builder, @Nullable T value) throws IOException;

    protected static class Path extends ParameterHandler<String>{

        @Override
        void apply(ConnectionBuilder builder, String value) throws IOException {
            if(value == null) return;
            builder.setUrl(value);
        }
    }

    protected static class RelativeUrl extends ParameterHandler<String>{

        @Override
        void apply(ConnectionBuilder builder, String value) throws IOException {
            if(value == null) return;
            builder.setRelativeUrl(value);
        }
    }

    protected static class Header extends ParameterHandler<String>{

        private String name;

        public Header(String name) {
            this.name = name;
        }

        @Override
        void apply(ConnectionBuilder builder, String value) throws IOException {
            if(value == null) return;
            builder.addHeader(name,value);
        }
    }

    protected static class HeaderMap extends ParameterHandler<HashMap<String,String>>{

        @Override
        void apply(ConnectionBuilder builder, HashMap<String, String> value) throws IOException {
            if(value == null) return;
            builder.addHeaders(value);
        }
    }

    protected static class Cookie extends ParameterHandler<String>{

        private String name;

        public Cookie(String name) {
            this.name = name;
        }

        @Override
        void apply(ConnectionBuilder builder, String value) throws IOException {
            if(value == null) return;
            builder.addCookie(name,value);
        }
    }

    protected static class CookieMap extends ParameterHandler<HashMap<String,String>>{

        @Override
        void apply(ConnectionBuilder builder, HashMap<String, String> value) throws IOException {
            if(value == null) return;
            builder.addCookies(value);
        }
    }

    protected static class Query extends ParameterHandler<Object>{

        private String name;

        public Query(String name) {
            this.name = name;
        }

        @Override
        void apply(ConnectionBuilder builder, Object value) throws IOException {
            if(value == null) return;
            builder.addData(name,value);
        }
    }

    protected static class QueryMap extends ParameterHandler<HashMap<String,String>>{

        @Override
        void apply(ConnectionBuilder builder, HashMap<String, String> value) throws IOException {
            if(value == null) return;
            builder.addDatas(value);
        }
    }

    protected static class Body extends ParameterHandler<Object>{

        @Override
        void apply(ConnectionBuilder builder, Object value) throws IOException {
            //TODO: 暂时先用json解析
            if(value == null) return;
            String body = JSONObject.toJSONString(value);
            builder.setRequestBody(body);
        }
    }
}
