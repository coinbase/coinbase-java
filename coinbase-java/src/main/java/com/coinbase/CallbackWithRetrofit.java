package com.coinbase;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alexshoykhet on 12/7/16.
 */

public interface CallbackWithRetrofit<T> {
    void onResponse(Call<T> call, Response<T> response, Retrofit retrofit);
    void onFailure(Call<T> call, Throwable t);
}
