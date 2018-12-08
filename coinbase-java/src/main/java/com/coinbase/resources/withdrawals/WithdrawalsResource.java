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

package com.coinbase.resources.withdrawals;

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
 * Represents a resource for working with Withdrawals: listing, placing orders and committing.
 * <p>
 * Withdrawal can be started with {@code commit: false} which is useful when displaying the confirmation for a withdrawal.
 * These withdrawals will never complete and receive an associated transaction unless they are committed separately.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#withdrawals">online docs: Withdrawals</a> for more info.
 */
public class WithdrawalsResource extends TradesResource<Withdrawal> {

    public WithdrawalsResource(TradesApi tradesApi, TradesApiRx tradesApiRx) {
        super(tradesApi, tradesApiRx, ApiConstants.WITHDRAWALS);
    }

    //region ApiCall methods

    /**
     * Lists withdrawals for an account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Withdrawals#READ wallet:withdrawals:read}</li>
     * </ul>
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters}.
     * @param expandFields     {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for getting the list of withdrawals.
     * @see <a href="https://developers.coinbase.com/api/v2#list-withdrawals">online docs: List Withdrawals</a>.
     */
    public ApiCall<PagedResponse<Withdrawal>> listWithdrawals(@NonNull String accountId,
                                                              @NonNull PaginationParams paginationParams,
                                                              @Nullable ExpandField... expandFields) {
        return listTrades(accountId, paginationParams, expandFields);
    }

    /**
     * Same as {@link #listWithdrawals(String, PaginationParams, ExpandField...) listWithdrawals(accountId, expandFields)}
     * with default pagination params.
     *
     * @param accountId    id of the account.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for getting the list of withdrawals.
     * @see <a href="https://developers.coinbase.com/api/v2#list-withdrawals">online docs: List Withdrawals</a>.
     */
    public ApiCall<PagedResponse<Withdrawal>> listWithdrawals(@NonNull String accountId,
                                                              @Nullable ExpandField... expandFields) {
        return listTrades(accountId, null, expandFields);
    }

    /**
     * Show an individual withdrawal.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Withdrawals#READ wallet:withdrawals:read}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param withdrawalId id of the withdrawal.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for showing the withdrawal.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-withdrawal">online docs: Show a Withdraw</a>.
     */
    public ApiCall<CoinbaseResponse<Withdrawal>> showWithdrawal(@NonNull String accountId,
                                                                @NonNull String withdrawalId,
                                                                @Nullable ExpandField... expandFields) {
        return showTrade(accountId, withdrawalId, expandFields);
    }

    /**
     * Withdraws user-defined amount of funds to a fiat account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Withdrawals#CREATE wallet:withdrawals:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param body         request body.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for creating a withdrawal request.
     * @see <a href="https://developers.coinbase.com/api/v2#withdraw-funds">online docs: Withdraw Funds</a>.
     */
    public ApiCall<CoinbaseResponse<Withdrawal>> placeWithdrawal(@NonNull String accountId,
                                                                 @NonNull PlaceWithdrawalBody body,
                                                                 @Nullable ExpandField... expandFields) {
        return placeTradeOrder(accountId, body, expandFields);
    }

    /**
     * Completes a withdrawal that is created in {@code commit: false} state.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Withdrawals#CREATE wallet:withdrawals:create}</li>
     * </ul>
     *
     * @param accountId    id of the account.
     * @param withdrawalId id of the withdrawal.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link ApiCall call} for committing the withdrawal.
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-withdrawal">online docs: Commit a Withdraw</a>.
     */
    public ApiCall<CoinbaseResponse<Withdrawal>> commitWithdrawal(@NonNull String accountId,
                                                                  @NonNull String withdrawalId,
                                                                  @Nullable ExpandField... expandFields) {
        return commitTradeOrder(accountId, withdrawalId, expandFields);
    }

    //endregion

    //region Rx methods

    /**
     * Rx version of {@link #listWithdrawals(String, PaginationParams, ExpandField...)}.
     *
     * @param accountId        id of the account.
     * @param paginationParams {@link PaginationParams pagination parameters}.
     * @param expandFields     {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for getting the list of withdrawals.
     */
    public Single<PagedResponse<Withdrawal>> listWithdrawalsRx(@NonNull String accountId,
                                                               @NonNull PaginationParams paginationParams,
                                                               @Nullable ExpandField... expandFields) {
        return listTradesRx(accountId, paginationParams, expandFields);
    }

    /**
     * Rx version of {@link #listWithdrawals(String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for getting the list of withdrawals.
     */
    public Single<PagedResponse<Withdrawal>> listWithdrawalsRx(@NonNull String accountId,
                                                               @Nullable ExpandField... expandFields) {
        return listWithdrawalsRx(accountId, null, expandFields);
    }

    /**
     * Rx version of {@link #showWithdrawal(String, String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param withdrawalId id of the withdrawal.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for showing the withdrawal.
     */
    public Single<CoinbaseResponse<Withdrawal>> showWithdrawalRx(@NonNull String accountId,
                                                                 @NonNull String withdrawalId,
                                                                 @Nullable ExpandField... expandFields) {
        return showTradeRx(accountId, withdrawalId, expandFields);
    }

    /**
     * Rx version of {@link #placeWithdrawal(String, PlaceWithdrawalBody, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param body         request body.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for creating a withdrawal request.
     */
    public Single<CoinbaseResponse<Withdrawal>> placeWithdrawalRx(@NonNull String accountId,
                                                                  @NonNull PlaceWithdrawalBody body,
                                                                  @Nullable ExpandField... expandFields) {
        return placeTradeOrderRx(accountId, body, expandFields);
    }

    /**
     * Rx version of {@link #commitWithdrawal(String, String, ExpandField...)}.
     *
     * @param accountId    id of the account.
     * @param withdrawalId id of the withdrawal.
     * @param expandFields {@link com.coinbase.resources.trades.Trade.ExpandField expand field} options.
     * @return {@link Single rxSingle} for committing the withdrawal.
     */
    public Single<CoinbaseResponse<Withdrawal>> commitWithdrawalRx(@NonNull String accountId,
                                                                   @NonNull String withdrawalId,
                                                                   @Nullable ExpandField... expandFields) {
        return commitTradeOrderRx(accountId, withdrawalId, expandFields);
    }

    //endregion

}
