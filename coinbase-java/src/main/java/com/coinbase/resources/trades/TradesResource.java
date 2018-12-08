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

import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.trades.Trade.ExpandField;

import java.util.Collections;

import io.reactivex.Single;

import static com.coinbase.resources.ExpandUtils.toValueSet;

/**
 * Abstract class for Trade resources.
 *
 * @param <T> trade type.
 * @see com.coinbase.resources.buys.BuysResource
 * @see com.coinbase.resources.sells.SellsResource
 * @see com.coinbase.resources.deposits.DepositsResource
 * @see com.coinbase.resources.withdrawals.WithdrawalsResource
 */
@SuppressWarnings("unchecked")
public abstract class TradesResource<T extends Trade> {

    private final TradesApi tradesApi;
    private final TradesApiRx tradesApiRx;
    private final String tradeType;

    public TradesResource(TradesApi tradesApi, TradesApiRx tradesApiRx, String tradeType) {
        this.tradesApi = tradesApi;
        this.tradesApiRx = tradesApiRx;
        this.tradeType = tradeType;
    }

    protected ApiCall<PagedResponse<T>> listTrades(String accountId, ExpandField... expandFields) {
        return listTrades(accountId, null, expandFields);
    }

    protected ApiCall<PagedResponse<T>> listTrades(
            String accountId,
            PaginationParams paginationParams,
            ExpandField... expandFields
    ) {
        return tradesApi.listTrades(
                accountId,
                tradeType,
                paginationParams != null ? paginationParams.toQueryMap() : Collections.emptyMap(),
                toValueSet(expandFields)
        );
    }

    protected ApiCall<CoinbaseResponse<T>> showTrade(
            String accountId,
            String tradeId,
            ExpandField... expandFields) {
        return tradesApi.showTrade(accountId, tradeType, tradeId, toValueSet(expandFields));
    }

    protected ApiCall<CoinbaseResponse<T>> placeTradeOrder(
            String accountId,
            PlaceTradeOrderBody body,
            ExpandField... expandFields) {
        return tradesApi.placeTradeOrder(
                accountId,
                tradeType,
                body,
                toValueSet(expandFields)
        );
    }

    protected ApiCall<CoinbaseResponse<T>> commitTradeOrder(
            String accountId,
            String tradeId,
            ExpandField... expandFields) {
        return tradesApi.commitTradeOrder(accountId, tradeType, tradeId, toValueSet(expandFields));
    }

    protected Single<PagedResponse<T>> listTradesRx(
            String accountId,
            ExpandField... expandFields) {
        return listTradesRx(accountId, null, expandFields);
    }

    protected Single<PagedResponse<T>> listTradesRx(
            String accountId,
            PaginationParams paginationParams,
            ExpandField... expandFields) {
        return tradesApiRx.listTradesRx(
                accountId,
                tradeType,
                paginationParams != null ? paginationParams.toQueryMap() : Collections.emptyMap(),
                toValueSet(expandFields)
        );
    }

    protected Single<CoinbaseResponse<T>> showTradeRx(
            String accountId,
            String tradeId,
            ExpandField... expandFields) {
        return tradesApiRx.showTradeRx(accountId, tradeType, tradeId, toValueSet(expandFields));
    }

    protected Single<CoinbaseResponse<T>> placeTradeOrderRx(
            String accountId,
            PlaceTradeOrderBody body,
            ExpandField... expandFields) {
        return tradesApiRx.placeTradeOrderRx(
                accountId,
                tradeType,
                body,
                toValueSet(expandFields)
        );
    }

    protected Single<CoinbaseResponse<T>> commitTradeOrderRx(
            String accountId,
            String tradeId,
            ExpandField... expandFields) {
        return tradesApiRx.commitTradeOrderRx(accountId, tradeType, tradeId, toValueSet(expandFields));
    }
}
