package com.github.qlone;

import java.io.IOException;

/**
 * @author heweinan
 * @date 2020-10-26 17:33
 */
public interface Call<T> extends Cloneable {

    Response<T> execute() throws IOException;

    void enqueue(Callback<T> callback);

    boolean isExecuted();

    void cancel();

    boolean isCanceled();

    Call<T> clone();


}
