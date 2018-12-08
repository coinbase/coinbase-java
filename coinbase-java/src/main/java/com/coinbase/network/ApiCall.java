/*
 * Copyright 2018 Coinbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase.network;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

/**
 * An invocation of an API call that sends a request to a Coinbase server and returns
 * a response.Each call yields its own HTTP request and response pair.
 * <p>
 * Calls may be executed synchronously with {@link #execute}, or asynchronously with {@link
 * #enqueue}. In either case the call can be canceled at any time with {@link #cancel}.
 * A call that is busy writing its request or reading its response may receive an {@link IOException};
 * this is working as designed.
 *
 * @param <T> Successful response body type.
 */
public class ApiCall<T> {

    private Call<T> originalCall;
    private Executor callbackExecutor;

    ApiCall(Call<T> originalCall, Executor callbackExecutor) {
        this.originalCall = originalCall;
        this.callbackExecutor = callbackExecutor;
    }

    /**
     * Synchronously send the request and return its response.
     *
     * @throws IOException      if a problem occurred talking to the server.
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request
     *                          or decoding the response.
     */
    public T execute() throws IOException {
        return originalCall.execute().body();
    }

    /**
     * Asynchronously send the request and notify {@link Callback callback} of its response or if
     * an error occurred talking to the server, creating the request, or processing the response.
     */
    public void enqueue(Callback<T> callback) {
        originalCall.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callbackExecutor.execute(() -> callback.onSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callbackExecutor.execute(() -> callback.onFailure(t));
            }
        });
    }

    /**
     * Returns {@code true} if this call has been either {@link #execute() executed} or {@link
     * #enqueue(Callback) enqueued}. It is an error to execute or enqueue a call more than once.
     */
    public boolean isExecuted() {
        return originalCall.isExecuted();
    }

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    public void cancel() {
        originalCall.cancel();
    }

    /**
     * True if {@link #cancel()} was called.
     */
    public boolean isCanceled() {
        return originalCall.isCanceled();
    }

}
