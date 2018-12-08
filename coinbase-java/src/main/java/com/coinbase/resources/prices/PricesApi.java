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
import com.coinbase.network.ApiCall;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API interface for getting prices of supported crypto currencies.
 *
 * @see PricesResource
 */
public interface PricesApi {

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.BUY)
    ApiCall<CoinbaseResponse<Price>> getBuyPrice(@Path("base_currency") String baseCurrency,
                                                 @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SELL)
    ApiCall<CoinbaseResponse<Price>> getSellPrice(@Path("base_currency") String baseCurrency,
                                                  @Path("fiat_currency") String fiatCurrency);

    @GET(ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SPOT)
    ApiCall<CoinbaseResponse<Price>> getSpotPrice(@Path("base_currency") String baseCurrency,
                                                  @Path("fiat_currency") String fiatCurrency,
                                                  @Query("date") String date);

    @GET(ApiConstants.PRICES + "/{fiat_currency}/" + ApiConstants.SPOT)
    ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@Path("fiat_currency") String fiatCurrency,
                                                         @Query("date") String date);
}
