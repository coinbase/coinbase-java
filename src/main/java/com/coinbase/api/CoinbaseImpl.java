package com.coinbase.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.money.Money;
import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVReader;

import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountChangesResponse;
import com.coinbase.api.entity.AccountResponse;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.entity.Address;
import com.coinbase.api.entity.AddressResponse;
import com.coinbase.api.entity.AddressesResponse;
import com.coinbase.api.entity.Application;
import com.coinbase.api.entity.ApplicationResponse;
import com.coinbase.api.entity.ApplicationsResponse;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.ButtonResponse;
import com.coinbase.api.entity.ContactsResponse;
import com.coinbase.api.entity.HistoricalPrice;
import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.OrderResponse;
import com.coinbase.api.entity.OrdersResponse;
import com.coinbase.api.entity.PaymentMethodsResponse;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.RecurringPayment;
import com.coinbase.api.entity.RecurringPaymentResponse;
import com.coinbase.api.entity.RecurringPaymentsResponse;
import com.coinbase.api.entity.Report;
import com.coinbase.api.entity.ReportResponse;
import com.coinbase.api.entity.ReportsResponse;
import com.coinbase.api.entity.Request;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Token;
import com.coinbase.api.entity.TokenResponse;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionResponse;
import com.coinbase.api.entity.TransactionsResponse;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransferResponse;
import com.coinbase.api.entity.TransfersResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.entity.UserResponse;
import com.coinbase.api.entity.UsersResponse;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnauthorizedException;
import com.coinbase.api.exception.UnspecifiedAccount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class CoinbaseImpl implements Coinbase {

    private static final ObjectMapper objectMapper = ObjectMapperProvider.createDefaultMapper();
    
    private URL    _baseUrl;
    private String _accountId;
    private String _apiKey;
    private String _apiSecret;
    private String _accessToken;
    private SSLSocketFactory _socketFactory;

    CoinbaseImpl(CoinbaseBuilder builder) {

        try {
            // This class expects URL.openConnection() to return an instance of
            // javax.net.ssl.HttpsURLConnection. If another https handler had already
            // been resolved, openConnection() can return other implementations.
            // One instance this is a problem is with Weblogic. By default Weblogic
            // will return weblogic.net.http.SOAPHttpsURLConnection which is not
            // an instance of HttpsUrlConnection. To resolve, specify the default
            // sun https handler.
            _baseUrl = new URL(null, "https://coinbase.com/api/v1/", new sun.net.www.protocol.https.Handler());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        _apiKey = builder.api_key;
        _apiSecret = builder.api_secret;
        _accessToken = builder.access_token;
        _accountId = builder.acct_id;

        // Register BTC as a currency since Android won't let joda read from classpath resources
        try {
            CurrencyUnit.registerCurrency("BTC", -1, 8, new ArrayList<String>());
        } catch (IllegalArgumentException ex) {}

        _socketFactory = CoinbaseSSL.getSSLSocketFactory();

    }

    public User getUser() throws IOException, CoinbaseException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(usersUrl, UsersResponse.class).getUsers().get(0).getUser();
    }

    public AccountsResponse getAccounts() throws IOException, CoinbaseException {
        return getAccounts(1, 25, false);
    }

    public AccountsResponse getAccounts(int page) throws IOException, CoinbaseException {
        return getAccounts(page, 25, false);
    }

    public AccountsResponse getAccounts(int page, int limit) throws IOException, CoinbaseException {
        return getAccounts(page, limit, false);
    }

    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive) throws IOException, CoinbaseException {
        URL accountsUrl;
        try {
            accountsUrl = new URL(
                    _baseUrl,
                    "accounts?" +
                    "&page=" + page +
                    "&limit=" + limit +
                    "&all_accounts=" + (includeInactive ? "true" : "false")
            );   
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(accountsUrl, AccountsResponse.class);
    }

    public Money getBalance() throws IOException, CoinbaseException {
        if (_accountId != null) {
            return getBalance(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    public Money getBalance(String accountId) throws IOException, CoinbaseException {
        URL accountBalanceUrl;
        try {
            accountBalanceUrl = new URL(_baseUrl, "accounts/" + accountId + "/balance");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return deserialize(doHttp(accountBalanceUrl, "GET", null), Money.class);
    }

    public void setPrimaryAccount(String accountId) throws CoinbaseException, IOException {
        URL setPrimaryUrl;
        try {
            setPrimaryUrl = new URL(_baseUrl, "accounts/" + accountId + "/primary");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        post(setPrimaryUrl, new Request(), Response.class);
    }

    public void deleteAccount(String accountId) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseUrl, "accounts/" + accountId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        delete(accountUrl, Response.class);
    }

    public void setPrimaryAccount() throws CoinbaseException, IOException {
        if (_accountId != null) {
            setPrimaryAccount(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    public void deleteAccount() throws CoinbaseException, IOException {
        if (_accountId != null) {
            deleteAccount(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    public void updateAccount(Account account) throws CoinbaseException, IOException, UnspecifiedAccount {
        if (_accountId != null) {
            updateAccount(_accountId, account);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    public Account createAccount(Account account) throws CoinbaseException, IOException {
        URL accountsUrl;
        try {
            accountsUrl = new URL(_baseUrl, "accounts");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setAccount(account);

        return post(accountsUrl, request, AccountResponse.class).getAccount();
    }

    public void updateAccount(String accountId, Account account) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseUrl, "accounts/" + accountId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }

        Request request = new Request();
        request.setAccount(account);
        put(accountUrl, request, Response.class);
    }

    public Money getSpotPrice(CurrencyUnit currency) throws IOException, CoinbaseException {
        URL spotPriceUrl;
        try {
            spotPriceUrl = new URL(_baseUrl, "prices/spot_rate?currency=" + URLEncoder.encode(currency.getCurrencyCode(), "UTF-8"));
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(spotPriceUrl, "GET", null), Money.class);
    }

    public Quote getBuyQuote(Money btcAmount) throws IOException, CoinbaseException {
        if (!btcAmount.getCurrencyUnit().getCode().equals("BTC")) {
            throw new CoinbaseException("Only BTC amounts are supported for quotes");
        }

        URL buyPriceUrl;
        try {
            buyPriceUrl = new URL(
                _baseUrl,
                "prices/buy?qty=" + URLEncoder.encode(btcAmount.getAmount().toPlainString(), "UTF-8")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(buyPriceUrl, "GET", null), Quote.class);
    }

    public Quote getSellQuote(Money btcAmount) throws IOException, CoinbaseException {
        if (!btcAmount.getCurrencyUnit().getCode().equals("BTC")) {
            throw new CoinbaseException("Only BTC amounts are supported for quotes");
        }

        URL sellPriceUrl;
        try {
            sellPriceUrl = new URL(
                _baseUrl,
                "prices/sell?qty=" + URLEncoder.encode(btcAmount.getAmount().toPlainString(), "UTF-8")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(sellPriceUrl, "GET", null), Quote.class);
    }

    public TransactionsResponse getTransactions(int page) throws IOException, CoinbaseException {
        URL transactionsUrl;
        try {
            transactionsUrl = new URL(
                _baseUrl,
                "transactions?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(transactionsUrl, TransactionsResponse.class);
    }

    public TransactionsResponse getTransactions() throws IOException, CoinbaseException {
        return getTransactions(1);
    }

    public Transaction getTransaction(String id) throws IOException, CoinbaseException {
        URL transactionUrl;
        try {
            transactionUrl = new URL(_baseUrl, "transactions/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return get(transactionUrl, TransactionResponse.class).getTransaction();
    }

    public Transaction requestMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL requestMoneyUrl;
        try {
            requestMoneyUrl = new URL(_baseUrl, "transactions/request_money");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        if (transaction.getAmount() == null) {
            throw new CoinbaseException("Amount is a required field");
        }

        Request request = newAccountSpecificRequest();
        request.setTransaction(transaction);

        return post(requestMoneyUrl, request, TransactionResponse.class).getTransaction();
    }

    public void resendRequest(String id) throws CoinbaseException, IOException {
        URL resendRequestUrl;
        try {
            resendRequestUrl = new URL(_baseUrl, "transactions/" + id + "/resend_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        put(resendRequestUrl, newAccountSpecificRequest(), Response.class);
    }

    public void deleteRequest(String id) throws CoinbaseException, IOException {
        URL cancelRequestUrl;
        try {
            cancelRequestUrl = new URL(_baseUrl, "transactions/" + id + "/cancel_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        delete(cancelRequestUrl, Response.class);
    }

    public Transaction completeRequest(String id) throws CoinbaseException, IOException {
        URL completeRequestUrl;
        try {
            completeRequestUrl = new URL(_baseUrl, "transactions/" + id + "/complete_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return put(completeRequestUrl, newAccountSpecificRequest(), TransactionResponse.class).getTransaction();
    }

    public Transaction sendMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL sendMoneyUrl;
        try {
            sendMoneyUrl = new URL(_baseUrl, "transactions/send_money");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        if (transaction.getAmount() == null) {
            throw new CoinbaseException("Amount is a required field");
        }

        Request request = newAccountSpecificRequest();
        request.setTransaction(transaction);

        return post(sendMoneyUrl, request, TransactionResponse.class).getTransaction();
    }

    public TransfersResponse getTransfers(int page) throws IOException, CoinbaseException {
        URL transfersUrl;
        try {
            transfersUrl = new URL(
                _baseUrl,
                "transfers?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(transfersUrl, TransfersResponse.class);
    }

    public TransfersResponse getTransfers() throws IOException, CoinbaseException {
        return getTransfers(1);
    }

    public Transfer sell(Money amount) throws CoinbaseException, IOException {
        return sell(amount, null);
    }

    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
            throw new CoinbaseException(
                "Cannot sell " + amount.getCurrencyUnit().getCode()
                + " Only BTC amounts are supported for sells"
            );
        }

        URL sellsUrl;
        try {
            sellsUrl = new URL(_baseUrl, "sells");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setQty(amount.getAmount().doubleValue());
        request.setPaymentMethodId(paymentMethodId);

        return post(sellsUrl, request, TransferResponse.class).getTransfer();
    }

    public Transfer buy(Money amount) throws CoinbaseException, IOException {
        return buy(amount, null);
    }

    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
            throw new CoinbaseException(
                "Cannot buy " + amount.getCurrencyUnit().getCode()
                + " Only BTC amounts are supported for buys"
            );
        }

        URL buysUrl;
        try {
            buysUrl = new URL(_baseUrl, "buys");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setQty(amount.getAmount().doubleValue());
        request.setPaymentMethodId(paymentMethodId);

        return post(buysUrl, request, TransferResponse.class).getTransfer();
    }

    public Order getOrder(String idOrCustom) throws IOException, CoinbaseException {
        URL orderUrl;
        try {
            orderUrl = new URL(_baseUrl, "orders/" + idOrCustom);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid order id/custom");
        }
        return get(orderUrl, OrderResponse.class).getOrder();
    }

    public OrdersResponse getOrders(int page) throws IOException, CoinbaseException {
        URL ordersUrl;
        try {
            ordersUrl = new URL(
                _baseUrl,
                "orders?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid  account id");
        }
        return get(ordersUrl, OrdersResponse.class);
    }

    public OrdersResponse getOrders() throws IOException, CoinbaseException {
        return getOrders(1);
    }

    public AddressesResponse getAddresses(int page) throws IOException, CoinbaseException {
        URL addressesUrl;
        try {
            addressesUrl = new URL(
                _baseUrl,
                "addresses?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(addressesUrl, AddressesResponse.class);
    }

    public AddressesResponse getAddresses() throws IOException, CoinbaseException {
        return getAddresses(1);
    }

    public ContactsResponse getContacts(int page) throws IOException, CoinbaseException {
        URL contactsUrl;
        try {
            contactsUrl = new URL(
                _baseUrl,
                "contacts?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(contactsUrl, ContactsResponse.class);
    }

    public ContactsResponse getContacts() throws IOException, CoinbaseException {
        return getContacts(1);
    }

    public ContactsResponse getContacts(String query, int page) throws IOException, CoinbaseException {
        URL contactsUrl;
        try {
            contactsUrl = new URL(
                _baseUrl,
                "contacts?page=" + page + 
                "&query=" + URLEncoder.encode(query, "UTF-8")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(contactsUrl, ContactsResponse.class);
    }

    public ContactsResponse getContacts(String query) throws IOException, CoinbaseException {
        return getContacts(query, 1);
    }

    public Map<String, BigDecimal> getExchangeRates() throws IOException, CoinbaseException {
        URL ratesUrl;
        try {
            ratesUrl = new URL(_baseUrl, "currencies/exchange_rates");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(ratesUrl, "GET", null), new TypeReference<HashMap<String, BigDecimal>>() {});
    }

    public List<CurrencyUnit> getSupportedCurrencies() throws IOException, CoinbaseException {
        URL currenciesUrl;
        try {
            currenciesUrl = new URL(_baseUrl, "currencies");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        List<List<String>> rawResponse = 
                deserialize(doHttp(currenciesUrl, "GET", null), new TypeReference<List<List<String>>>() {});

        List<CurrencyUnit> result = new ArrayList<CurrencyUnit>();
        for (List<String> currency : rawResponse) {
            try {
                result.add(CurrencyUnit.getInstance(currency.get(1)));
            } catch (IllegalCurrencyException ex) {}
        }

        return result;
    }

    public List<HistoricalPrice> getHistoricalPrices(int page) throws CoinbaseException, IOException {
        URL historicalPricesUrl;
        try {
            historicalPricesUrl = new URL(
                _baseUrl,
                "prices/historical?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        String responseBody = doHttp(historicalPricesUrl, "GET", null);

        CSVReader reader = new CSVReader(new StringReader(responseBody));

        ArrayList<HistoricalPrice> result = new ArrayList<HistoricalPrice>();
        String[] nextLine;

        try {
            while ((nextLine = reader.readNext()) != null) {
                HistoricalPrice hp = new HistoricalPrice();
                hp.setTime(DateTime.parse(nextLine[0]));
                hp.setSpotPrice(Money.of(CurrencyUnit.USD, new BigDecimal(nextLine[1])));
                result.add(hp);
            }
        } catch (IOException e) {
            throw new CoinbaseException("Error parsing csv response");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {}
        }

        return result;
    }

    public List<HistoricalPrice> getHistoricalPrices() throws CoinbaseException, IOException {
        return getHistoricalPrices(1);
    }

    public Button createButton(Button button) throws CoinbaseException, IOException {
        URL buttonsUrl;
        try {
            buttonsUrl = new URL(_baseUrl, "buttons");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setButton(serializePrice(button));

        return post(buttonsUrl, request, ButtonResponse.class).getButton();
    }

    public Order createOrder(Button button) throws CoinbaseException, IOException {
        URL ordersUrl;
        try {
            ordersUrl = new URL(_baseUrl, "orders");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setButton(serializePrice(button));

        return post(ordersUrl, request, OrderResponse.class).getOrder();
    }

    public Order createOrderForButton(String buttonCode) throws CoinbaseException, IOException {
        URL createOrderForButtonUrl;
        try {
            createOrderForButtonUrl = new URL(_baseUrl, "buttons/" + buttonCode + "/create_order");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid button code");
        }

        return post(createOrderForButtonUrl, new Request(), OrderResponse.class).getOrder();
    }

    public PaymentMethodsResponse getPaymentMethods() throws IOException, CoinbaseException {
        URL paymentMethodsUrl;
        try {
            paymentMethodsUrl = new URL(_baseUrl, "payment_methods");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(paymentMethodsUrl, PaymentMethodsResponse.class);
    }

    public RecurringPaymentsResponse getSubscribers(int page) throws IOException, CoinbaseException {
        URL subscribersUrl;
        try {
            subscribersUrl = new URL(
                _baseUrl,
                "subscribers?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(subscribersUrl, RecurringPaymentsResponse.class);
    }

    public RecurringPaymentsResponse getSubscribers() throws IOException, CoinbaseException {
        return getSubscribers(1);
    }

    public RecurringPaymentsResponse getRecurringPayments(int page) throws IOException, CoinbaseException {
        URL recurringPaymentsUrl;
        try {
            recurringPaymentsUrl = new URL(
                _baseUrl,
                "recurring_payments?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(recurringPaymentsUrl, RecurringPaymentsResponse.class);
    }

    public RecurringPaymentsResponse getRecurringPayments() throws IOException, CoinbaseException {
        return getRecurringPayments(1);
    }

    public RecurringPayment getRecurringPayment(String id) throws CoinbaseException, IOException {
        URL recurringPaymentUrl;
        try {
            recurringPaymentUrl = new URL(_baseUrl, "recurring_payments/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid payment id");
        }

        return get(recurringPaymentUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    public RecurringPayment getSubscriber(String id) throws CoinbaseException, IOException {
        URL subscriberUrl;
        try {
            subscriberUrl = new URL(_baseUrl, "subscribers/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid subscriber id");
        }

        return get(subscriberUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    public AddressResponse generateReceiveAddress(Address addressParams) throws CoinbaseException, IOException {
        URL generateAddressUrl;
        try {
            generateAddressUrl = new URL(_baseUrl, "account/generate_receive_address");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setAddress(addressParams);

        return post(generateAddressUrl, request, AddressResponse.class);
    }

    public AddressResponse generateReceiveAddress() throws CoinbaseException, IOException {
        return generateReceiveAddress(null);
    }

    public User createUser(User userParams) throws CoinbaseException, IOException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setUser(userParams);

        return post(usersUrl, request, UserResponse.class).getUser();
    }

    public User createUser(User userParams, String clientId, String scope) throws CoinbaseException, IOException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setUser(userParams);
        request.setScopes(scope);
        request.setClientId(clientId);

        return post(usersUrl, request, UserResponse.class).getUser();
    }

    public User updateUser(String userId, User userParams) throws CoinbaseException, IOException {
        URL userUrl;
        try {
            userUrl = new URL(_baseUrl, "users/" + userId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid user id");
        }

        Request request = new Request();
        request.setUser(userParams);

        return put(userUrl, request, UserResponse.class).getUser();
    }

    public Token createToken() throws CoinbaseException, IOException {
        URL tokensUrl;
        try {
            tokensUrl = new URL(_baseUrl, "tokens");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return post(tokensUrl, new Request(), TokenResponse.class).getToken();
    }

    public void redeemToken(String tokenId) throws CoinbaseException, IOException {
        URL redeemTokenUrl;
        try {
            redeemTokenUrl = new URL(_baseUrl, "tokens/redeem");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setTokenId(tokenId);

        post(redeemTokenUrl, request, Response.class);
    }

    public Application createApplication(Application applicationParams) throws CoinbaseException, IOException {
        URL applicationsUrl;
        try {
            applicationsUrl = new URL(_baseUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setApplication(applicationParams);

        return post(applicationsUrl, request, ApplicationResponse.class).getApplication();
    }

    public ApplicationsResponse getApplications() throws IOException, CoinbaseException {
        URL applicationsUrl;
        try {
            applicationsUrl = new URL(_baseUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(applicationsUrl, ApplicationsResponse.class);
    }

    public Application getApplication(String id) throws IOException, CoinbaseException {
        URL applicationUrl;
        try {
            applicationUrl = new URL(_baseUrl, "oauth/applications/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid application id");
        }
        return get(applicationUrl, ApplicationResponse.class).getApplication();
    }

    public Report createReport(Report reportParams) throws CoinbaseException, IOException {
        URL reportsUrl;
        try {
            reportsUrl = new URL(_baseUrl, "reports");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setReport(reportParams);

        return post(reportsUrl, request, ReportResponse.class).getReport();
    }

    public Report getReport(String reportId) throws IOException, CoinbaseException {
        URL reportUrl;
        try {
            reportUrl = new URL(
                _baseUrl,
                "reports/" + reportId +
                (_accountId != null ? "?account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid report id");
        }
        return get(reportUrl, ReportResponse.class).getReport();
    }

    public ReportsResponse getReports(int page) throws IOException, CoinbaseException {
        URL reportsUrl;
        try {
            reportsUrl = new URL(
                _baseUrl,
                "reports?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(reportsUrl, ReportsResponse.class);
    }

    public ReportsResponse getReports() throws IOException, CoinbaseException {
        return getReports(1);
    }

    public AccountChangesResponse getAccountChanges(int page) throws IOException, CoinbaseException {
        URL accountChangesUrl;
        try {
            accountChangesUrl = new URL(
                _baseUrl,
                "account_changes?page=" + page +
                (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(accountChangesUrl, AccountChangesResponse.class);
    }

    public AccountChangesResponse getAccountChanges() throws IOException, CoinbaseException {
        return getAccountChanges(1);
    }

    private void doHmacAuthentication (URL url, String body, HttpsURLConnection conn) throws IOException {
        String nonce = String.valueOf(System.currentTimeMillis());

        String message = nonce + url.toString() + (body != null ? body : "");

        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(_apiSecret.getBytes(), "HmacSHA256"));
        } catch (Throwable t) {
            throw new IOException(t);
        }

        String signature = new String(Hex.encodeHex(mac.doFinal(message.getBytes())));

        conn.setRequestProperty("ACCESS_KEY", _apiKey);
        conn.setRequestProperty("ACCESS_SIGNATURE", signature);
        conn.setRequestProperty("ACCESS_NONCE", nonce);
    }

    private void doAccessTokenAuthentication(HttpsURLConnection conn) {
        conn.setRequestProperty("Authorization", "Bearer " + _accessToken);
    }

    private String doHttp(URL url, String method, Object requestBody) throws IOException, CoinbaseException {
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(_socketFactory);
        conn.setRequestMethod(method);

        String body = null;
        if (requestBody != null) {
            body = objectMapper.writeValueAsString(requestBody);
            conn.setRequestProperty("Content-Type", "application/json");
        }

        if (_apiKey != null && _apiSecret != null) {
            doHmacAuthentication(url, body, conn);
        } else if (_accessToken != null) {
            doAccessTokenAuthentication(conn);
        }

        if (body != null) {
            conn.setDoOutput(true);
            OutputStream outputStream = conn.getOutputStream();
            try {
                outputStream.write(body.getBytes(Charset.forName("UTF-8")));
            } finally {
                outputStream.close();
            }
        }

        InputStream is = null;
        InputStream es = null;
        try {
            is = conn.getInputStream();
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            es = conn.getErrorStream();
            String errorMessage = null;
            if (es != null) {
                errorMessage = IOUtils.toString(es, "UTF-8");
            }
            if (HttpsURLConnection.HTTP_UNAUTHORIZED == conn.getResponseCode()) {
              throw new UnauthorizedException(errorMessage);
            }
            if (conn.getContentType().toLowerCase().contains("json")) {
                CoinbaseException cbEx = new CoinbaseException(errorMessage);
                cbEx.addSuppressed(e);
                throw cbEx;
            }
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }

            if (es != null) {
                es.close();
            }
        }
    }

    private static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    private static <T> T deserialize(String json, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(json, typeReference);
    }

    private <T extends Response> T get(URL url, Class<T> responseClass) throws IOException, CoinbaseException {
        return handleErrors(deserialize(doHttp(url, "GET", null), responseClass));
    }

    private <T extends Response> T post(URL url, Object entity, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "POST", entity), responseClass));
    }

    private <T extends Response> T put(URL url, Object entity, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "PUT", entity), responseClass));
    }

    private <T extends Response> T delete(URL url, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "DELETE", null), responseClass));
    }

    private static Button serializePrice(Button button) throws CoinbaseException {
        if (button.getPrice() == null) {
            throw new CoinbaseException("Price is a required field");
        }

        // Massage amount
        Money price = button.getPrice();
        button.setPrice(null);

        button.setPriceString(price.getAmount().toPlainString());
        button.setPriceCurrencyIso(price.getCurrencyUnit().getCurrencyCode());

        return button;
    }

    private static <T extends Response> T handleErrors(T response) throws CoinbaseException {
        if (response.hasErrors()) {
            throw new CoinbaseException(response.getErrors());
        }

        if (response.isSuccess() != null && !response.isSuccess()) {
            throw new CoinbaseException("Unknown error");
        }

        return response;
    }

    private Request newAccountSpecificRequest() {
        Request request = new Request();
        if (_accountId != null) {
            request.setAccountId(_accountId);
        }
        return request;
    }

}
