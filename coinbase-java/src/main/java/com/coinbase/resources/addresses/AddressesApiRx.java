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

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.resources.transactions.Transaction;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Rx API interface for getting addresses of an account crypto currencies.
 *
 * @see AddressResource
 */
public interface AddressesApiRx {
    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES)
    Single<PagedResponse<Address>> listAddresses(@Path("account_id") String accountId);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES)
    Single<PagedResponse<Address>> listAddresses(@Path("account_id") String accountId,
                                                 @QueryMap Map<String, String> query);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES + "/{address_id}")
    Single<CoinbaseResponse<Address>> showAddress(
            @Path("account_id") String accountId,
            @Path("address_id") String addressId);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES + "/{address_id}/" + ApiConstants.TRANSACTIONS)
    Single<PagedResponse<Transaction>> getAddressTransactions(
            @Path("account_id") String accountId,
            @Path("address_id") String addressId);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES + "/{address_id}/" + ApiConstants.TRANSACTIONS)
    Single<PagedResponse<Transaction>> getAddressTransactions(
            @Path("account_id") String accountId,
            @Path("address_id") String addressId,
            @QueryMap Map<String, String> query);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES)
    Single<CoinbaseResponse<Address>> generateAddress(@Path("account_id") String accountId,
                                                      @Body GenerateAddressRequest generateAddressRequest);
}