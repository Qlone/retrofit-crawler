package com.github.qlone;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author heweinan
 * @date 2020-10-26 18:21
 */
public class JsoupCall<T> implements Call<T> {
    private final ConnectionFactory connectionFactory;
    private final Converter<Document, T> documentTConverter;
    private final Object[] args;

    private Connection rawConnection;
    private volatile boolean canceled;
    private boolean executed;

    public JsoupCall(ConnectionFactory connectionFactory, Object[] args, Converter<Document, T> documentTConverter) {
        this.connectionFactory = connectionFactory;
        this.args = args;
        this.documentTConverter = documentTConverter;
    }

    private Connection getCrawConnection() throws IOException {
        Connection connection = rawConnection;
        if (connection != null) {
            return connection;
        }

        try {
            return rawConnection = createRawCall();
        } catch (IOException e) {
            //would do something,not this version
            throw e;
        }
    }

    private Connection createRawCall() throws IOException {
        Connection connection = connectionFactory.create(args);
        if (connection == null) {
            throw new NullPointerException("ConnectionFactory returned null.");
        }
        return connection;
    }

    @Override
    public Response<T> execute() throws IOException {

        Connection connection = getCrawConnection();
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;
        }

        return parseResponse(connection.execute());
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        Connection connection;
        Throwable failure = null;
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;
            connection = rawConnection;
            if (connection == null) {
                try {
                    connection = rawConnection = createRawCall();
                } catch (Throwable e) {
                    failure = e;
                }
            }
        }
        if (failure != null) {
            callback.onFailure(this, failure);
            return;
        }

        final Connection connectionInner = connection;
        Thread callThread = new Thread(() -> {
            try {
                Connection.Response response = connectionInner.execute();
                callback.onResponse(this, parseResponse(response));
            } catch (IOException e) {
                callback.onFailure(JsoupCall.this, e);
            }
        });
        callThread.start();
    }

    Response<T> parseResponse(Connection.Response rawReponse) throws IOException {

        int code = rawReponse.statusCode();
        if (code < 200 || code >= 300) {
            return Response.error(rawReponse);
        }
        if (code == 204 || code == 205) {
            return Response.success(null, rawReponse);
        }

        T convert = documentTConverter.convert(rawReponse.parse());
        return Response.success(convert, rawReponse);

    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public JsoupCall<T> clone() {
        return new JsoupCall<>(connectionFactory, args, documentTConverter);
    }
}
