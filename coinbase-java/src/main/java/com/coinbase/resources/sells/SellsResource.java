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

package com.coinbase.resources.sells;

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

import io.reactivex.Single;

/**
 * Represents a resource for working with Sells: listing, placing orders and committing.
 * <p>
 * Sells can be started with {@code commit: false} which is useful when displaying the confirmation for a sell.
 * These sells will never complete and receive an associated transaction unless they are committed separately.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#sells">online docs: Sells</a>.
 */
public class SellsResource extends TradesResource<Sell> {

    public SellsResource(TradesApi tradesApi, TradesApiRx tradesApiRx) {
        super(tradesApi, tradesApiRx, ApiConstants.SELLS);
    }

    //region ApiCall methods

    /**
     * Lists sells for an account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Sells#READ wallet:sells:read}</li>
     * </ul>
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters} for this request.
     * @param expandFields     (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting the list of sells.
     * @see <a href="https://developers.coinbase.com/api/v2#list-sells">online docs: List Sells</a>.
     */
    public ApiCall<PagedResponse<Sell>> listSells(@NonNull String accountId,
                                                  @NonNull PaginationParams paginationParams,
                                                  @Nullable ExpandField... expandFields) {
        return listTrades(accountId, paginationParams, expandFields);
    }

    /**
     * Same as {@link #listSells(String, PaginationParams, ExpandField...) listSells()} with
     * default pagination params.
     *
     * @param accountId    id of the account.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting the list of sells.
     * @see <a href="https://developers.coinbase.com/api/v2#list-sells">online docs: List Sells</a>.
     */
    public ApiCall<PagedResponse<Sell>> listSells(@NonNull String accountId,
                                                  @Nullable ExpandField... expandFields) {
        return listTrades(accountId, expandFields);
    }

    /**
     * Show an individual sell.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Sells#READ wallet:sells:read}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param sellId       id of the sell.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for getting sell information.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-sell">online docs: Show a Sell</a>.
     */
    public ApiCall<CoinbaseResponse<Sell>> showSell(@NonNull String accountId,
                                                    @NonNull String sellId,
                                                    @Nullable ExpandField... expandFields) {
        return showTrade(accountId, sellId, expandFields);
    }

    /**
     * Sells a user-defined amount of bitcoin, bitcoin cash, litecoin or ethereum.
     * <p>
     * There are two ways to define sell amounts – you can use either the amount or the total parameter:
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
     * Given the price of digital currency depends on the time of the call and amount of the sell, it’s recommended
     * to use the {@code commit: false} parameter to create an uncommitted sell to get a quote and
     * then to commit that with a {@link #commitSellOrder(String, String, ExpandField...) separate request}.
     * <p>
     * If you need to query the sell price without locking in the sell,
     * you can use {@link TransferOrderBody#setQuote(Boolean)} quote: true} option.
     * This returns an unsaved sell and unlike {@code commit: false}, this sell can’t be completed.
     * This option is useful when you need to show the detailed sell price quote for the user when they are filling a form or similar situation.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Sells#CREATE wallet:sells:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param body         {@link TransferOrderBody request body}.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for placing a sell order.
     * @see <a href="https://developers.coinbase.com/api/v2#place-sell-order">online docs: Place Sell Order</a>.
     */
    public ApiCall<CoinbaseResponse<Sell>> placeSellOrder(@NonNull String accountId,
                                                          @NonNull TransferOrderBody body,
                                                          @Nullable ExpandField... expandFields) {
        return placeTradeOrder(accountId, body, expandFields);
    }

    /**
     * Completes a sell that is created in {@code commit: false} state.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Sells#READ wallet:sells:read}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param sellId       id of the sell.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link ApiCall call} for committing the sell order.
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-sell">online docs: Commit a Sell</a>.
     */
    public ApiCall<CoinbaseResponse<Sell>> commitSellOrder(@NonNull String accountId,
                                                           @NonNull String sellId,
                                                           @Nullable ExpandField... expandFields) {
        return commitTradeOrder(accountId, sellId, expandFields);
    }

    //endregion

    //region Rx methods

    /**
     * Rx version of {@link #listSells(String, PaginationParams, ExpandField...) listSells(accountId, paginationParams, expandFields)}.
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters} for this request.
     * @param expandFields     (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting the list of sells.
     */
    public Single<PagedResponse<Sell>> listSellsRx(@NonNull String accountId,
                                                   @NonNull PaginationParams paginationParams,
                                                   @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, paginationParams, expandFields);
    }

    /**
     * Rx version of {@link #listSells(String, ExpandField...) listSells(accountId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting the list of sells.
     */
    public Single<PagedResponse<Sell>> listSellsRx(@NonNull String accountId,
                                                   @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, expandFields);
    }

    /**
     * Rx version of {@link #showSell(String, String, ExpandField...) showSell(accountId, sellId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param sellId       id of the sell.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for getting sell information.
     */
    public Single<CoinbaseResponse<Sell>> showSellRx(@NonNull String accountId,
                                                     @NonNull String sellId,
                                                     @Nullable ExpandField... expandFields) {
        return showTradeRx(accountId, sellId, expandFields);
    }

    /**
     * Rx version of {@link #placeSellOrder(String, TransferOrderBody, ExpandField...) placeSellOrder(accountId, body, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param body         {@link TransferOrderBody request body}.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for placing a sell order.
     */
    public Single<CoinbaseResponse<Sell>> placeSellOrderRx(@NonNull String accountId,
                                                           @NonNull TransferOrderBody body,
                                                           @Nullable ExpandField... expandFields) {
        return placeTradeOrderRx(accountId, body, expandFields);
    }

    /**
     * Rx version of {@link #commitSellOrder(String, String, ExpandField...) commitSellOrder(accountId, sellId, expandFields)}.
     *
     * @param accountId    id of the account.
     * @param sellId       id of the sell.
     * @param expandFields (optional) {@link com.coinbase.resources.trades.Trade.ExpandField expand fields} options.
     * @return {@link Single rxSingle} for committing the sell order.
     */
    public Single<CoinbaseResponse<Sell>> commitSellOrderRx(@NonNull String accountId,
                                                            @NonNull String sellId,
                                                            @Nullable ExpandField... expandFields) {
        return commitTradeOrderRx(accountId, sellId, expandFields);
    }

    //endregion

}
