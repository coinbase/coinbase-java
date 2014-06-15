package com.coinbase.api;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.User;

public interface Coinbase {

    public User getUser();
    
    public Transaction getTransaction(String id);
    
    public Transaction requestMoney(Transaction transaction);

    public void resendRequest(String transactionId);
    public void deleteRequest(String transactionId);
    public Transaction completeRequest(String transactionId);
    public Transaction sendMoney(Transaction transaction);

    public Response getTransactions();
    public Response getTransactions(int page);

    public Response getTransfers();
    public Response getTransfers(int page);

    public Response getAddresses();
    public Response getAddresses(int page);

    public Response getAccounts();
    public Response getAccounts(int page);
    public Response getAccounts(int page, int limit);
    public Response getAccounts(int page, int limit, boolean includeInactive);

    public Money getBalance(String accountId);

    public Money getSpotPrice(CurrencyUnit currency);
    public Quote getBuyQuote(Money btcAmount);
    public Quote getSellQuote(Money btcAmount);

}
