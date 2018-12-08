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

/**
 * Communicates responses. One and only one method will be invoked in response to a given request.
 * <p>
 * Callbacks are executed on the application's main (UI) thread.
 *
 * @param <T> Successful response body type.
 */
public interface Callback<T> {

    /**
     * Invoked for a successful request.
     * <p>
     * Note: if an HTTP response contained failure such as a 404 or 500 {@link #onFailure} with
     * {@link com.coinbase.errors.CoinbaseException CoinbaseException} is invoked instead.
     */
    void onSuccess(T result);

    /**
     * Invoked when a network exception or error response (such as a 404 or 500) occurred talking to
     * the Coinbase server or when an unexpected exception occurred creating the request or
     * processing the response.
     */
    void onFailure(Throwable t);
}
