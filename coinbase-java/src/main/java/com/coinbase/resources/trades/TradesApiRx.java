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

package com.coinbase.resources.trades;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;

import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Rx API Interface for Buys, Sells, Deposits and Withdrawals
 *
 * @param <T> trade type
 * @see com.coinbase.resources.buys.BuysResource
 * @see com.coinbase.resources.sells.SellsResource
 * @see com.coinbase.resources.deposits.DepositsResource
 * @see com.coinbase.resources.withdrawals.WithdrawalsResource
 */
@SuppressWarnings("unused")
public interface TradesApiRx<T extends Trade> {

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/{trade_type}")
    Single<PagedResponse<Trade>> listTradesRx(
            @Path("account_id") String accountId,
            @Path("trade_type") String tradeType,
            @QueryMap Map<String, String> query,
            @Query("expand[]") Set<String> expandOptions
    );

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/{trade_type}/{trade_id}")
    Single<CoinbaseResponse<Trade>> showTradeRx(
            @Path("account_id") String accountId,
            @Path("trade_type") String tradeType,
            @Path("trade_id") String tradeId,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/{trade_type}")
    Single<CoinbaseResponse<Trade>> placeTradeOrderRx(
            @Path("account_id") String accountId,
            @Path("trade_type") String tradeType,
            @Body PlaceTradeOrderBody placeTradeOrderBody,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/{trade_type}/{trade_id}/" + ApiConstants.COMMIT)
    Single<CoinbaseResponse<Trade>> commitTradeOrderRx(
            @Path("account_id") String accountId,
            @Path("trade_type") String tradeType,
            @Path("trade_id") String tradeId,
            @Query("expand[]") Set<String> expandOptions
    );
}
