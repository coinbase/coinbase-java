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

package com.coinbase.resources.paymentmethods;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.resources.paymentmethods.models.PaymentMethod;

import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Rx API interface for getting payment methods
 */
public interface PaymentMethodsApiRx {

    @GET(ApiConstants.PAYMENT_METHODS)
    Single<PagedResponse<PaymentMethod>> getPaymentMethods(@Query("expand[]") Set<String> expandOptions);

    @GET(ApiConstants.PAYMENT_METHODS)
    Single<PagedResponse<PaymentMethod>> getPaymentMethods(@Query("expand[]") Set<String> expandOptions,
                                                           @QueryMap Map<String, String> queryParams);

    @GET(ApiConstants.PAYMENT_METHODS + "/{id}")
    Single<CoinbaseResponse<PaymentMethod>> getPaymentMethod(@Path("id") String paymentMethodId, @Query("expand[]") Set<String> expandOptions);

    @DELETE(ApiConstants.PAYMENT_METHODS + "/{id}")
    Single<CoinbaseResponse<Void>> deletePaymentMethod(@Path("id") String paymentMethodId);
}
