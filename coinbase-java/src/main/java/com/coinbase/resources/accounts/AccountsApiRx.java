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

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Rx API interface for account resource.
 *
 * @see AccountResource
 */
public interface AccountsApiRx {

    @GET(ApiConstants.ACCOUNTS)
    Single<PagedResponse<Account>> getAccounts();

    @GET(ApiConstants.ACCOUNTS)
    Single<PagedResponse<Account>> getAccounts(@QueryMap Map<String, String> query);

    @GET(ApiConstants.ACCOUNTS + "/{id}")
    Single<CoinbaseResponse<Account>> showAccount(@Path("id") String accountId);

    @POST(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.PRIMARY)
    Single<CoinbaseResponse<Account>> setAccountPrimary(@Path("id") String accountId);

    @PUT(ApiConstants.ACCOUNTS + "/{id}")
    Single<CoinbaseResponse<Account>> updateAccount(
            @Path("id") String accountId,
            @Body UpdateAccountRequest updateAccountRequest);

    @DELETE(ApiConstants.ACCOUNTS + "/{id}")
    Single<CoinbaseResponse<Void>> deleteAccount(@Path("id") String accountId);

}
