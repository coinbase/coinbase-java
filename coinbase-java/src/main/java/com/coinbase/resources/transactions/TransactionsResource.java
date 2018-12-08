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

package com.coinbase.resources.transactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.transactions.Transaction.ExpandField;

import io.reactivex.Single;

import static com.coinbase.resources.ExpandUtils.toValueSet;

/**
 * Resource for interacting with transactions, e.g. getting list of transactions, sending money, making
 * money requests.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#transactions">online docs: Transactions</a> for more info.
 */
public class TransactionsResource {

    private final TransactionsApi transactionsApi;
    private final TransactionsApiRx transactionsApiRx;

    public TransactionsResource(TransactionsApi transactionsApi,
                                TransactionsApiRx transactionsApiRx) {
        this.transactionsApi = transactionsApi;
        this.transactionsApiRx = transactionsApiRx;
    }

    //region ApiCall methods

    /**
     * Lists account’s transactions. See {@link Transaction transaction} resource for more information.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#READ wallet:transactions:read}</li>
     * </ul>
     *
     * @param accountId        account id.
     * @param paginationParams parameters for pagination.
     * @param expandOptions    {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for getting list of transactions.
     * @see <a href="https://developers.coinbase.com/api/v2#list-transactions">online docs: List Transactions</a>.
     */
    public ApiCall<PagedResponse<Transaction>> listTransactions(@NonNull String accountId,
                                                                @NonNull PaginationParams paginationParams,
                                                                @Nullable ExpandField... expandOptions) {
        return transactionsApi.listTransactions(accountId,
                toValueSet(expandOptions),
                paginationParams.toQueryMap());

    }

    /**
     * Same as {@link #listTransactions(String, PaginationParams, ExpandField...) listTransactions(accountId, pagination, expandOptions)}
     * with default {@link PaginationParams pagination params}.
     *
     * @param accountId     account id.
     * @param expandOptions (optional) expand options.
     * @return {@link ApiCall call} for getting transaction list.
     */
    public ApiCall<PagedResponse<Transaction>> listTransactions(@NonNull String accountId,
                                                                @Nullable ExpandField... expandOptions) {
        return transactionsApi.listTransactions(accountId, toValueSet(expandOptions));
    }

