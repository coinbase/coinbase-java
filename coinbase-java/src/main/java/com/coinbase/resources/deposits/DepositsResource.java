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

package com.coinbase.resources.deposits;

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

import io.reactivex.Single;

/**
 * Represents a resource for working with Deposits: listing, placing orders and committing.
 * <p>
 * Deposits can be started with {@code commit: false} which is useful when displaying the confirmation for a deposit.
 * These deposits will never complete and receive an associated transaction unless they are committed separately.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#deposits">online docs: Deposits</a> for more info.
 */
public class DepositsResource extends TradesResource<Deposit> {

    public DepositsResource(TradesApi tradesApi, TradesApiRx tradesApiRx) {
        super(tradesApi, tradesApiRx, ApiConstants.DEPOSITS);
    }

    //region ApiCall methods

    /**
     * Lists deposits for an account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Deposits#READ wallet:deposits:read}</li>
     * </ul>
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters}.
     * @param expandFields     {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for getting the list of deposits.
     * @see <a href="https://developers.coinbase.com/api/v2#list-deposits">online docs: List Deposits</a>.
     */
    public ApiCall<PagedResponse<Deposit>> listDeposits(@NonNull String accountId,
                                                        @NonNull PaginationParams paginationParams,
                                                        @Nullable ExpandField... expandFields) {
        return listTrades(accountId, paginationParams, expandFields);
    }

    /**
     * Same as {@link #listDeposits(String, PaginationParams, ExpandField...) listDeposits(accountId, expandFields)}
     * with default pagination params.
     *
     * @param accountId    id of the account.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for getting the list of deposits.
     * @see <a href="https://developers.coinbase.com/api/v2#list-deposits">online docs: List Deposits</a>.
     */
    public ApiCall<PagedResponse<Deposit>> listDeposits(@NonNull String accountId,
                                                        @Nullable ExpandField... expandFields) {
        return listDeposits(accountId, null, expandFields);
    }

    /**
     * Show an individual deposit.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Deposits#READ wallet:deposits:read}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param depositId    id of the deposit.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for showing the deposit.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-deposit">online docs: Show a Deposit</a>.
     */
    public ApiCall<CoinbaseResponse<Deposit>> showDeposit(@NonNull String accountId,
                                                          @NonNull String depositId,
                                                          @Nullable ExpandField... expandFields) {
        return showTrade(accountId, depositId, expandFields);
    }

    /**
     * Deposits user-defined amount of funds to a fiat account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Deposits#CREATE wallet:deposits:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param body         request body.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for creating a deposit request.
     * @see <a href="https://developers.coinbase.com/api/v2#deposit-funds">online docs: Deposit Funds</a>.
     */
    public ApiCall<CoinbaseResponse<Deposit>> placeDeposit(@NonNull String accountId,
                                                           @NonNull PlaceDepositBody body,
                                                           @Nullable ExpandField... expandFields
    ) {
        return placeTradeOrder(accountId, body, expandFields);
    }

    /**
     * Completes a deposit that is created in {@code commit: false} state.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Deposits#CREATE wallet:deposits:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param depositId    id of the deposit.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for committing the deposit.
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-deposit">online docs: Commit a Deposit</a>.
     */
    public ApiCall<CoinbaseResponse<Deposit>> commitDeposit(@NonNull String accountId,
                                                            @NonNull String depositId,
                                                            @Nullable ExpandField... expandFields) {
        return commitTradeOrder(accountId, depositId, expandFields);
    }

    //endregion

    //region Rx Methods

    /**
     * Rx version of {@link #listDeposits(String, PaginationParams, ExpandField...)}.
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters}.
     * @param expandFields     {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for getting the list of deposits.
     */
    public Single<PagedResponse<Deposit>> listDepositsRx(@NonNull String accountId,
                                                         @NonNull PaginationParams paginationParams,
                                                         @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, paginationParams, expandFields);
    }

    /**
     * Rx version of {@link #listDeposits(String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for getting the list of deposits.
     */
    public Single<PagedResponse<Deposit>> listDepositsRx(@NonNull String accountId,
                                                         @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, null, expandFields);
    }

    /**
     * Rx version of {@link #showDeposit(String, String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param depositId    id of the deposit.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for showing the deposit.
     */
    public Single<CoinbaseResponse<Deposit>> showDepositRx(@NonNull String accountId,
                                                           @NonNull String depositId,
                                                           @Nullable ExpandField... expandFields) {
        return showTradeRx(accountId, depositId, expandFields);
    }

    /**
     * Rx version of {@link #placeDeposit(String, PlaceDepositBody, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param body         request body.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for creating a deposit request.
     */
    public Single<CoinbaseResponse<Deposit>> placeDepositRx(@NonNull String accountId,
                                                            @NonNull PlaceDepositBody body,
                                                            @Nullable ExpandField... expandFields) {
        return placeTradeOrderRx(accountId, body, expandFields);
    }

    /**
     * Rx version of {@link #commitDeposit(String, String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param depositId    id of the deposit.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for committing the deposit.
     */
    public Single<CoinbaseResponse<Deposit>> commitDepositRx(@NonNull String accountId,
                                                             @NonNull String depositId,
                                                             @Nullable ExpandField... expandFields) {
        return commitTradeOrderRx(accountId, depositId, expandFields);
    }

    //endregion

}
