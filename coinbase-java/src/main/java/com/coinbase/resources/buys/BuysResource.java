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

package com.coinbase.resources.buys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.trades.Trade.ExpandField;
import com.coinbase.resources.trades.TradesApi;
import com.coinbase.resources.trades.TradesApiRx;
import com.coinbase.resources.trades.TradesResource;
import com.coinbase.resources.trades.TransferOrderBody;

import io.reactivex.rxjava3.core.Single;

/**
 * Represents a resource for working with Buys: listing, placing orders and committing.
 * <p>
 * Buys can be started with {@code commit: false} which is useful when displaying the confirmation for a buy.
 * These buys will never complete and receive an associated transaction unless they are committed separately.
 * <p>
 * When using this endpoint, it is possible that our system will not be able to process the buy as normal.
 * If this is the case, client will get {@link com.coinbase.errors.CoinbaseException} with code {@code 400} and id an id of {@code unknown_error}.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#buys">online docs: Buys</a> for more info.
 */
public class BuysResource extends TradesResource<Buy> {

    public BuysResource(TradesApi tradesApi, TradesApiRx tradesApiRx) {
        super(tradesApi, tradesApiRx, ApiConstants.BUYS);
    }

    //region ApiCall methods

    /**
     * Lists buys for an account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Buys#READ wallet:buys:read}</li>
     * </ul>
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters} for this request.
     * @param expandFields     (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting list of buys.
     * @see <a href="https://developers.coinbase.com/api/v2#list-buys">online docs: List Buys</a>.
     */
    public ApiCall<PagedResponse<Buy>> listBuys(@NonNull String accountId,
                                                @NonNull PaginationParams paginationParams,
                                                @Nullable ExpandField... expandFields) {
        return listTrades(accountId, paginationParams, expandFields);
    }

    /**
     * Same as {@link #listBuys(String, PaginationParams, ExpandField...) listBuys()} with default
     * {@link PaginationParams pagination parameters}.
     *
     * @param accountId    id of the account.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting list of buys.
     */
    public ApiCall<PagedResponse<Buy>> listBuys(String accountId,
                                                ExpandField... expandFields) {
        return listTrades(accountId, expandFields);
    }

    /**
     * Shows an individual buy.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Buys#READ wallet:buys:read}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param buyId        id of the buy.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting buy information.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-buy">online docs: Show a buy</a>.
     */
    public ApiCall<CoinbaseResponse<Buy>> showBuy(@NonNull String accountId,
                                                  @NonNull String buyId,
                                                  @Nullable ExpandField... expandFields) {
        return showTrade(accountId, buyId, expandFields);
    }

    /**
     * Buys a user-defined amount of bitcoin, bitcoin cash, litecoin or ethereum.
     * <p>
     * There are two ways to define buy amounts – you can use either the amount or the total parameter:
     * <ol>
     * <li>
     * When supplying {@link TransferOrderBody#withAmount(String, String, String) amount},
     * you’ll get the amount of bitcoin, bitcoin cash, litecoin or ethereum defined.
     * With {@code amount} it’s recommended to use BTC or ETH as the currency value, but you can always specify
     * a fiat currency and and the amount will be converted to BTC or ETH respectively.
     * </li>
     * <li>
     * When supplying {@link TransferOrderBody#withTotal(String, String, String) total},
     * your payment method will be debited the total amount and you’ll get the amount
     * in BTC or ETH after fees have been reduced from the total.
     * With {@code total} it’s recommended to use the currency of the payment method as the currency parameter,
     * but you can always specify a different currency and it will be converted.
     * </li>
     * </ol>
     * <p>
     * Given the price of digital currency depends on the time of the call and on the amount of purchase,
     * it’s recommended to use the {@code commit: false} parameter to create an uncommitted buy
     * to show the confirmation for the user or get the final quote, and commit that with a
     * {@link #commitBuyOrder(String, String, ExpandField...) separate request}.
     * <p>
     * If you need to query the buy price without locking in the buy, you can use {@link TransferOrderBody#setQuote(Boolean)} quote: true} option.
     * This returns an unsaved buy and unlike {@code commit: false}, <b>this buy can’t be completed</b>.
     * This option is useful when you need to show the detailed buy price quote for the user
     * when they are filling a form or similar situation.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Buys#CREATE wallet:buys:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param body         {@link TransferOrderBody request body}.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for placing a buy order.
     * @see <a href="https://developers.coinbase.com/api/v2#place-buy-order">online docs: Place Buy Order</a>.
     */
    public ApiCall<CoinbaseResponse<Buy>> placeBuyOrder(@NonNull String accountId,
                                                        @NonNull TransferOrderBody body,
                                                        @Nullable ExpandField... expandFields) {
        return placeTradeOrder(accountId, body, expandFields);
    }

    /**
     * Completes a buy that is created in {@code commit: false} state.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Buys#CREATE wallet:buys:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param buyId        id of the buy.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for completing uncommitted buy order.
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-buy">online docs: Commit a Buy</a>.
     */
    public ApiCall<CoinbaseResponse<Buy>> commitBuyOrder(@NonNull String accountId,
                                                         @NonNull String buyId,
                                                         @Nullable ExpandField... expandFields) {
        return commitTradeOrder(accountId, buyId, expandFields);
    }

    //endregion

    //region Rx Methods

    /**
     * Rx version of {@link #listBuys(String, PaginationParams, ExpandField...) listBuys(accountId, paginationParams, expandFields}.
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters} for this request.
     * @param expandFields     (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting list of buys.
     */
    public Single<PagedResponse<Buy>> listBuysRx(@NonNull String accountId,
                                                 @NonNull PaginationParams paginationParams,
                                                 @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, paginationParams, expandFields);
    }

    /**
     * Rx version of {@link #listBuys(String, ExpandField...) listBuys(accountId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting list of buys.
     */
    public Single<PagedResponse<Buy>> listBuysRx(@NonNull String accountId,
                                                 @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, expandFields);
    }

    /**
     * Rx version of {@link #showBuy(String, String, ExpandField...) showBuy(accountId, buyId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param buyId        id of the buy.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting buy information.
     */
    public Single<CoinbaseResponse<Buy>> showBuyRx(@NonNull String accountId,
                                                   @NonNull String buyId,
                                                   @Nullable ExpandField... expandFields) {
        return showTradeRx(accountId, buyId, expandFields);
    }

    /**
     * Rx version of {@link #placeBuyOrder(String, TransferOrderBody, ExpandField...) placeBuyOrder(accountId, body, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param body         {@link TransferOrderBody request body}.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for placing buy order.
     */
    public Single<CoinbaseResponse<Buy>> placeBuyOrderRx(@NonNull String accountId,
                                                         @NonNull TransferOrderBody body,
                                                         @Nullable ExpandField... expandFields) {
        return placeTradeOrderRx(accountId, body, expandFields);
    }

    /**
     * Rx version of {@link #commitBuyOrder(String, String, ExpandField...) commitBuyOrder(accountId, buyId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param buyId        id of the buy.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for completing uncommitted buy order.
     */
    public Single<CoinbaseResponse<Buy>> commitBuyOrderRx(@NonNull String accountId,
                                                          @NonNull String buyId,
                                                          @Nullable ExpandField... expandFields) {
        return commitTradeOrderRx(accountId, buyId, expandFields);
    }

    //endregion
}
