package com.coinbase;

import com.coinbase.auth.AccessToken;
import com.coinbase.v2.models.account.Account;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.address.Address;
import com.coinbase.v2.models.exchangeRates.ExchangeRates;
import com.coinbase.v2.models.paymentMethods.PaymentMethod;
import com.coinbase.v2.models.paymentMethods.PaymentMethods;
import com.coinbase.v2.models.price.Price;
import com.coinbase.v2.models.price.Prices;
import com.coinbase.v2.models.supportedCurrencies.SupportedCurrencies;
import com.coinbase.v2.models.transactions.Transaction;
import com.coinbase.v2.models.transactions.Transactions;
import com.coinbase.v2.models.transfers.Transfer;
import com.coinbase.v2.models.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @POST(ApiConstants.TOKEN)
    Call<AccessToken> refreshTokens(@Body HashMap<String, Object> body);

    @POST(ApiConstants.REVOKE)
    Call<Void> revokeToken(@Body HashMap<String, Object> body);

    @GET(ApiConstants.USER)
    Call<User> getUser();

    @PUT(ApiConstants.USER)
    Call<User> updateUser(@Body HashMap<String, Object> body);

    @GET(ApiConstants.ACCOUNTS + "/{id}")
    Call<Account> getAccount(@Path("id") String accountId);

    @GET(com.coinbase.ApiConstants.ACCOUNTS)
    Call<Accounts> getAccounts(@QueryMap Map<String, Object> options);

    @POST(ApiConstants.ACCOUNTS)
    Call<Account> createAccount(@Body HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.PRIMARY)
    Call<Void> setAccountPrimary(@Path("id") String accountId);

    @PUT(ApiConstants.ACCOUNTS + "/{id}")
    Call<Account> updateAccount(@Path("id") String acountId, @Body HashMap<String, Object> body);

    @DELETE(ApiConstants.ACCOUNTS + "/{id}")
    Call<Void> deleteAccount(@Path("id") String accountId);

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
    Call<Void> cancelTransaction(@Path("account_id") String accountId, @Path("transaction_id") String transactionId);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> sendMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> requestMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(com.coinbase.ApiConstants.ACCOUNTS + "/{id}/" + com.coinbase.ApiConstants.TRANSACTIONS)
    Call<Transaction> transferMoney(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @GET(com.coinbase.ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SELL)
    Call<Price> getSellPrice(@Path("base_currency") String baseCurrency,
                             @Path("fiat_currency") String fiatCurrency,
                             @QueryMap HashMap<String, Object> body);

    @GET(com.coinbase.ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.BUY)
    Call<Price> getBuyPrice(@Path("base_currency") String baseCurrency,
                            @Path("fiat_currency") String fiatCurrency,
                            @QueryMap HashMap<String, Object> body);

    @GET(com.coinbase.ApiConstants.PRICES + "/{base_currency}-" + "{fiat_currency}/" + ApiConstants.SPOT)
    Call<Price> getSpotPrice(@Path("base_currency") String baseCurrency,
                             @Path("fiat_currency") String fiatCurrency,
                             @QueryMap HashMap<String, Object> body);

    @GET(com.coinbase.ApiConstants.PRICES + "/{fiat_currency}/" + ApiConstants.SPOT)
    Call<Prices> getSpotPrices(@Path("fiat_currency") String fiatCurrency,
                               @QueryMap HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.BUYS)
    Call<Transfer> buyBitcoin(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.BUYS + "/{buy_id}/" + ApiConstants.COMMIT)
    Call<Transfer> commitBuyBitcoin(@Path("account_id") String accountId, @Path("buy_id") String buyId);

    @POST(ApiConstants.ACCOUNTS + "/{id}/" + ApiConstants.SELLS)
    Call<Transfer> sellBitcoin(@Path("id") String accountId, @Body HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.SELLS + "/{sell_id}/" + ApiConstants.COMMIT)
    Call<Transfer> commitSellBitcoin(@Path("account_id") String accountId, @Path("sell_id") String sellId);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.ADDRESSES)
    Call<Address> generateAddress(@Path("account_id") String accoundId);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.DEPOSITS)
    Call<Transfer> depositFunds(@Path("account_id") String accountId, @Body HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.DEPOSITS + "/{deposit_id}/" + ApiConstants.COMMIT)
    Call<Transfer> commitDeposit(@Path("account_id") String accountId, @Path("deposit_id") String depositId);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.WITHDRAWALS)
    Call<Transfer> withdrawFunds(@Path("account_id") String accountId, @Body HashMap<String, Object> body);

    @POST(ApiConstants.ACCOUNTS + "/{account_id}/" + ApiConstants.WITHDRAWALS + "/{withdrawal_id}/" + ApiConstants.COMMIT)
    Call<Transfer> commitWithdraw(@Path("account_id") String accountId, @Path("withdrawal_id") String depositId);

    @GET(ApiConstants.PAYMENT_METHODS)
    Call<PaymentMethods> getPaymentMethods(@QueryMap HashMap<String, Object> body);

    @GET(ApiConstants.PAYMENT_METHODS + "/{id}")
    Call<PaymentMethod> getPaymentMethod(@Path("id") String paymentMethodId);

    @GET(ApiConstants.EXCHANGE_RATES)
    Call<ExchangeRates> getExchangeRates(@QueryMap HashMap<String, Object> body);

    @GET(ApiConstants.CURRENCIES)
    Call<SupportedCurrencies> getSupportedCurrencies();
}
