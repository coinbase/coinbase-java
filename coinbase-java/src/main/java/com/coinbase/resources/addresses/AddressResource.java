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

package com.coinbase.resources.addresses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.transactions.Transaction;

import io.reactivex.rxjava3.core.Single;

/**
 * Address API methods.
 *
 * @see Address
 * @see <a href="https://developers.coinbase.com/api/v2#addresses">online docs: Addresses</a> for more info.
 */
public class AddressResource {

    private final AddressesApi addressesApi;
    private final AddressesApiRx addressesApiRx;

    public AddressResource(AddressesApi addressesApi, AddressesApiRx addressesApiRx) {
        this.addressesApi = addressesApi;
        this.addressesApiRx = addressesApiRx;
    }

    /**
     * Same as {@link #listAddresses(String, PaginationParams) listAddresses(accountId)} but with
     * default pagination params.
     *
     * @param accountId user account id.
     * @return {@link ApiCall} for crypto currencies addresses.
     */
    public ApiCall<PagedResponse<Address>> listAddresses(@NonNull String accountId) {
        return addressesApi.listAddresses(accountId);
    }

    /**
     * Lists addresses for an account.
     * <p>
     * <b>Addresses should be considered one time use only. Please use
     * {@link #generateAddress} to create new addresses.
     * </b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Addresses#READ wallet:addresses:read}.</li>
     * </ul>
     *
     * @param accountId        user account id.
     * @param paginationParams optional pagination parameters. If not supplied default values
     *                         used. Check <a href="https://developers.coinbase.com/api/v2#pagination">
     *                         online docs</a> for pagination details.
     * @return {@link ApiCall} with {@link PagedResponse} for crypto currencies addresses.
     * @see <a href="https://developers.coinbase.com/api/v2#list-addresses">online docs</a>
     * for more info.
     */
    public ApiCall<PagedResponse<Address>> listAddresses(@NonNull String accountId,
                                                         @Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return listAddresses(accountId);
        }
        return addressesApi.listAddresses(accountId, paginationParams.toQueryMap());
    }

    /**
     * Show an individual address for an account. A regular bitcoin, bitcoin cash, litecoin or
     * ethereum address can be used in place of {@code addressId} but the address has to be
     * associated to the correct user account.
     * <p>
     * <b>Addresses should be considered one time use only. Please use
     * {@link #generateAddress} to create new addresses.
     * </b>
     * <p/>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Addresses#READ wallet:addresses:read}.</li>
     * </ul>
     *
     * @param accountId user account id.
     * @param address   address or address resource id.
     * @return {@link ApiCall} for crypto currency addresses.
     * @see <a href="https://developers.coinbase.com/api/v2#show-addresss">online docs</a>
     * for more info.
     */
    public ApiCall<CoinbaseResponse<Address>> showAddress(@NonNull String accountId,
                                                          @NonNull String address) {
        return addressesApi.showAddress(accountId, address);
    }

    /**
     * Same as {@link #getAddressTransactions(String, String, PaginationParams)
     * getAddressTransactions(accountId, addressId)} but with default pagination params.
     *
     * @param accountId user account id.
     * @param address   address or address resource id.
     * @return {@link ApiCall} with {@link PagedResponse} for address transactions.
     */
    public ApiCall<PagedResponse<Transaction>> getAddressTransactions(@NonNull String accountId,
                                                                      @NonNull String address) {
        return addressesApi.getAddressTransactions(accountId, address);
    }

    /**
     * List transactions that have been sent to a specific address. A regular bitcoin, bitcoin cash,
     * litecoin or ethereum address can be used in place of {@code addressId} but the address has to be
     * associated to the correct account.
     * <p/>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Transactions#READ wallet:transactions:read}.</li>
     * </ul>
     *
     * @param accountId        user account id.
     * @param address          address address or address resource id.
     * @param paginationParams optional pagination parameters. If not supplied default values
     *                         used. Check <a href="https://developers.coinbase.com/api/v2#pagination">
     *                         online docs</a> for pagination details.
     * @return {@link ApiCall} with {@link PagedResponse} for address transactions.
     * @see <a href="https://developers.coinbase.com/api/v2#list-address39s-transactions">online docs</a>
     * for more info.
     */
    public ApiCall<PagedResponse<Transaction>> getAddressTransactions(@NonNull String accountId,
                                                                      @NonNull String address,
                                                                      @Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return getAddressTransactions(accountId, address);
        }
        return addressesApi.getAddressTransactions(accountId, address, paginationParams.toQueryMap());
    }

    /**
     * Creates a new address for an account. As all the arguments are optinal, it’s possible just
     * to create a new address. This is handy if you need to create new receive addresses for an
     * account on-demand. Addresses can be created for all account types. With fiat accounts,
     * funds will be received with Instant Exchange.
     * <p/>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.Addresses#CREATE wallet:addresses:create}.</li>
     * </ul>
     *
     * @param accountId   user account id.
     * @param addressName generated address name
     * @return {@link ApiCall} for generated address.
     * @see <a href="https://developers.coinbase.com/api/v2#create-address">online docs</a>
     * for more info.
     */
    public ApiCall<CoinbaseResponse<Address>> generateAddress(@NonNull String accountId,
                                                              @NonNull String addressName) {
        return addressesApi.generateAddress(accountId, new GenerateAddressRequest(addressName));
    }

    /**
     * Rx version of {@link #listAddresses(String)}.
     *
     * @param accountId user account id.
     * @return {@link Single} for crypto currencies addresses.
     */
    public Single<PagedResponse<Address>> listAddressesRx(@NonNull String accountId) {
        return addressesApiRx.listAddresses(accountId);
    }

    /**
     * Rx version of {@link #listAddresses(String, PaginationParams)}.
     *
     * @param accountId        user account id.
     * @param paginationParams optional pagination parameters. If not supplied default values
     *                         used. Check <a href="https://developers.coinbase.com/api/v2#pagination">
     *                         online docs</a> for pagination details.
     * @return {@link Single} for crypto currencies addresses.
     */
    public Single<PagedResponse<Address>> listAddressesRx(@NonNull String accountId,
                                                          @Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return listAddressesRx(accountId);
        }
        return addressesApiRx.listAddresses(accountId, paginationParams.toQueryMap());
    }

    /**
     * Rx version of {@link #showAddress(String, String)}.
     *
     * @param accountId user account id.
     * @param address   address or address resource id.
     * @return {@link Single} for crypto currency addresses.
     */
    public Single<CoinbaseResponse<Address>> showAddressRx(@NonNull String accountId,
                                                           @NonNull String address) {
        return addressesApiRx.showAddress(accountId, address);
    }

    /**
     * Rx version of {@link #getAddressTransactions(String, String)}.
     *
     * @param accountId user account id.
     * @param address   address or address resource id.
     * @return {@link Single} with {@link PagedResponse} for address transactions.
     */
    public Single<PagedResponse<Transaction>> getAddressTransactionsRx(@NonNull String accountId,
                                                                       @NonNull String address) {
        return addressesApiRx.getAddressTransactions(accountId, address);
    }

    /**
     * Rx version of {@link #getAddressTransactions(String, String, PaginationParams)}.
     *
     * @param accountId        user account id.
     * @param address          address or address resource id.
     * @param paginationParams optional pagination parameters. If not supplied default values
     *                         used. Check <a href="https://developers.coinbase.com/api/v2#pagination">
     *                         online docs</a> for pagination details.
     * @return {@link Single} with {@link PagedResponse} for address transactions.
     */
    public Single<PagedResponse<Transaction>> getAddressTransactionsRx(@NonNull String accountId,
                                                                       @NonNull String address,
                                                                       @Nullable PaginationParams paginationParams) {
        if (paginationParams == null) {
            return getAddressTransactionsRx(accountId, address);
        }
        return addressesApiRx.getAddressTransactions(accountId, address, paginationParams.toQueryMap());
    }

    /**
     * Rx version of {@link #generateAddress(String, String)}.
     *
     * @param accountId   user account id.
     * @param addressName new address name.
     * @return {@link Single} with generated address.
     */
    public Single<CoinbaseResponse<Address>> generateAddressRx(@NonNull String accountId,
                                                               @NonNull String addressName) {
        return addressesApiRx.generateAddress(accountId, new GenerateAddressRequest(addressName));
    }
}
