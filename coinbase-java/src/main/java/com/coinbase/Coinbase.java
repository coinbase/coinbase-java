package com.coinbase;

import com.coinbase.v1.entity.Account;
import com.coinbase.v1.entity.AccountChangesResponse;
import com.coinbase.v1.entity.AccountsResponse;
import com.coinbase.v1.entity.Address;
import com.coinbase.v1.entity.AddressResponse;
import com.coinbase.v1.entity.AddressesResponse;
import com.coinbase.v1.entity.Application;
import com.coinbase.v1.entity.ApplicationsResponse;
import com.coinbase.v1.entity.Button;
import com.coinbase.v1.entity.ContactsResponse;
import com.coinbase.v1.entity.HistoricalPrice;
import com.coinbase.v1.entity.OAuthCodeRequest;
import com.coinbase.v1.entity.OAuthTokensResponse;
import com.coinbase.v1.entity.Order;
import com.coinbase.v1.entity.OrdersResponse;
import com.coinbase.v1.entity.PaymentMethodResponse;
import com.coinbase.v1.entity.PaymentMethodsResponse;
import com.coinbase.v1.entity.Quote;
import com.coinbase.v1.entity.RecurringPayment;
import com.coinbase.v1.entity.RecurringPaymentsResponse;
import com.coinbase.v1.entity.Report;
import com.coinbase.v1.entity.ReportsResponse;
import com.coinbase.v1.entity.Token;
import com.coinbase.v1.entity.Transaction;
import com.coinbase.v1.entity.TransactionsResponse;
import com.coinbase.v1.entity.Transfer;
import com.coinbase.v1.entity.TransfersResponse;
import com.coinbase.v1.entity.User;
import com.coinbase.v1.entity.UserResponse;
import com.coinbase.v1.exception.CoinbaseException;
import com.coinbase.v1.exception.CredentialsIncorrectException;
import com.coinbase.v1.exception.TwoFactorIncorrectException;
import com.coinbase.v1.exception.TwoFactorRequiredException;
import com.coinbase.v1.exception.UnauthorizedDeviceException;
import com.coinbase.v1.exception.UnspecifiedAccount;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.spotPrice.SpotPrice;
import com.coinbase.v2.models.transactions.Transactions;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;

public interface Coinbase {

    /**
     * Retrieve the current user and their settings.
     *
     * Also includes buy/sell levels (1, 2, or 3) and daily buy/sell limits (actual fiat amount).
     *
     * @return the current user and their settings
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/users/index.html">Online Documentation</a>
     * @deprecated
     */
    public User getUser() throws IOException, CoinbaseException;


    /**
     * Retrieve details of an individual transaction.
     *
     * @param id the transaction id or idem field value
     * @return the transaction details
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/show.html">Online Documentation</a>
     * @deprecated
     */
    public Transaction getTransaction(String id) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's recent transactions
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/index.html">Online Documentation</a>
     * @deprecated
     */
    public TransactionsResponse getTransactions() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's recent transactions
     *
     * @param page the page of transactions to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/index.html">Online Documentation</a>
     * @deprecated
     */
    public TransactionsResponse getTransactions(int page) throws IOException, CoinbaseException;

    /**
     * Send an invoice/money request to an email address.
     *
     * @param transaction a Transaction object containing the parameters for a money request
     *
     * @return the newly created transaction
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/request_money.html">Online Documentation</a>
     * @deprecated
     */
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException, IOException;

    /**
     * Resend emails for a money request.
     *
     * @param transactionId the id of the request money transaction to be resent
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/resend_request.html">Online Documentation</a>
     * @deprecated
     */
    public void resendRequest(String transactionId) throws CoinbaseException, IOException;

    /**
     * Cancel a money request.
     *
     * @param transactionId the id of the request money transaction to be canceled
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/cancel_request.html">Online Documentation</a>
     * @deprecated
     */
    public void deleteRequest(String transactionId) throws CoinbaseException, IOException;


