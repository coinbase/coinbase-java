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

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Adapter factory that provides adapter for coinbase {@link ApiCall calls}.
 */
public class ApiCallAdapterFactory extends CallAdapter.Factory {

    public static ApiCallAdapterFactory create() {
        return new ApiCallAdapterFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallAdapter<?, ApiCall<?>> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != ApiCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(ApiCall.class.getCanonicalName() + " must be parametrized with response type");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new CoinbaseCallAdapter(responseType, retrofit.callbackExecutor());
    }

    /**
     * Adapts a {@link Call} with response type {@code R} into the {@link ApiCall}.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static class CoinbaseCallAdapter implements CallAdapter<Object, ApiCall<?>> {

        private final Type responseType;
        private final Executor callbackExacutor;

        CoinbaseCallAdapter(Type responseType, Executor excetor) {
            this.responseType = responseType;
            this.callbackExacutor = excetor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type responseType() {
            return responseType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ApiCall<?> adapt(Call<Object> call) {
            return new ApiCall<>(call, callbackExacutor);
        }
    }

}
