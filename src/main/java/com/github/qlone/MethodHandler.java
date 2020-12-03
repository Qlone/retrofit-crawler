package com.github.qlone;

import com.github.qlone.http.Javascript;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author heweinan
 * @date 2020-12-02 16:23
 */
public abstract class MethodHandler {
    abstract void apply(ConnectionBuilder builder);

    public static class JavascriptHandler extends MethodHandler {
        private String javascript;
        private String[] arguments;
        private int blockTime;
        private TimeUnit blockTimeUnit;


        public JavascriptHandler(String javascript, String[] arguments, int blockTime, TimeUnit blockTimeUnit) {
            this.javascript = javascript;
            this.arguments = arguments;
            this.blockTime = blockTime;
            this.blockTimeUnit = blockTimeUnit;
        }

        @Override
        void apply(ConnectionBuilder builder) {
            builder.addJavaScript(javascript,arguments,blockTime,blockTimeUnit);
        }
    }
}
