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
import com.coinbase.api.entity.RecurringPaymentsResponse;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionsResponse;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransfersResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnspecifiedAccount;

public interface Coinbase {

    /**
     * Retrieve the current user and their settings.
     * 
     * Also includes buy/sell levels (1, 2, or 3) and daily buy/sell limits (actual fiat amount).
     * 
     * @return the current user and their settings
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/users/index.html">Online Documentation</a>
     */
    public User getUser();
    
    
    /**
     * Retrieve details of an individual transaction.
     * 
     * @param id the transaction id or idem field value
     * @return the transaction details
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/show.html">Online Documentation</a>
     */
    public Transaction getTransaction(String id);
    
    /**
     * Retrieve a list of the user's recent transactions
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/index.html">Online Documentation</a>
     */
    public TransactionsResponse getTransactions();
    
    /**
     * Retrieve a list of the user's recent transactions
     * 
     * @param page the page of transactions to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/index.html">Online Documentation</a>
     */
    public TransactionsResponse getTransactions(int page);
    
    /**
     * Send an invoice/money request to an email address.
     * 
     * @param transaction a Transaction object containing the parameters for a money request
     * 
     * @return the newly created transaction
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/request_money.html">Online Documentation</a>
     */
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException;

    /**
     * Resend emails for a money request.
     * 
     * @param transactionId the id of the request money transaction to be resent
     * @throws CoinbaseException
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/resend_request.html">Online Documentation</a>
     */
    public void resendRequest(String transactionId) throws CoinbaseException;
    
    /**
     * Cancel a money request.
     * 
     * @param transactionId the id of the request money transaction to be canceled
     * @throws CoinbaseException
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/cancel_request.html">Online Documentation</a>
     */
    public void deleteRequest(String transactionId) throws CoinbaseException;
    
    
    /**
     * Complete a money request.
     * 
     * @param transactionId the id of the request money transaction to be completed
     * @return the completed transaction
     * @throws CoinbaseException
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/complete_request.html">Online Documentation</a>
     */
    public Transaction completeRequest(String transactionId) throws CoinbaseException;
    
    
    /**
     * Send money to an email address or bitcoin address
     * 
     * @param transaction a Transaction object containing the send money parameters
     * @return the newly created transaction
     * @throws CoinbaseException
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/send_money.html">Online Documentation</a>
     */
    public Transaction sendMoney(Transaction transaction) throws CoinbaseException;

    /**
     * Retrieve details of an individual merchant order
     * 
     * @param idOrCustom the order id or custom field value
     * @return the details of the merchant order
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/show.html">Online Documentation</a>
     */
    public Order getOrder(String idOrCustom);
    
    
    /**
     * Retrieve a list of the user's recently received merchant orders
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/index.html">Online Documentation</a>
     */
    public OrdersResponse getOrders();
    
    /**
     * Retrieve a list of the user's recently received merchant orders
     * 
     * @param page the page of orders to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/index.html">Online Documentation</a>
     */
    public OrdersResponse getOrders(int page);

    /**
     * Retrieve a list of the user's recent transfers (buys/sales)
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transfers/index.html">Online Documentation</a>
     */
    public TransfersResponse getTransfers();
    
    /**
     * Retrieve a list of the user's recent transfers (buys/sales)
     * 
     * @param page the page of transfers to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/transfers/index.html">Online Documentation</a>
     */
    public TransfersResponse getTransfers(int page);

    /**
     * Retrieve a list of bitcoin addresses associated with this account
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/addresses/index.html">Online Documentation</a>
     */
    public AddressesResponse getAddresses();
    
    /**
     * Retrieve a list of bitcoin addresses associated with this account
     * 
     * @param page the page of addresses to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/addresses/index.html">Online Documentation</a>
     */
    public AddressesResponse getAddresses(int page);
    
    /**
     * Retrieve a list of accounts belonging to this user
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     */
    public AccountsResponse getAccounts();
    
    /**
     * Retrieve a list of active accounts belonging to this user
     * 
     * @param page the page of accounts to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     */
    public AccountsResponse getAccounts(int page);
    
    /**
     * Retrieve a list of active accounts belonging to this user
     * 
     * @param page the page of accounts to retrieve
     * @param limit the number of accounts to retrieve per page
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     */
    public AccountsResponse getAccounts(int page, int limit);
    
