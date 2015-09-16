package com.coinbase.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
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
import com.coinbase.api.entity.OAuthCodeRequest;
import com.coinbase.api.entity.OAuthCodeResponse;
import com.coinbase.api.entity.OAuthTokensRequest;
import com.coinbase.api.entity.OAuthTokensResponse;
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
import com.coinbase.api.entity.RevokeTokenRequest;
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
import com.coinbase.api.exception.CredentialsIncorrectException;
import com.coinbase.api.exception.TwoFactorIncorrectException;
import com.coinbase.api.exception.TwoFactorRequiredException;
import com.coinbase.api.exception.UnauthorizedDeviceException;
import com.coinbase.api.exception.UnauthorizedException;
import com.coinbase.api.exception.UnspecifiedAccount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class CoinbaseImpl implements Coinbase {

    private static final ObjectMapper objectMapper = ObjectMapperProvider.createDefaultMapper();

    private URL    _baseApiUrl;
    private URL    _baseOAuthUrl;
    private String _accountId;
    private String _apiKey;
    private String _apiSecret;
    private String _accessToken;
    private SSLContext _sslContext;
    private SSLSocketFactory _socketFactory;
    private CallbackVerifier _callbackVerifier;

    CoinbaseImpl(CoinbaseBuilder builder) {

        _baseApiUrl = builder.base_api_url;
        _baseOAuthUrl = builder.base_oauth_url;
        _apiKey = builder.api_key;
        _apiSecret = builder.api_secret;
        _accessToken = builder.access_token;
        _accountId = builder.acct_id;
        _sslContext = builder.ssl_context;
        _callbackVerifier = builder.callback_verifier;

        try {
            if (_baseApiUrl == null) {
                _baseApiUrl = new URL("https://coinbase.com/api/v1/");
            }
            if (_baseOAuthUrl == null) {
                _baseOAuthUrl = new URL("https://coinbase.com/oauth/");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // Register BTC as a currency since Android won't let joda read from classpath resources
        try {
            CurrencyUnit.registerCurrency("BTC", -1, 8, new ArrayList<String>());
        } catch (IllegalArgumentException ex) {}

        if (_sslContext != null) {
            _socketFactory = _sslContext.getSocketFactory();
        } else {
            _socketFactory = CoinbaseSSL.getSSLContext().getSocketFactory();
        }

        if (_callbackVerifier == null) {
            _callbackVerifier = new CallbackVerifierImpl();
        }
    }

    @Override
    public User getUser() throws IOException, CoinbaseException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseApiUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(usersUrl, UsersResponse.class).getUsers().get(0).getUser();
    }

    @Override
    public AccountsResponse getAccounts() throws IOException, CoinbaseException {
        return getAccounts(1, 25, false);
    }

    @Override
    public AccountsResponse getAccounts(int page) throws IOException, CoinbaseException {
        return getAccounts(page, 25, false);
    }

    @Override
    public AccountsResponse getAccounts(int page, int limit) throws IOException, CoinbaseException {
        return getAccounts(page, limit, false);
    }

    @Override
    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive) throws IOException, CoinbaseException {
        URL accountsUrl;
        try {
            accountsUrl = new URL(
                    _baseApiUrl,
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

    @Override
    public Money getBalance() throws IOException, CoinbaseException {
        if (_accountId != null) {
            return getBalance(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    @Override
    public Money getBalance(String accountId) throws IOException, CoinbaseException {
        URL accountBalanceUrl;
        try {
            accountBalanceUrl = new URL(_baseApiUrl, "accounts/" + accountId + "/balance");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return deserialize(doHttp(accountBalanceUrl, "GET", null), Money.class);
    }

    @Override
    public void setPrimaryAccount(String accountId) throws CoinbaseException, IOException {
        URL setPrimaryUrl;
        try {
            setPrimaryUrl = new URL(_baseApiUrl, "accounts/" + accountId + "/primary");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        post(setPrimaryUrl, new Request(), Response.class);
    }

    @Override
    public void deleteAccount(String accountId) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseApiUrl, "accounts/" + accountId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        delete(accountUrl, Response.class);
    }

    @Override
    public void setPrimaryAccount() throws CoinbaseException, IOException {
        if (_accountId != null) {
            setPrimaryAccount(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    @Override
    public void deleteAccount() throws CoinbaseException, IOException {
        if (_accountId != null) {
            deleteAccount(_accountId);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    @Override
    public void updateAccount(Account account) throws CoinbaseException, IOException, UnspecifiedAccount {
        if (_accountId != null) {
            updateAccount(_accountId, account);
        } else {
            throw new UnspecifiedAccount();
        }
    }

    @Override
    public Account createAccount(Account account) throws CoinbaseException, IOException {
        URL accountsUrl;
        try {
            accountsUrl = new URL(_baseApiUrl, "accounts");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setAccount(account);

        return post(accountsUrl, request, AccountResponse.class).getAccount();
    }

    @Override
    public void updateAccount(String accountId, Account account) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseApiUrl, "accounts/" + accountId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }

        Request request = new Request();
        request.setAccount(account);
        put(accountUrl, request, Response.class);
    }

    @Override
    public Money getSpotPrice(CurrencyUnit currency) throws IOException, CoinbaseException {
        URL spotPriceUrl;
        try {
            spotPriceUrl = new URL(_baseApiUrl, "prices/spot_rate?currency=" + URLEncoder.encode(currency.getCurrencyCode(), "UTF-8"));
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(spotPriceUrl, "GET", null), Money.class);
    }

    @Override
    public Quote getBuyQuote(Money amount) throws IOException, CoinbaseException {
        return getBuyQuote(amount, null);
    }

    @Override
    public Quote getBuyQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException {
        String qtyParam;
        if(amount.getCurrencyUnit().getCode().equals("BTC")) {
            qtyParam = "qty";
        }
        else {
            qtyParam = "native_qty";
        }

        URL buyPriceUrl;
        try {
            buyPriceUrl = new URL(
                    _baseApiUrl,
                    "prices/buy?" + qtyParam +"=" + URLEncoder.encode(amount.getAmount().toPlainString(), "UTF-8") +
                            (_accountId != null ? "&account_id=" + _accountId : "") +
                            (paymentMethodId != null ? "&payment_method_id=" + paymentMethodId : "")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(buyPriceUrl, "GET", null), Quote.class);
    }

    @Override
    public Quote getSellQuote(Money amount) throws IOException, CoinbaseException {
        return getSellQuote(amount, null);
    }

    @Override
    public Quote getSellQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException {
        String qtyParam;
        if(amount.getCurrencyUnit().getCode().equals("BTC")) {
            qtyParam = "qty";
        }
        else {
            qtyParam = "native_qty";
        }

        URL sellPriceUrl;
        try {
            sellPriceUrl = new URL(
                    _baseApiUrl,
                    "prices/sell?" + qtyParam + "=" + URLEncoder.encode(amount.getAmount().toPlainString(), "UTF-8") +
                            (_accountId != null ? "&account_id=" + _accountId : "") +
                            (paymentMethodId != null ? "&payment_method_id=" + paymentMethodId : "")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(sellPriceUrl, "GET", null), Quote.class);
    }

    @Override
    public TransactionsResponse getTransactions(int page) throws IOException, CoinbaseException {
        URL transactionsUrl;
        try {
            transactionsUrl = new URL(
                    _baseApiUrl,
                    "transactions?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(transactionsUrl, TransactionsResponse.class);
    }

    @Override
    public TransactionsResponse getTransactions() throws IOException, CoinbaseException {
        return getTransactions(1);
    }

    @Override
    public Transaction getTransaction(String id) throws IOException, CoinbaseException {
        URL transactionUrl;
        try {
            transactionUrl = new URL(_baseApiUrl, "transactions/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return get(transactionUrl, TransactionResponse.class).getTransaction();
    }

    @Override
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL requestMoneyUrl;
        try {
            requestMoneyUrl = new URL(_baseApiUrl, "transactions/request_money");
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

    @Override
    public void resendRequest(String id) throws CoinbaseException, IOException {
        URL resendRequestUrl;
        try {
            resendRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/resend_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        put(resendRequestUrl, newAccountSpecificRequest(), Response.class);
    }

    @Override
    public void deleteRequest(String id) throws CoinbaseException, IOException {
        URL cancelRequestUrl;
        try {
            cancelRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/cancel_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        delete(cancelRequestUrl, Response.class);
    }

    @Override
    public Transaction completeRequest(String id) throws CoinbaseException, IOException {
        URL completeRequestUrl;
        try {
            completeRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/complete_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return put(completeRequestUrl, newAccountSpecificRequest(), TransactionResponse.class).getTransaction();
    }

    @Override
    public Transaction sendMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL sendMoneyUrl;
        try {
            sendMoneyUrl = new URL(_baseApiUrl, "transactions/send_money");
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

    @Override
    public TransfersResponse getTransfers(int page) throws IOException, CoinbaseException {
        URL transfersUrl;
        try {
            transfersUrl = new URL(
                    _baseApiUrl,
                    "transfers?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(transfersUrl, TransfersResponse.class);
    }

    @Override
    public TransfersResponse getTransfers() throws IOException, CoinbaseException {
        return getTransfers(1);
    }

    @Override
    public Transfer sell(Money amount) throws CoinbaseException, IOException {
        return sell(amount, null);
    }

    @Override
    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        return sell(amount, paymentMethodId, null);
    }

    @Override
    public Transfer sell(Money amount, String paymentMethodId, Boolean commit) throws CoinbaseException, IOException {
        URL sellsUrl;
        try {
            sellsUrl = new URL(_baseApiUrl, "sells");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setQty(amount.getAmount().doubleValue());
        request.setPaymentMethodId(paymentMethodId);
        request.setCurrency(amount.getCurrencyUnit().getCurrencyCode());
        request.setCommit(commit);

        return post(sellsUrl, request, TransferResponse.class).getTransfer();
    }

    @Override
    public Transfer buy(Money amount) throws CoinbaseException, IOException {
        return buy(amount, null);
    }

    @Override
    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        return buy(amount, paymentMethodId, null);
    }

    @Override
    public Transfer buy(Money amount, String paymentMethodId, Boolean commit) throws CoinbaseException, IOException {
        URL buysUrl;
        try {
            buysUrl = new URL(_baseApiUrl, "buys");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setQty(amount.getAmount().doubleValue());
        request.setPaymentMethodId(paymentMethodId);
        request.setCurrency(amount.getCurrencyUnit().getCurrencyCode());
        request.setCommit(commit);

        return post(buysUrl, request, TransferResponse.class).getTransfer();
    }

    @Override
    public Transfer commitTransfer(String transactionId) throws CoinbaseException, IOException {
        URL commitUrl;
        try {
            commitUrl = new URL(_baseApiUrl, "transfers/" + transactionId + "/commit");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }

        return post(commitUrl, null, TransferResponse.class).getTransfer();
    }

    @Override
    public Order getOrder(String idOrCustom) throws IOException, CoinbaseException {
        URL orderUrl;
        try {
            orderUrl = new URL(_baseApiUrl, "orders/" + idOrCustom);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid order id/custom");
        }
        return get(orderUrl, OrderResponse.class).getOrder();
    }

    @Override
    public OrdersResponse getOrders(int page) throws IOException, CoinbaseException {
        URL ordersUrl;
        try {
            ordersUrl = new URL(
                    _baseApiUrl,
                    "orders?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid  account id");
        }
        return get(ordersUrl, OrdersResponse.class);
    }

    @Override
    public OrdersResponse getOrders() throws IOException, CoinbaseException {
        return getOrders(1);
    }

    @Override
    public AddressesResponse getAddresses(int page) throws IOException, CoinbaseException {
        URL addressesUrl;
        try {
            addressesUrl = new URL(
                    _baseApiUrl,
                    "addresses?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(addressesUrl, AddressesResponse.class);
    }

    @Override
    public AddressesResponse getAddresses() throws IOException, CoinbaseException {
        return getAddresses(1);
    }

    @Override
    public ContactsResponse getContacts(int page) throws IOException, CoinbaseException {
        URL contactsUrl;
        try {
            contactsUrl = new URL(
                    _baseApiUrl,
                    "contacts?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(contactsUrl, ContactsResponse.class);
    }

    @Override
    public ContactsResponse getContacts() throws IOException, CoinbaseException {
        return getContacts(1);
    }

    @Override
    public ContactsResponse getContacts(String query, int page) throws IOException, CoinbaseException {
        URL contactsUrl;
        try {
            contactsUrl = new URL(
                    _baseApiUrl,
                    "contacts?page=" + page +
                            "&query=" + URLEncoder.encode(query, "UTF-8")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(contactsUrl, ContactsResponse.class);
    }

    @Override
    public ContactsResponse getContacts(String query) throws IOException, CoinbaseException {
        return getContacts(query, 1);
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates() throws IOException, CoinbaseException {
        URL ratesUrl;
        try {
            ratesUrl = new URL(_baseApiUrl, "currencies/exchange_rates");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(ratesUrl, "GET", null), new TypeReference<HashMap<String, BigDecimal>>() {});
    }

    @Override
    public List<CurrencyUnit> getSupportedCurrencies() throws IOException, CoinbaseException {
        URL currenciesUrl;
        try {
            currenciesUrl = new URL(_baseApiUrl, "currencies");
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

    @Override
    public List<HistoricalPrice> getHistoricalPrices(int page) throws CoinbaseException, IOException {
        URL historicalPricesUrl;
        try {
            historicalPricesUrl = new URL(
                    _baseApiUrl,
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

    @Override
    public List<HistoricalPrice> getHistoricalPrices() throws CoinbaseException, IOException {
        return getHistoricalPrices(1);
    }

    @Override
    public Button createButton(Button button) throws CoinbaseException, IOException {
        URL buttonsUrl;
        try {
            buttonsUrl = new URL(_baseApiUrl, "buttons");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setButton(button);

        return post(buttonsUrl, request, ButtonResponse.class).getButton();
    }

    @Override
    public Order createOrder(Button button) throws CoinbaseException, IOException {
        URL ordersUrl;
        try {
            ordersUrl = new URL(_baseApiUrl, "orders");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setButton(button);

        return post(ordersUrl, request, OrderResponse.class).getOrder();
    }

    @Override
    public Order createOrderForButton(String buttonCode) throws CoinbaseException, IOException {
        URL createOrderForButtonUrl;
        try {
            createOrderForButtonUrl = new URL(_baseApiUrl, "buttons/" + buttonCode + "/create_order");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid button code");
        }

        return post(createOrderForButtonUrl, new Request(), OrderResponse.class).getOrder();
    }

    @Override
    public PaymentMethodsResponse getPaymentMethods() throws IOException, CoinbaseException {
        URL paymentMethodsUrl;
        try {
            paymentMethodsUrl = new URL(_baseApiUrl, "payment_methods");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(paymentMethodsUrl, PaymentMethodsResponse.class);
    }

    @Override
    public RecurringPaymentsResponse getSubscribers(int page) throws IOException, CoinbaseException {
        URL subscribersUrl;
        try {
            subscribersUrl = new URL(
                    _baseApiUrl,
                    "subscribers?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return get(subscribersUrl, RecurringPaymentsResponse.class);
    }

    @Override
    public RecurringPaymentsResponse getSubscribers() throws IOException, CoinbaseException {
        return getSubscribers(1);
    }

    @Override
    public RecurringPaymentsResponse getRecurringPayments(int page) throws IOException, CoinbaseException {
        URL recurringPaymentsUrl;
        try {
            recurringPaymentsUrl = new URL(
                    _baseApiUrl,
                    "recurring_payments?page=" + page
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(recurringPaymentsUrl, RecurringPaymentsResponse.class);
    }

    @Override
    public RecurringPaymentsResponse getRecurringPayments() throws IOException, CoinbaseException {
        return getRecurringPayments(1);
    }

    @Override
    public RecurringPayment getRecurringPayment(String id) throws CoinbaseException, IOException {
        URL recurringPaymentUrl;
        try {
            recurringPaymentUrl = new URL(_baseApiUrl, "recurring_payments/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid payment id");
        }

        return get(recurringPaymentUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    @Override
    public RecurringPayment getSubscriber(String id) throws CoinbaseException, IOException {
        URL subscriberUrl;
        try {
            subscriberUrl = new URL(_baseApiUrl, "subscribers/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid subscriber id");
        }

        return get(subscriberUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    @Override
    public AddressResponse generateReceiveAddress(Address addressParams) throws CoinbaseException, IOException {
        URL generateAddressUrl;
        try {
            generateAddressUrl = new URL(_baseApiUrl, "account/generate_receive_address");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setAddress(addressParams);

        return post(generateAddressUrl, request, AddressResponse.class);
    }

    @Override
    public AddressResponse generateReceiveAddress() throws CoinbaseException, IOException {
        return generateReceiveAddress(null);
    }

    @Override
    public User createUser(User userParams) throws CoinbaseException, IOException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseApiUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setUser(userParams);

        return post(usersUrl, request, UserResponse.class).getUser();
    }

    @Override
    public User createUser(User userParams, String clientId, String scope) throws CoinbaseException, IOException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseApiUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setUser(userParams);
        request.setScopes(scope);
        request.setClientId(clientId);

        return post(usersUrl, request, UserResponse.class).getUser();
    }

    @Override
    public UserResponse createUserWithOAuth(User userParams, String clientId, String scope) throws CoinbaseException, IOException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseApiUrl, "users");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setUser(userParams);
        request.setScopes(scope);
        request.setClientId(clientId);

        return post(usersUrl, request, UserResponse.class);
    }

    @Override
    public User updateUser(String userId, User userParams) throws CoinbaseException, IOException {
        URL userUrl;
        try {
            userUrl = new URL(_baseApiUrl, "users/" + userId);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid user id");
        }

        Request request = new Request();
        request.setUser(userParams);

        return put(userUrl, request, UserResponse.class).getUser();
    }

    @Override
    public Token createToken() throws CoinbaseException, IOException {
        URL tokensUrl;
        try {
            tokensUrl = new URL(_baseApiUrl, "tokens");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return post(tokensUrl, new Request(), TokenResponse.class).getToken();
    }

    @Override
    public void redeemToken(String tokenId) throws CoinbaseException, IOException {
        URL redeemTokenUrl;
        try {
            redeemTokenUrl = new URL(_baseApiUrl, "tokens/redeem");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setTokenId(tokenId);

        post(redeemTokenUrl, request, Response.class);
    }

    @Override
    public Application createApplication(Application applicationParams) throws CoinbaseException, IOException {
        URL applicationsUrl;
        try {
            applicationsUrl = new URL(_baseApiUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = new Request();
        request.setApplication(applicationParams);

        return post(applicationsUrl, request, ApplicationResponse.class).getApplication();
    }

    @Override
    public ApplicationsResponse getApplications() throws IOException, CoinbaseException {
        URL applicationsUrl;
        try {
            applicationsUrl = new URL(_baseApiUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(applicationsUrl, ApplicationsResponse.class);
    }

    @Override
    public Application getApplication(String id) throws IOException, CoinbaseException {
        URL applicationUrl;
        try {
            applicationUrl = new URL(_baseApiUrl, "oauth/applications/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid application id");
        }
        return get(applicationUrl, ApplicationResponse.class).getApplication();
    }

    @Override
    public Report createReport(Report reportParams) throws CoinbaseException, IOException {
        URL reportsUrl;
        try {
            reportsUrl = new URL(_baseApiUrl, "reports");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Request request = newAccountSpecificRequest();
        request.setReport(reportParams);

        return post(reportsUrl, request, ReportResponse.class).getReport();
    }

    @Override
    public Report getReport(String reportId) throws IOException, CoinbaseException {
        URL reportUrl;
        try {
            reportUrl = new URL(
                    _baseApiUrl,
                    "reports/" + reportId +
                            (_accountId != null ? "?account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid report id");
        }
        return get(reportUrl, ReportResponse.class).getReport();
    }

    @Override
    public ReportsResponse getReports(int page) throws IOException, CoinbaseException {
        URL reportsUrl;
        try {
            reportsUrl = new URL(
                    _baseApiUrl,
                    "reports?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(reportsUrl, ReportsResponse.class);
    }

    @Override
    public ReportsResponse getReports() throws IOException, CoinbaseException {
        return getReports(1);
    }

    @Override
    public AccountChangesResponse getAccountChanges(int page) throws IOException, CoinbaseException {
        URL accountChangesUrl;
        try {
            accountChangesUrl = new URL(
                    _baseApiUrl,
                    "account_changes?page=" + page +
                            (_accountId != null ? "&account_id=" + _accountId : "")
            );
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return get(accountChangesUrl, AccountChangesResponse.class);
    }

    @Override
    public AccountChangesResponse getAccountChanges() throws IOException, CoinbaseException {
        return getAccountChanges(1);
    }

    @Override
    public String getAuthCode(OAuthCodeRequest request)
            throws CoinbaseException, IOException {

        URL credentialsAuthorizationUrl;
        try {
            credentialsAuthorizationUrl = new URL(_baseOAuthUrl, "authorize/with_credentials");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return post(credentialsAuthorizationUrl, request, OAuthCodeResponse.class).getCode();
    }

    @Override
    public OAuthTokensResponse getTokens(String clientId, String clientSecret, String authCode, String redirectUri)
            throws UnauthorizedDeviceException, CoinbaseException, IOException {

        URL tokenUrl;
        try {
            tokenUrl = new URL(_baseOAuthUrl, "token");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        OAuthTokensRequest request = new OAuthTokensRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setGrantType(OAuthTokensRequest.GrantType.AUTHORIZATION_CODE);
        request.setCode(authCode);
        request.setRedirectUri(redirectUri != null? redirectUri : "2_legged");

        return post(tokenUrl, request, OAuthTokensResponse.class);
    }

    @Override
    public void revokeToken() throws CoinbaseException, IOException {

        if (_accessToken == null) {
            throw new CoinbaseException(
                "This client must have been initialized with an access token in order to call revokeToken()"
            );
        }

        URL revokeTokenUrl;
        try {
            revokeTokenUrl = new URL(_baseOAuthUrl, "revoke");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        RevokeTokenRequest request = new RevokeTokenRequest();
        request.setToken(_accessToken);

        post(revokeTokenUrl, request, Response.class);
        _accessToken = null;
    }

    @Override
    public OAuthTokensResponse refreshTokens(String clientId, String clientSecret, String refreshToken)
            throws CoinbaseException, IOException {

        URL tokenUrl;
        try {
            tokenUrl = new URL(_baseOAuthUrl, "token");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        OAuthTokensRequest request = new OAuthTokensRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setGrantType(OAuthTokensRequest.GrantType.REFRESH_TOKEN);
        request.setRefreshToken(refreshToken);

        return post(tokenUrl, request, OAuthTokensResponse.class);
    }

    @Override
    public void sendSMS(String clientId, String clientSecret, String email, String password) throws CoinbaseException, IOException {

        URL smsUrl;
        try {
            smsUrl = new URL(_baseOAuthUrl, "authorize/with_credentials/sms_token");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        OAuthCodeRequest request = new OAuthCodeRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setUsername(email);
        request.setPassword(password);

        post(smsUrl, request, Response.class);
    }

    @Override
    public URI getAuthorizationUri(OAuthCodeRequest params) throws CoinbaseException {
        URL authorizeURL;
        URIBuilder uriBuilder;

        try {
            authorizeURL = new URL(_baseOAuthUrl, "authorize");
            uriBuilder = new URIBuilder(authorizeURL.toURI());
        } catch (URISyntaxException ex) {
            throw new AssertionError(ex);
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        uriBuilder.addParameter("response_type", "code");

        if (params.getClientId() != null) {
            uriBuilder.addParameter("client_id", params.getClientId());
        } else {
            throw new CoinbaseException("client_id is required");
        }

        if (params.getRedirectUri() != null) {
            uriBuilder.addParameter("redirect_uri", params.getRedirectUri());
        } else {
            throw new CoinbaseException("redirect_uri is required");
        }

        if (params.getScope() != null) {
            uriBuilder.addParameter("scope", params.getScope());
        } else {
            throw new CoinbaseException("scope is required");
        }

        if (params.getMeta() != null) {
            OAuthCodeRequest.Meta meta = params.getMeta();

            if (meta.getName() != null) {
                uriBuilder.addParameter("meta[name]", meta.getName());
            }
            if (meta.getSendLimitAmount() != null) {
                Money sendLimit = meta.getSendLimitAmount();
                uriBuilder.addParameter("meta[send_limit_amount]", sendLimit.getAmount().toPlainString());
                uriBuilder.addParameter("meta[send_limit_currency]", sendLimit.getCurrencyUnit().getCurrencyCode());
                if (meta.getSendLimitPeriod() != null) {
                    uriBuilder.addParameter("meta[send_limit_period]", meta.getSendLimitPeriod().toString());
                }
            }
        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean verifyCallback(String body, String signature) {
        return _callbackVerifier.verifyCallback(body, signature);
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
        URLConnection urlConnection = url.openConnection();
        if (!(urlConnection instanceof HttpsURLConnection)) {
            throw new RuntimeException(
                    "Custom Base URL must return javax.net.ssl.HttpsURLConnection on openConnection.");
        }
        HttpsURLConnection conn = (HttpsURLConnection) urlConnection;
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
            String errorBody = null;
            if (es != null) {
                errorBody = IOUtils.toString(es, "UTF-8");
                if (errorBody != null && conn.getContentType().toLowerCase().contains("json")) {
                    Response coinbaseResponse;
                    try {
                        coinbaseResponse = deserialize(errorBody, Response.class);
                    } catch (Exception ex) {
                        throw new CoinbaseException(errorBody);
                    }
                    handleErrors(coinbaseResponse);
                }
            }
            if (HttpsURLConnection.HTTP_UNAUTHORIZED == conn.getResponseCode()) {
                throw new UnauthorizedException(errorBody);
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

    private static <T extends Response> T handleErrors(T response) throws CoinbaseException {
        String errors = response.getErrors();
        if (errors != null) {
            if (errors.contains("device_confirmation_required")) {
                throw new UnauthorizedDeviceException();
            } else if (errors.contains("2fa_required")) {
                throw new TwoFactorRequiredException();
            } else if (errors.contains("2fa_incorrect")) {
                throw new TwoFactorIncorrectException();
            } else if (errors.contains("incorrect_credentials")) {
                throw new CredentialsIncorrectException();
            }
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
