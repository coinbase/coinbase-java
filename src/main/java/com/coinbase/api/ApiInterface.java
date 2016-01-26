package com.coinbase.api;

import com.coinbase.api.models.account.Account;
import com.coinbase.api.models.account.Accounts;
import com.coinbase.api.models.transactions.Transaction;
import com.coinbase.api.models.transactions.Transactions;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface ApiInterface {
    @GET(ApiConstants.ACCOUNTS + "/{id}")
    Call<Account> getAccount(@Path("id") String accountId);

    @GET(ApiConstants.ACCOUNTS)
    Call<Accounts> getAccounts();

    @GET(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.TRANSACTIONS)
    Call<Transactions> getTransactions(@Path("id") String accountId,
                                       @QueryMap Map<String, Object> options);

    @GET(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.TRANSACTIONS + "/{transaction_id}")
    Call<Transaction> getTransaction(@Path("account_id") String accountId,
                                     @Path("transaction_id") String transactionId,
                                     @Query("expand[]") List<String> expandOptions);
}