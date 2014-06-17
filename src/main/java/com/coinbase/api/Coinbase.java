package com.coinbase.api;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnknownAccount;

public interface Coinbase {

    public User getUser();
    
    public Transaction getTransaction(String id);
    
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException;

    public void resendRequest(String transactionId) throws CoinbaseException;
    public void deleteRequest(String transactionId) throws CoinbaseException;
    public Transaction completeRequest(String transactionId) throws CoinbaseException;
    public Transaction sendMoney(Transaction transaction) throws CoinbaseException;

    public Order getOrder(String idOrCustom);
    public Response getOrders();
    public Response getOrders(int page);

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

    // TODO re-introduce limit param when BUGS-263 is fixed
    public Response getContacts();
    public Response getContacts(int page);
    public Response getContacts(String query);
    public Response getContacts(String query, int page);

    public Money getBalance() throws CoinbaseException;
    public Money getBalance(String accountId);

    public void setPrimaryAccount() throws CoinbaseException;
    public void setPrimaryAccount(String accountId) throws CoinbaseException;

    public void deleteAccount() throws CoinbaseException;
    public void deleteAccount(String accountId) throws CoinbaseException;

    public void updateAccount(Account account) throws CoinbaseException;
    public void updateAccount(String accountId, Account account) throws CoinbaseException;

    public Account createAccount(Account account) throws CoinbaseException;

    public Money getSpotPrice(CurrencyUnit currency);
    public Quote getBuyQuote(Money btcAmount);
    public Quote getSellQuote(Money btcAmount);

    public Button createButton(Button button) throws CoinbaseException;
    public Order createOrder(Button button) throws CoinbaseException;

    public Transfer sell(Money amount) throws CoinbaseException;
    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException;

    public Transfer buy(Money amount) throws CoinbaseException;
    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException;

    public Response getPaymentMethods();
}
