package com.coinbase;

import com.coinbase.v2.models.account.Account;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.spotPrice.SpotPrice;
import com.coinbase.v2.models.transactions.Transaction;
import com.coinbase.v2.models.transactions.Transactions;
import com.coinbase.v2.models.transfers.Transfer;
import com.coinbase.v2.models.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface ApiInterface {
    @GET(ApiConstants.USER)
    Call<User> getUser();

    @PUT(ApiConstants.USER)
    Call<User> updateUser(@Body HashMap<String, Object> body);

    @GET(ApiConstants.ACCOUNTS + "/{id}")
    Call<Account> getAccount(@Path("id") String accountId);

    @GET(com.coinbase.ApiConstants.ACCOUNTS)
    Call<Accounts> getAccounts();

    @GET(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transactions> getTransactions(@Path("id") String accountId,
                                       @Query("expand[]") List<String> expandOptions,
                                       @QueryMap Map<String, Object> options);

    @GET(com.coinbase.ApiConstants.ACCOUNTS + "/{account_id}/" + com.coinbase.ApiConstants.TRANSACTIONS + "/{transaction_id}")
    Call<Transaction> getTransaction(@Path("account_id") String accountId,
                                     @Path("transaction_id") String transactionId,
                                     @Query("expand[]") List<String> expandOptions);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{account_id}/" + com.coinbase.ApiConstants.TRANSACTIONS + "/{transaction_id}/" + com.coinbase.ApiConstants.COMPLETE)
    Call<Void> completeRequest(@Path("account_id") String accountId, @Path("transaction_id") String transactionId);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{account_id}/" + com.coinbase.ApiConstants.TRANSACTIONS + "/{transaction_id}/" + com.coinbase.ApiConstants.RESEND)
    Call<Void> resendRequest(@Path("account_id") String accountId, @Path("transaction_id") String transactionId);

    @DELETE(com.coinbase.ApiConstants.ACCOUNTS + "/{account_id}/" + com.coinbase.ApiConstants.TRANSACTIONS + "/{transaction_id}")
    Call<Void> cancelRequest(@Path("account_id") String accountId, @Path("transaction_id") String transactionId);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> sendMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> requestMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> transferMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @GET(com.coinbase.ApiConstants.PRICES_SPOT)
    Call<SpotPrice> getSpotPrice(@Query("currency") String currency);

    @POST(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.BUYS)
    Call<Transfer> buyBitcoin(@Path("id") String accountId, @Body HashMap<String, Object> body);
}