    /**
     * Complete a money request.
     *
     * @param transactionId the id of the request money transaction to be completed
     * @return the completed transaction
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/complete_request.html">Online Documentation</a>
     * @deprecated
     */
    public Transaction completeRequest(String transactionId) throws CoinbaseException, IOException;


    /**
     * Send money to an email address or bitcoin address
     *
     * @param transaction a Transaction object containing the send money parameters
     * @return the newly created transaction
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transactions/send_money.html">Online Documentation</a>
     * @deprecated
     */
    public Transaction sendMoney(Transaction transaction) throws CoinbaseException, IOException;

    /**
     * Retrieve details of an individual merchant order
     *
     * @param idOrCustom the order id or custom field value
     * @return the details of the merchant order
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/show.html">Online Documentation</a>
     */
    public Order getOrder(String idOrCustom) throws IOException, CoinbaseException;


    /**
     * Retrieve a list of the user's recently received merchant orders
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/index.html">Online Documentation</a>
     */
    public OrdersResponse getOrders() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's recently received merchant orders
     *
     * @param page the page of orders to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/index.html">Online Documentation</a>
     */
    public OrdersResponse getOrders(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's recent transfers (buys/sales)
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transfers/index.html">Online Documentation</a>
     */
    public TransfersResponse getTransfers() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's recent transfers (buys/sales)
     *
     * @param page the page of transfers to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transfers/index.html">Online Documentation</a>
     */
    public TransfersResponse getTransfers(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of bitcoin addresses associated with this account
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/addresses/index.html">Online Documentation</a>
     */
    public AddressesResponse getAddresses() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of bitcoin addresses associated with this account
     *
     * @param page the page of addresses to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/addresses/index.html">Online Documentation</a>
     */
    public AddressesResponse getAddresses(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of accounts belonging to this user
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     * @deprecated
     */
    public AccountsResponse getAccounts() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of active accounts belonging to this user
     *
     * @param page the page of accounts to retrieve
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     * @deprecated
     */
    public AccountsResponse getAccounts(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of active accounts belonging to this user
     *
     * @param page the page of accounts to retrieve
     * @param limit the number of accounts to retrieve per page
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     * @deprecated
     */
    public AccountsResponse getAccounts(int page, int limit) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of accounts belonging to this user
     *
     * @param page the page of accounts to retrieve
     * @param limit the number of accounts to retrieve per page
     * @param includeInactive include inactive accounts in the response
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/index.html">Online Documentation</a>
     * @deprecated
     */
    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive) throws IOException, CoinbaseException;

    // TODO re-introduce limit param when BUGS-263 is fixed

    /**
     * Retrieve a list of the user's contacts
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts() throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's contacts
     *
     * @param page the page of accounts to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's contacts
     *
     * @param query partial string match to filter contacts.
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(String query) throws IOException, CoinbaseException;

    /**
     * Retrieve a list of the user's contacts
     *
     * @param page the page of accounts to retrieve
     * @param query partial string match to filter contacts.
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/contacts/index.html">Online Documentation</a>
     */
    public ContactsResponse getContacts(String query, int page) throws IOException, CoinbaseException;

    /**
     * Retrieve the balance of the current account
     *
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * @return the balance of the current account
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/balance.html">Online Documentation</a>
     */
    public Money getBalance() throws UnspecifiedAccount, IOException, CoinbaseException;

    /**
     * Retrieve the balance of the specified account
     *
     * @param accountId the id of the account for which to retrieve the balance
     * @return the balance of the specified account
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/balance.html">Online Documentation</a>
     */
    public Money getBalance(String accountId) throws IOException, CoinbaseException;

    /**
     * Set the current account as primary
     *
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/primary.html">Online Documentation</a>
     */
    public void setPrimaryAccount() throws UnspecifiedAccount, CoinbaseException, IOException;

    /**
     * Set the specified account as primary
     *
     * @param accountId the id of the account to set as primary
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/primary.html">Online Documentation</a>
     */
    public void setPrimaryAccount(String accountId) throws CoinbaseException, IOException;

    /**
     * Delete the current account
     *
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/destroy.html">Online Documentation</a>
     */
    public void deleteAccount() throws UnspecifiedAccount, CoinbaseException, IOException;

    /**
     * Delete the specified account
     *
     * @param accountId the id of the account to delete
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/destroy.html">Online Documentation</a>
     */
    public void deleteAccount(String accountId) throws CoinbaseException, IOException;

    /**
     * Update the settings of the current account
     *
     * @param account an Account object containing the parameters to be updated
     * @throws UnspecifiedAccount if the account was not specified during the creation of the client
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/update.html">Online Documentation</a>
     */
    public void updateAccount(Account account) throws UnspecifiedAccount, CoinbaseException, IOException;

    /**
     * Update the settings of the current account
     *
     * @param accountId the id of the account to update
     * @param account an Account object containing the parameters to be updated
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/update.html">Online Documentation</a>
     */
    public void updateAccount(String accountId, Account account) throws CoinbaseException, IOException;

    /**
     * Create a new account
     *
     * @param account an Account object containing the parameters to create an account
     * @return the details of the created account
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/accounts/create.html">Online Documentation</a>
     */
    public Account createAccount(Account account) throws CoinbaseException, IOException;

    /**
     * Retrieve the current spot price of 1 BTC
     *
     * @param currency the currency in which to retrieve the price
     * @return the spot price of 1 BTC in the specified currency
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/spot_rate.html">Online Documentation</a>
     * @deprecated
     */
    public Money getSpotPrice(CurrencyUnit currency) throws IOException, CoinbaseException;

    /**
     * Get a quote for purchasing a given amount of BTC
     *
     * @param amount the amount for which to retrieve a quote. Can be either bitcoin or native currency
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/buy.html">Online Documentation</a>
     */
    public Quote getBuyQuote(Money amount) throws IOException, CoinbaseException;

    /**
     * Get a quote for purchasing a given amount of BTC
     *
     * @param amount the amount for which to retrieve a quote. Can be either bitcoin or native currency
     * @param paymentMethodId the ID of the payment method for which to retrieve a quote
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/buy.html">Online Documentation</a>
     */
    public Quote getBuyQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException;

    /**
     * Get a quote for selling a given amount of BTC
     *
     * @param amount the amount for which to retrieve a quote. Can be either bitcoin or native currency
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/sell.html">Online Documentation</a>
     */
    public Quote getSellQuote(Money amount) throws IOException, CoinbaseException;

    /**
     * Get a quote for selling a given amount of BTC
     *
     * @param amount the amount for which to retrieve a quote. Can be either bitcoin or native currency
     * @param paymentMethodId the ID of the payment method for which to retrieve a quote
     * @throws IOException
     * @throws CoinbaseException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/sell.html">Online Documentation</a>
     */
    public Quote getSellQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException;

    /**
     * Create a new payment button, page, or iFrame.
     *
     * @param button a Button object containing the parameters for creating a button
     * @return the created Button
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/buttons/create.html">Online Documentation</a>
     */
    public Button createButton(Button button) throws CoinbaseException, IOException;

    /**
     * Create an order for a new button
     *
     * @param button a Button object containing the parameters for creating a button
     * @return the created Order
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/orders/create.html">Online Documentation</a>
     */
    public Order createOrder(Button button) throws CoinbaseException, IOException;

    /**
     * Create an order for an existing button
     *
     * @param buttonCode the code of the button for which to create an order
     * @return the created Order
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/buttons/create_order.html">Online Documentation</a>
     */
    public Order createOrderForButton(String buttonCode) throws CoinbaseException, IOException;

    /**
     * Sell a given quantity of BTC to Coinbase
     *
     * @param amount the quantity of BTC to sell
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/sells/create.html">Online Documentation</a>
     */
    public Transfer sell(Money amount) throws CoinbaseException, IOException;

    /**
     * Sell a given quantity of BTC to Coinbase
     *
     * @param amount the quantity of BTC to sell
     * @param paymentMethodId the ID of the payment method to credit with the proceeds of the sale
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/sells/create.html">Online Documentation</a>
     */
    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException, IOException;

    /**
     * Sell a given quantity of BTC to Coinbase
     *
     * @param amount the quantity of BTC to sell
     * @param paymentMethodId the ID of the payment method to credit with the proceeds of the sale
     * @param commit create the transfer in 'created' state
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/sells/create.html">Online Documentation</a>
     */
    public Transfer sell(Money amount, String paymentMethodId, Boolean commit) throws CoinbaseException, IOException;

    /**
     * Buy a given quantity of BTC from Coinbase
     *
     * @param amount the quantity of BTC to buy
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/buys/create.html">Online Documentation</a>
     */
    public Transfer buy(Money amount) throws CoinbaseException, IOException;

    /**
     * Buy a given quantity of BTC from Coinbase
     *
     * @param amount the quantity of BTC to buy
     * @param paymentMethodId the ID of the payment method to debit for the purchase
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/buys/create.html">Online Documentation</a>
     */
    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException, IOException;

    /**
     * Buy a given quantity of BTC from Coinbase
     *
     * @param amount the quantity of BTC to buy
     * @param paymentMethodId the ID of the payment method to debit for the purchase
     * @param commit create the transfer in 'created' state
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/buys/create.html">Online Documentation</a>
     */
    public Transfer buy(Money amount, String paymentMethodId, Boolean commit) throws CoinbaseException, IOException;

    /**
     * Start a transfer that is in the 'created' state
     *
     * @return the resulting Transfer object
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/transfers/index.html">Online Documentation</a>
     */
    public Transfer commitTransfer(String accountId, String transactionId) throws CoinbaseException, IOException;

    /**
     * Get the user's payment methods
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/payment_methods/index.html">Online Documentation</a>
     */
    public PaymentMethodsResponse getPaymentMethods() throws IOException, CoinbaseException;

    /**
     * Authenticated resource that returns one of the user’s payment methods.
     * @param id Payment method id
     * @return Payment method
     * @throws CoinbaseException
     * @throws IOException
     */
    public PaymentMethodResponse getPaymentMethod(String id) throws CoinbaseException, IOException;

    /**
     * Delete the specified payment method.
     *
     * @param id Id of the payment method to delete
     * @throws CoinbaseException
     * @throws IOException
     */
    public void deletePaymentMethod(String id) throws CoinbaseException, IOException;

    /**
     * Retrieve all the recurring payments (scheduled buys, sells, and subscriptions) you've created with merchants.
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getRecurringPayments() throws IOException, CoinbaseException;

    /**
     * Retrieve all the recurring payments (scheduled buys, sells, and subscriptions) you've created with merchants.
     *
     * @param page the page of recurring payments to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getRecurringPayments(int page) throws IOException, CoinbaseException;

    /**
     * Retrieve all the subscriptions customers have made with you
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/subscribers/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getSubscribers() throws IOException, CoinbaseException;

    /**
     * Retrieve all the subscriptions customers have made with you
     *
     * @param page the page of subscribers to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/subscribers/index.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/merchant_tools/recurring_payments">About recurring payments</a>
     */
    public RecurringPaymentsResponse getSubscribers(int page) throws IOException, CoinbaseException;

    /**
     * Unauthenticated resource that returns BTC to fiat (and vice versus) exchange rates in various currencies.
     *
     * It has keys for both btc_to_xxx and xxx_to_btc so you can convert either way. The key always contains downcase representations of the currency ISO.
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/currencies/exchange_rates.html">Online Documentation</a>
     */
    public Map<String, BigDecimal> getExchangeRates() throws IOException, CoinbaseException;

    /**
     * Unauthenticated resource that returns currencies supported on Coinbase
     * @throws CoinbaseException
     * @throws IOException
     *
     */
    public List<CurrencyUnit> getSupportedCurrencies() throws IOException, CoinbaseException;

    /**
     * Unauthenticated resource that creates a user with an email and password.
     *
     * @param userParams a User object containing the parameters to create a new User
     *
     * @return the created user
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/users/create.html">Online Documentation</a>
     *
     */
    public User createUser(User userParams) throws CoinbaseException, IOException;

    /**
     * Unauthenticated resource that creates a user with an email and password.
     *
     * @param userParams a User object containing the parameters to create a new User
     * @param clientId your OAuth application's client id
     * @param scope a space-separated list of Coinbase OAuth permissions
     *
     * @return the created user
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/users/create.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/api/permissions">Permissions Reference</a>
     *
     */
    public User createUser(User userParams, String clientId, String scope) throws CoinbaseException, IOException;

    /**
     * Authenticated resource that creates a user with an email and password and gets the OAuth
     * tokens from the server.
     *
     * @param userParams a User object containing the parameters to create a new User
     * @param clientId your OAuth application's client id
     * @param scope a space-separated list of Coinbase OAuth permissions
     *
     * @return the user response
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/users/create.html">Online Documentation</a>
     * @see <a href="https://coinbase.com/docs/api/permissions">Permissions Reference</a>
     *
     */
    public UserResponse createUserWithOAuth(User userParams, String clientId, String scope) throws CoinbaseException, IOException;

    /**
     * Updates account settings for the current user
     *
     * @param userId the user's id
     * @param userParams a User object containing the parameters to update
     *
     * @return the updated user
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/users/update.html">Online Documentation</a>
     * @deprecated
     */
    public User updateUser(String userId, User userParams) throws CoinbaseException, IOException;

    /**
     * Retrieves the details of a recurring payment
     *
     * @param id the id of the recurring payment
     *
     * @return the recurring payment details
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/show.html">Online Documentation</a>
     *
     */
    public RecurringPayment getRecurringPayment(String id) throws CoinbaseException, IOException;

    /**
     * Retrieves the details of a subscriber's recurring payment
     *
     * @param id the id of the recurring payment
     *
     * @return the recurring payment details
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/recurring_payments/show.html">Online Documentation</a>
     *
     */
    public RecurringPayment getSubscriber(String id) throws CoinbaseException, IOException;

    /**
     * Create a token which can be redeemed for bitcoin
     *
     * @return the newly created token
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/tokens/create.html">Online Documentation</a>
     *
     */
    public Token createToken() throws CoinbaseException, IOException;

    /**
     * Redeem a token, claiming its address and all its bitcoins
     *
     * @param tokenId the id of the token
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/tokens/redeem.html">Online Documentation</a>
     *
     */
    public void redeemToken(String tokenId) throws CoinbaseException, IOException;

    /**
     * Generate a new receive address
     *
     * @param addressParams an Address object containing any arguments for the new address
     *
     * @return the generated address
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/account/generate_receive_address.html">Online Documentation</a>
     *
     */
    public AddressResponse generateReceiveAddress(Address addressParams) throws CoinbaseException, IOException;

    /**
     * Generate a new receive address
     *
     * @return the generated address
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/account/generate_receive_address.html">Online Documentation</a>
     *
     */
    public AddressResponse generateReceiveAddress() throws CoinbaseException, IOException;

    /**
     * Retrieve details for an individual OAuth application.
     *
     * @param id the id of the OAuth application
     *
     * @return the details for the OAuth application
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/applications/show.html">Online Documentation</a>
     *
     */
    public Application getApplication(String id) throws CoinbaseException, IOException;

    /**
     * List OAuth applications on your account.
     *
     * @param id the id of the OAuth application
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/applications/index.html">Online Documentation</a>
     *
     */
    public ApplicationsResponse getApplications() throws CoinbaseException, IOException;

    /**
     * Create a new OAuth application
     *
     * @param applicationParams an Application object containing the arguments for creating an OAuth application
     *
     * @return the newly created OAuth application including client id and client secret
     *
     * @throws CoinbaseException on error
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/applications/create.html">Online Documentation</a>
     *
     */
    public Application createApplication(Application application) throws CoinbaseException, IOException;

    /**
     * Get the historical spot price of bitcoin in USD.
     *
     * @param page the page of results to retrieve
     *
     * @return a list of HistoricalPrice
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/historical.html">Online Documentation</a>
     *
     */
    public List<HistoricalPrice> getHistoricalPrices(int page) throws CoinbaseException, IOException;

    /**
     * Get the historical spot price of bitcoin in USD.
     *
     * @return a list of HistoricalPrice
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/prices/historical.html">Online Documentation</a>
     *
     */
    public List<HistoricalPrice> getHistoricalPrices() throws CoinbaseException, IOException;

    /**
     * Create a new report
     *
     * @return the newly created report
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/reports/create.html">Online Documentation</a>
     *
     */
    public Report createReport(Report reportParams) throws CoinbaseException, IOException;

    /**
     * Retrieve details for a report
     *
     * @param  reportId the id of the report to retrieve
     * @return the requested report
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/reports/show.html">Online Documentation</a>
     *
     */
    public Report getReport(String reportId) throws CoinbaseException, IOException;

    /**
     * Retrieve all reports
     *
     * @param  page the page of results to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/reports/index.html">Online Documentation</a>
     *
     */
    public ReportsResponse getReports(int page) throws CoinbaseException, IOException;

    /**
     * Retrieve all reports
     *
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/reports/index.html">Online Documentation</a>
     *
     */
    public ReportsResponse getReports() throws CoinbaseException, IOException;

    /**
     * Retrieve account changes
     *
     * @param  page the page of results to retrieve
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/account_changes/index.html">Online Documentation</a>
     *
     */
    public AccountChangesResponse getAccountChanges(int page) throws CoinbaseException, IOException;

    /**
     * Retrieve account changes
     *
     * @throws CoinbaseException
     * @throws IOException
     *
     * @see <a href="https://coinbase.com/api/doc/1.0/account_changes/index.html">Online Documentation</a>
     *
     */
    public AccountChangesResponse getAccountChanges() throws CoinbaseException, IOException;

    /**
     * Obtain OAuth Authorization Code
     *
     * @throws CredentialsIncorrectException if the username/password are incorrect
     * @throws TwoFactorRequiredException if the user has 2fa enabled but a 2fa code was not provided
     * @throws TwoFactorIncorrectException if the provided two factor code is incorrect or expired
     * @throws CoinbaseException
     * @throws IOException
     *
     */
    public String getAuthCode(OAuthCodeRequest request)
            throws CredentialsIncorrectException, TwoFactorRequiredException, TwoFactorIncorrectException, CoinbaseException, IOException;

    /**
     * Exchange OAuth Authorization Code for OAuth token
     *
     * @param redirectUri required iff a redirectUri was set when the auth code was obtained
     *
     * @throws UnauthorizedDeviceException if the authCode was obtained using email/password and device verification has not yet been completed by the user
     * @throws CoinbaseException
     * @throws IOException
     *
     */
    public OAuthTokensResponse getTokens(String clientId, String clientSecret, String authCode, String redirectUri) throws UnauthorizedDeviceException, CoinbaseException, IOException;

    /**
     * Revoke current access token.
     *
     * This client must have previously been initialized with an access token.
     * Future methods requiring authentication will fail.
     *
     * @throws CoinbaseException
     * @throws IOException
     */
    public void revokeToken() throws CoinbaseException, IOException;

    /**
     * Refresh OAuth tokens
     *
     * @throws CoinbaseException
     * @throws IOException
     *
     */
    public OAuthTokensResponse refreshTokens(String clientId, String clientSecret, String refreshToken) throws CoinbaseException, IOException;

    /**
     * Send 2FA token to user via SMS
     *
     * @throws CoinbaseException
     * @throws IOException
     */
    public void sendSMS(String clientId, String clientSecret, String email, String password) throws CoinbaseException, IOException;

    /**
     * Get Three-Legged OAuth Authorization URI
     *
     * @throws CoinbaseException
     */
    public URI getAuthorizationUri(OAuthCodeRequest params) throws CoinbaseException;

    /**
     * Transfer money between accounts
     *
     * @param amount Amount to transfer, in BTC
     * @param toAccountId Account to transfer money to
     *
     * @return Resulting transaction
     * @throws CoinbaseException
     * @throws IOException
     */
    public Transaction transferMoneyBetweenAccounts(String amount, String toAccountId) throws CoinbaseException, IOException;

    /*
     * Verify authenticity of merchant callback from Coinbase
     *
     */
    boolean verifyCallback(String body, String signature);





    /**
     * Retrieve the current user and their settings.
     *
     *
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-user">Online Documentation</a>
     */
    public Call getUser(final Callback<com.coinbase.v2.models.user.User> callback);

    /**
     * Modify current user and their preferences
     *
     * @param name User's public name
     * @param timeZone Time zone
     * @param nativeCurrency Local currency used to display amounts converted from BTC
     * @param callback callback interface
     *
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#update-current-user">Online Documentation</a>
     *
     */
    public Call updateUser(String name, String timeZone, String nativeCurrency, final Callback<com.coinbase.v2.models.user.User> callback);

    /**
     * Retrieve an account belonging to this user
     * @param accountId account ID for the account to retrieve
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-an-account">Online Documentation</a>
     */
    public Call getAccount(String accountId, final Callback<com.coinbase.v2.models.account.Account> callback);

    /**
     * Retrieve a list of accounts belonging to this user
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#list-accounts">Online Documentation</a>
     */
    public Call getAccounts(final Callback<Accounts> callback);

    /**
     * Retrieve details of an individual transaction.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the transaction id or idem field value
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-transaction">Online Documentation</a>
     */
    public Call getTransaction(String accountId, String transactionId, final Callback<com.coinbase.v2.models.transactions.Transaction> callback);

    /**
     * Retrieve a list of the user's recent transactions.
     * @param accountId account ID that the transaction belongs to
     * @param options endpoint options
     * @param expandOptions expand options
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#list-transactions">Online Documentation</a>
     */
    public Call getTransactions(String accountId,
                                HashMap<String, Object> options,
                                List<String> expandOptions,
                                final Callback<Transactions> callback);

    /**
     * Complete a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be completed
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#complete-request-money">Online Documentation</a>
     */
    public Call completeRequest(String accountId, String transactionId, final Callback<Void> callback);

    /**
     * Resend emails for a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be resent
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#re-send-request-money">Online Documentation</a>
     */
    public Call resendRequest(String accountId, String transactionId, final Callback<Void> callback);

    /**
     * Cancel a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be cancelled
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#cancel-request-money">Online Documentation</a>
     */
    public Call cancelRequest(String accountId, String transactionId, final Callback<Void> callback);

    /**
     * Send money to an email address or bitcoin address
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#send-money">Online Documentation</a>
     */
    public Call sendMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback);

    /**
     * Request money from an email address or bitcoin address
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#request-money">Online Documentation</a>
     */
    public Call requestMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback);

    /**
     * Transfer bitcoin between two of a user’s accounts
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#transfer-money-between-accounts">Online Documentation</a>
     */
    public Call transferMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback);

    /**
     * Retrieve the current spot price of 1 BTC
     *
     * @param currency the currency in which to retrieve the price
     * @return the spot price of 1 BTC in the specified currency
     *
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getSpotPrice(String currency, final Callback<SpotPrice> callback);
}
