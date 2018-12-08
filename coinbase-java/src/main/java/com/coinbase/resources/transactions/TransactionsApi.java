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

import com.coinbase.ApiConstants;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.network.ApiCall;

import java.util.Map;
import java.util.Set;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * API interface for transactions.
 *
 * @see TransactionsResource
 */
public interface TransactionsApi {

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS)
    ApiCall<PagedResponse<Transaction>> listTransactions(
            @Path("account_id") String accountId,
            @Query("expand[]") Set<String> expandOptions
    );

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS)
    ApiCall<PagedResponse<Transaction>> listTransactions(
            @Path("account_id") String accountId,
            @Query("expand[]") Set<String> expandOptions,
            @QueryMap Map<String, String> query);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS + "/{transaction_id}")
    ApiCall<CoinbaseResponse<Transaction>> showTransaction(
            @Path("account_id") String accountId,
            @Path("transaction_id") String transactionId,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS)
    ApiCall<CoinbaseResponse<Transaction>> sendMoney(
            @Path("account_id") String accountId,
            @Header(ApiConstants.Headers.CB_2_FA_TOKEN) String twoFactorAuthToken,
            @Body SendMoneyRequest sendMoneyRequest,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS)
    ApiCall<CoinbaseResponse<Transaction>> requestMoney(
            @Path("account_id") String accountId,
            @Body MoneyRequest moneyRequest,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS + "/{transaction_id}/" + ApiConstants.COMPLETE)
    ApiCall<CoinbaseResponse<Transaction>> completeMoneyRequest(
            @Path("account_id") String accountId,
            @Path("transaction_id") String transactionId,
            @Query("expand[]") Set<String> expandOptions
    );

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS + "/{transaction_id}/" + ApiConstants.RESEND)
    ApiCall<CoinbaseResponse<Transaction>> resendMoneyRequest(
            @Path("account_id") String accountId,
            @Path("transaction_id") String transactionId,
            @Query("expand[]") Set<String> expandOptions
    );

    @DELETE(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS + "/{transaction_id}")
    ApiCall<CoinbaseResponse<Void>> cancelMoneyRequest(
            @Path("account_id") String accountId,
            @Path("transaction_id") String transactionId
    );
}