    /**
     * Show an individual transaction for an account. See {@link Transaction transaction} resource for more information.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#READ wallet:transactions:read}</li>
     * </ul>
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for getting transaction information.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-transaction">online docs: Show Transaction</a>.
     */
    public ApiCall<CoinbaseResponse<Transaction>> showTransaction(@NonNull String accountId,
                                                                  @NonNull String transactionId,
                                                                  @Nullable ExpandField... expandOptions) {
        return transactionsApi.showTransaction(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * <p>
     * Send funds to a bitcoin address, bitcoin cash address, litecoin address, ethereum address, or email address.
     * No transaction fees are required for off blockchain bitcoin transactions.
     * <p>
     * It’s recommended to always supply a unique {@link SendMoneyRequest#setIdem(String) idem} field for each transaction.
     * This prevents you from sending the same transaction twice if there has been an unexpected network outage or other issue.
     * <p>
     * When used with OAuth2 authentication, this endpoint requires two factor authentication
     * unless used with {@link com.coinbase.Scope.Wallet.Transactions#SEND_BYPASS_2FA wallet:transactions:send:bypass-2fa} scope.
     * <p>
     * In other case, call it with {@code twoFactorAuthToken == null}, server will respond with
     * status code {@link com.coinbase.errors.Error 400} and {@link com.coinbase.errors.Error#getMessage() message}
     * indicating two factor authentication is required. After this, you can request {@code twoFactorAuthToken}
     * from user and send same request again, which, if all data is correct, should succeed.
     * <p>
     * If the user is able to buy bitcoin, they can send funds from their fiat account using instant exchange feature.
     * Buy fees will be included in the created transaction and the recipient will receive the user defined amount.
     * <p>
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#SEND wallet:transactions:send}</li>
     * </ul>
     *
     * @param accountId          account id.
     * @param twoFactorAuthToken (optional) token for authorizing this request.
     * @param sendMoneyRequest   request object.
     * @param expandOptions      {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for sending money.
     * @see SendMoneyRequest
     * @see <a href="https://developers.coinbase.com/api/v2#send-money">online docs: Send Money</a>.
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/two-factor-authentication">online docs: Two Factor Authentication</a>.
     */
    public ApiCall<CoinbaseResponse<Transaction>> sendMoney(@NonNull String accountId,
                                                            @Nullable String twoFactorAuthToken,
                                                            @NonNull SendMoneyRequest sendMoneyRequest,
                                                            @Nullable ExpandField... expandOptions) {
        return transactionsApi.sendMoney(accountId, twoFactorAuthToken, sendMoneyRequest, toValueSet(expandOptions));
    }

    /**
     * Requests money from an email address.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#REQUEST wallet:transactions:request}</li>
     * </ul>
     *
     * @param accountId     account id.
     * @param moneyRequest  request object.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for requesting money.
     * @see <a href="https://developers.coinbase.com/api/v2#request-money">online docs: Request money</a>.
     */
    public ApiCall<CoinbaseResponse<Transaction>> requestMoney(@NonNull String accountId,
                                                               @NonNull MoneyRequest moneyRequest,
                                                               @Nullable ExpandField... expandOptions) {
        return transactionsApi.requestMoney(accountId, moneyRequest, toValueSet(expandOptions));
    }

    /**
     * Lets the recipient of a money request complete the request by sending money to the user who requested the money.
     * This can only be completed by the user to whom the request was made, <i>not</i> the user who sent the request.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#REQUEST wallet:transactions:request}</li>
     * </ul>
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for completing the money request.
     * @see #requestMoney(String, MoneyRequest, ExpandField...)
     * @see <a href="https://developers.coinbase.com/api/v2#complete-request-money">online docs: Complete Money Request</a>.
     */
    public ApiCall<CoinbaseResponse<Transaction>> completeMoneyRequest(@NonNull String accountId,
                                                                       @NonNull String transactionId,
                                                                       @Nullable ExpandField... expandOptions) {
        return transactionsApi.completeMoneyRequest(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * Lets the user resend a money request. This will notify recipient with a new email.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#REQUEST wallet:transactions:request}</li>
     * </ul>
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link ApiCall call} for resending money request.
     */
    public ApiCall<CoinbaseResponse<Transaction>> resendMoneyRequest(@NonNull String accountId,
                                                                     @NonNull String transactionId,
                                                                     @Nullable ExpandField... expandOptions) {
        return transactionsApi.resendMoneyRequest(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * Lets a user cancel a money request. Money requests can be canceled by the sender or the recipient.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#REQUEST wallet:transactions:request}</li>
     * </ul>
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @return {@link ApiCall call} for canceling money request.
     */
    public ApiCall<CoinbaseResponse<Void>> cancelMoneyRequest(@NonNull String accountId,
                                                              @NonNull String transactionId) {
        return transactionsApi.cancelMoneyRequest(accountId, transactionId);
    }

    //endregion

    //region Rx methods

    /**
     * Rx version of {@link #listTransactions(String, PaginationParams, ExpandField...) listTransactions(accountId, paginationParams, expandOptions)}.
     *
     * @param accountId        account id.
     * @param paginationParams parameters for pagination.
     * @param expandOptions    {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for getting list of transactions.
     */
    public Single<PagedResponse<Transaction>> listTransactionsRx(@NonNull String accountId,
                                                                 @NonNull PaginationParams paginationParams,
                                                                 @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.listTransactions(accountId,
                toValueSet(expandOptions),
                paginationParams.toQueryMap());
    }

    /**
     * Rx version of {@link #listTransactions(String, ExpandField...) listTransactions(accountId, expandOptions)}.
     *
     * @param accountId     account id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for getting list of transactions.
     */
    public Single<PagedResponse<Transaction>> listTransactionsRx(@NonNull String accountId,
                                                                 @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.listTransactions(accountId, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #showTransaction(String, String, ExpandField...) showTransaction(accountId, transactionId, expandOptions)}.
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for getting transaction information.
     */
    public Single<CoinbaseResponse<Transaction>> showTransactionRx(@NonNull String accountId,
                                                                   @NonNull String transactionId,
                                                                   @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.showTransaction(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #sendMoney(String, String, SendMoneyRequest, ExpandField...)
     * sendMoney(accountId, twoFactorAuthToken, sendMoneyRequest, expandOptions)}.
     *
     * @param accountId          account id.
     * @param twoFactorAuthToken (optional) token for authorizing this request.
     * @param sendMoneyRequest   request object.
     * @param expandOptions      {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for sending money.
     */
    public Single<CoinbaseResponse<Transaction>> sendMoneyRx(@NonNull String accountId,
                                                             @Nullable String twoFactorAuthToken,
                                                             @NonNull SendMoneyRequest sendMoneyRequest,
                                                             @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.sendMoney(accountId, twoFactorAuthToken, sendMoneyRequest, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #requestMoney(String, MoneyRequest, ExpandField...) requestMoney(accountId, moneyRequest, expandOptions)}.
     *
     * @param accountId     account id.
     * @param moneyRequest  request object.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for requesting money.
     */
    public Single<CoinbaseResponse<Transaction>> requestMoneyRx(@NonNull String accountId,
                                                                @NonNull MoneyRequest moneyRequest,
                                                                @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.requestMoney(accountId, moneyRequest, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #completeMoneyRequest(String, String, ExpandField...) completeMoneyRequest(accountId, transactionId, expandOptions)}.
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for completing money request.
     */
    public Single<CoinbaseResponse<Transaction>> completeMoneyRequestRx(@NonNull String accountId,
                                                                        @NonNull String transactionId,
                                                                        @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.completeMoneyRequest(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #resendMoneyRequest(String, String, ExpandField...) resendMoneyRequest(accountId, transactionId, expandOptions)}.
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @param expandOptions {@link ExpandField expand fields} list.
     * @return {@link Single rxSingle} for resending money request
     */
    public Single<CoinbaseResponse<Transaction>> resendMoneyRequestRx(@NonNull String accountId,
                                                                      @NonNull String transactionId,
                                                                      @Nullable ExpandField... expandOptions) {
        return transactionsApiRx.resendMoneyRequest(accountId, transactionId, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #cancelMoneyRequest(String, String) cancelMoneyRequest(accountId, transactionId)}
     *
     * @param accountId     account id.
     * @param transactionId transaction id.
     * @return {@link Single rxSingle} for canceling money request.
     */
    public Single<CoinbaseResponse<Void>> cancelMoneyRequestRx(@NonNull String accountId,
                                                               @NonNull String transactionId) {
        return transactionsApiRx.cancelMoneyRequest(accountId, transactionId);
    }

    //endregion

}
