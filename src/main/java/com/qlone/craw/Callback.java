package com.qlone.craw;

/**
 * @author heweinan
 * @date 2020-10-26 17:39
 */
public interface Callback<T> {
    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call, Throwable t);
}
