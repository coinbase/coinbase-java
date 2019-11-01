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

package com.coinbase.resources.accounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;

import io.reactivex.rxjava3.core.Single;

/**
 * Resource for handling Account information.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#accounts">online docs: Accounts</a> for more info.
 */
public class AccountResource {

    private final AccountsApi accountsApi;
    private final AccountsApiRx accountsApiRx;

    public AccountResource(AccountsApi accountsApi, AccountsApiRx accountsApiRx) {
        this.accountsApi = accountsApi;
        this.accountsApiRx = accountsApiRx;
    }

    //region ApiCall methods

    /**
     * Same as {@link #getAccounts(PaginationParams)} but with default pagination params.
     *
     * @return {@link ApiCall call} to get accounts.
     */
    public ApiCall<PagedResponse<Account>> getAccounts() {
        return accountsApi.getAccounts();
    }

    /**
     * Lists current user’s accounts to which the authentication method has access to.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Accounts#READ wallet:accounts:read}</li>
     * </ul>
     *
     * @param paginationParams (optional) pagination parameters.
     * @return {@link ApiCall call} to get accounts.
     * @see PaginationParams
     * @see <a href="https://developers.coinbase.com/api/v2#list-accounts">online docs: List accouns</a>.
     */
    public ApiCall<PagedResponse<Account>> getAccounts(@Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return getAccounts();
        }
        return accountsApi.getAccounts(paginationParams.toQueryMap());
    }

    /**
     * Show current user’s account. To access the primary account for a given currency,
     * a currency string (BTC or ETH) can be used instead of the account id in the URL.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Accounts#READ wallet:accounts:read}</li>
     * </ul>
     *
     * @param accountId id of the account or currency to get primary account.
     * @return {@link ApiCall call} to get account details.
     * @see <a href="https://developers.coinbase.com/api/v2#show-an-account">online docs: Show an account</a>.
     */
    public ApiCall<CoinbaseResponse<Account>> showAccount(@NonNull String accountId) {
        return accountsApi.showAccount(accountId);
    }

    /**
     * Promotes an account as primary account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Accounts#UPDATE wallet:accounts:update}</li>
     * </ul>
     *
     * @param accountId id of the account.
     * @return {@link ApiCall call} for updating account.
     * @see <a href="https://developers.coinbase.com/api/v2#set-account-as-primary">online docs: Set account primary</a>.
     */
    public ApiCall<CoinbaseResponse<Account>> setAccountPrimary(@NonNull String accountId) {
        return accountsApi.setAccountPrimary(accountId);
    }

    /**
     * Modifies user’s account. Effectively you can only change the name of the account.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Accounts#UPDATE wallet:accounts:update}</li>
     * </ul>
     *
     * @param accountId id of the account.
     * @param name      (optional) account name.
     * @return {@link ApiCall call} for updating account.
     * @see <a href="https://developers.coinbase.com/api/v2#update-account">online docs: Update account</a>.
     */
    public ApiCall<CoinbaseResponse<Account>> updateAccount(@NonNull String accountId,
                                                            @Nullable String name) {
        return accountsApi.updateAccount(accountId, new UpdateAccountRequest(name));
    }

    /**
     * Removes user’s account. In order to remove an account it can’t be:
     * <ul>
     * <li>Primary account</li>
     * <li>Account with non zero balance</li>
     * <li>Fiat account</li>
     * <li>Vault with pending withdrawal</li>
     * </ul>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Accounts#DELETE wallet:accounts:delete}</li>
     * </ul>
     *
     * @param accountId id of the account.
     * @return {@link ApiCall call} for deleting account.
     * @see <a href="https://developers.coinbase.com/api/v2#delete-account">online docs: Delete account</a>.
     */
    public ApiCall<CoinbaseResponse<Void>> deleteAccount(@NonNull String accountId) {
        return accountsApi.deleteAccount(accountId);
    }

    //endregion

    //region Rx methods

    /**
     * Rx version of {@link #getAccounts()}.
     *
     * @return {@link Single rxSingle} for getting accounts.
     */
    public Single<PagedResponse<Account>> getAccountsRx() {
        return accountsApiRx.getAccounts();
    }

    /**
     * Rx version of {@link #getAccounts(PaginationParams)}.
     *
     * @param paginationParams (optional) pagination parameters.
     * @return {@link Single rxSingle} for getting accounts.
     * @see PaginationParams
     */
    public Single<PagedResponse<Account>> getAccountsRx(@Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return getAccountsRx();
        }
        return accountsApiRx.getAccounts(paginationParams.toQueryMap());
    }

    /**
     * Rx version of {@link #showAccount(String)}.
     *
     * @param accountId id of the account or currency to get primary account.
     * @return {@link Single rxSingle} for getting account details.
     */
    public Single<CoinbaseResponse<Account>> showAccountRx(@NonNull String accountId) {
        return accountsApiRx.showAccount(accountId);
    }

    /**
     * Rx version of {@link #setAccountPrimary(String)}.
     *
     * @param accountId id of the account
     * @return {@link Single rxSingle} for updating account.
     */
    public Single<CoinbaseResponse<Account>> setAccountPrimaryRx(@NonNull String accountId) {
        return accountsApiRx.setAccountPrimary(accountId);
    }

    /**
     * Rx version of {@link #updateAccount(String, String)}.
     *
     * @param accountId id of the account
     * @param name      updated name of the account
     * @return {@link Single rxSingle} for updating account.
     */
    public Single<CoinbaseResponse<Account>> updateAccountRx(@NonNull String accountId,
                                                             @Nullable String name) {
        return accountsApiRx.updateAccount(accountId, new UpdateAccountRequest(name));
    }

    /**
     * Rx version of {@link #deleteAccount(String)}.
     *
     * @param accountId id of the account
     * @return {@link Single rxSingle} for deleting account.
     */
    public Single<CoinbaseResponse<Void>> deleteAccountRx(@NonNull String accountId) {
        return accountsApiRx.deleteAccount(accountId);
    }

    //endregion
}
