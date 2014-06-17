package com.coinbase.api;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.entity.AddressesResponse;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.ContactsResponse;
import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.OrdersResponse;
import com.coinbase.api.entity.PaymentMethodsResponse;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionsResponse;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransfersResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;

public interface Coinbase {

    public User getUser();
    
    public Transaction getTransaction(String id);
    
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException;

    public void resendRequest(String transactionId) throws CoinbaseException;
    public void deleteRequest(String transactionId) throws CoinbaseException;
    public Transaction completeRequest(String transactionId) throws CoinbaseException;
    public Transaction sendMoney(Transaction transaction) throws CoinbaseException;

    public Order getOrder(String idOrCustom);
    public OrdersResponse getOrders();
    public OrdersResponse getOrders(int page);

    public TransactionsResponse getTransactions();
    public TransactionsResponse getTransactions(int page);

    public TransfersResponse getTransfers();
    public TransfersResponse getTransfers(int page);

    public AddressesResponse getAddresses();
    public AddressesResponse getAddresses(int page);

    public AccountsResponse getAccounts();
    public AccountsResponse getAccounts(int page);
    public AccountsResponse getAccounts(int page, int limit);
    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive);

    // TODO re-introduce limit param when BUGS-263 is fixed
    public ContactsResponse getContacts();
    public ContactsResponse getContacts(int page);
    public ContactsResponse getContacts(String query);
    public ContactsResponse getContacts(String query, int page);

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

    public PaymentMethodsResponse getPaymentMethods();
}
