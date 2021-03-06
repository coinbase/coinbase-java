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

package com.coinbase.resources.prices;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Rx API interface for getting prices of supported crypto currencies.
 *
 * @see PricesResource
 */
public interface PricesApiRx {

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SELL)
    Single<CoinbaseResponse<Price>> getSellPrice(@Path("base_currency") String baseCurrency,
                                                 @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.BUY)
    Single<CoinbaseResponse<Price>> getBuyPrice(@Path("base_currency") String baseCurrency,
                                                @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SPOT)
    Single<CoinbaseResponse<Price>> getSpotPrice(@Path("base_currency") String baseCurrency,
                                                 @Path("fiat_currency") String fiatCurrency,
                                                 @Query("date") String date);

    @GET(ApiConstants.PRICES + "/{fiat_currency}/" + ApiConstants.SPOT)
    Single<CoinbaseResponse<List<Price>>> getSpotPrices(@Path("fiat_currency") String fiatCurrency,
                                                        @Query("date") String date);

}
