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

package com.coinbase.resources.rates;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Rx API interface for getting exchange rates.
 *
 * @see ExchangeRatesResource
 */
public interface ExchangeRatesApiRx {

    @GET(ApiConstants.EXCHANGE_RATES)
    Single<CoinbaseResponse<ExchangeRates>> getExchangeRates(@Query("currency") String currency);

}