    /**
     * Retrieve a list of accounts belonging to this user
     * 
     * @param page the page of accounts to retrieve
     * @param limit the number of accounts to retrieve per page
     * @param includeInactive include inactive accounts in the response
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     */
    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive);

    // TODO re-introduce limit param when BUGS-263 is fixed
    
    /**
     * Retrieve a list of the user's contacts
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts();
    
    /**
     * Retrieve a list of the user's contacts
     * 
     * @param page the page of accounts to retrieve
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(int page);
    
    /**
     * Retrieve a list of the user's contacts
     * 
     * @param query partial string match to filter contacts.
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(String query);
    
    /**
     * Retrieve a list of the user's contacts
     * 
     * @param page the page of accounts to retrieve
     * @param query partial string match to filter contacts.
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(String query, int page);

    /**
     * Retrieve the balance of the current account
     * 
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * @return the balance of the current account
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/balance.html">Online Documentation</a>
     */
    public Money getBalance() throws UnspecifiedAccount;
    
    /**
     * Retrieve the balance of the specified account
     * 
     * @param accountId the id of the account for which to retrieve the balance
     * @return the balance of the specified account
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/balance.html">Online Documentation</a>
     */
    public Money getBalance(String accountId);

    /**
     * Set the current account as primary
     * 
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/primary.html">Online Documentation</a>
     */
    public void setPrimaryAccount() throws UnspecifiedAccount, CoinbaseException;
    
    /**
     * Set the specified account as primary
     * 
     * @param accountId the id of the account to set as primary
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/primary.html">Online Documentation</a>
     */
    public void setPrimaryAccount(String accountId) throws CoinbaseException;

    /**
     * Delete the current account
     * 
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/destroy.html">Online Documentation</a>
     */
    public void deleteAccount() throws UnspecifiedAccount, CoinbaseException;
    
    /**
     * Delete the specified account
     * 
     * @param accountId the id of the account to delete
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/destroy.html">Online Documentation</a>
     */
    public void deleteAccount(String accountId) throws CoinbaseException;

    /**
     * Update the settings of the current account
     * 
     * @param account an Account object containing the parameters to be updated
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/update.html">Online Documentation</a>
     */
    public void updateAccount(Account account) throws UnspecifiedAccount, CoinbaseException;
    
    /**
     * Update the settings of the current account
     * 
     * @param accountId the id of the account to update
     * @param account an Account object containing the parameters to be updated
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/update.html">Online Documentation</a>
     */
    public void updateAccount(String accountId, Account account) throws CoinbaseException;

    /**
     * Create a new account
     * 
     * @param account an Account object containing the parameters to create an account
     * @return the details of the created account
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/create.html">Online Documentation</a>
     */
    public Account createAccount(Account account) throws CoinbaseException;

    /**
     * Retrieve the current spot price of 1 BTC
     * 
     * @param currency the currency in which to retrieve the price
     * @return the spot price of 1 BTC in the specified currency
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/spot_rate.html">Online Documentation</a>
     */
    public Money getSpotPrice(CurrencyUnit currency);
    
    /**
     * Get a quote for purchasing a given amount of BTC
     * 
     * @param btcAmount the amount of bitcoin for which to retrieve a quote
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/buy.html">Online Documentation</a>
     */
    public Quote getBuyQuote(Money btcAmount);
    
    /**
     * Get a quote for selling a given amount of BTC
     * 
     * @param btcAmount the amount of bitcoin for which to retrieve a quote
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/sell.html">Online Documentation</a>
     */
    public Quote getSellQuote(Money btcAmount);

    /**
     * Create a new payment button, page, or iFrame.
     * 
     * @param button a Button object containing the parameters for creating a button
     * @return the created Button
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/buttons/create.html">Online Documentation</a>
     */
    public Button createButton(Button button) throws CoinbaseException;
    
    /**
     * Create an order for a new button
     * 
     * @param button a Button object containing the parameters for creating a button
     * @return the created Order
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/create.html">Online Documentation</a>
     */
    public Order createOrder(Button button) throws CoinbaseException;

    /**
     * Create an order for an existing button
     * 
     * @param buttonCode the code of the button for which to create an order
     * @return the created Order
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/buttons/create_order.html">Online Documentation</a>
     */
    public Order createOrderForButton(String buttonCode) throws CoinbaseException;

    /**
     * Sell a given quantity of BTC to Coinbase
     * 
     * @param amount the quantity of BTC to sell
     * @return the resulting Transfer object
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/sells/create.html">Online Documentation</a>
     */
    public Transfer sell(Money amount) throws CoinbaseException;
    
    /**
     * Sell a given quantity of BTC to Coinbase
     * 
     * @param amount the quantity of BTC to sell
     * @param paymentMethodId the ID of the payment method to credit with the proceeds of the sale
     * @return the resulting Transfer object
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/sells/create.html">Online Documentation</a>
     */
    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException;

    /**
     * Buy a given quantity of BTC from Coinbase
     * 
     * @param amount the quantity of BTC to buy
     * @return the resulting Transfer object
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/buys/create.html">Online Documentation</a>
     */
    public Transfer buy(Money amount) throws CoinbaseException;
    
    /**
     * Buy a given quantity of BTC from Coinbase
     * 
     * @param amount the quantity of BTC to buy
     * @param paymentMethodId the ID of the payment method to debit for the purchase
     * @return the resulting Transfer object
     * 
     * @see <a href="https://coinbase.com/api/doc/1.0/buys/create.html">Online Documentation</a>
     */
    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException;

    /**
     * Get the user's payment methods
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/payment_methods/index.html">Online Documentation</a>
     */
    public PaymentMethodsResponse getPaymentMethods();

    /**
     * Retrieve all the recurring payments (scheduled buys, sells, and subscriptions) you've created with merchants.
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getRecurringPayments();

    /**
     * Retrieve all the recurring payments (scheduled buys, sells, and subscriptions) you've created with merchants.
     *
     * @param page the page of recurring payments to retrieve
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getRecurringPayments(int page);

    /**
     * Retrieve all the subscriptions customers have made with you
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/subscribers/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getSubscribers();

    /**
     * Retrieve all the subscriptions customers have made with you
     *
     * @param page the page of subscribers to retrieve
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/subscribers/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getSubscribers(int page);
}
