package com.coinbase;

import com.coinbase.v1.entity.Account;
import com.coinbase.v1.entity.AccountChangesResponse;
import com.coinbase.v1.entity.AccountResponse;
import com.coinbase.v1.entity.AccountsResponse;
import com.coinbase.v1.entity.Address;
import com.coinbase.v1.entity.AddressResponse;
import com.coinbase.v1.entity.AddressesResponse;
import com.coinbase.v1.entity.Application;
import com.coinbase.v1.entity.ApplicationResponse;
import com.coinbase.v1.entity.ApplicationsResponse;
import com.coinbase.v1.entity.Button;
import com.coinbase.v1.entity.ButtonResponse;
import com.coinbase.v1.entity.ContactsResponse;
import com.coinbase.v1.entity.HistoricalPrice;
import com.coinbase.v1.entity.OAuthCodeRequest;
import com.coinbase.v1.entity.OAuthCodeResponse;
import com.coinbase.v1.entity.OAuthTokensRequest;
import com.coinbase.v1.entity.OAuthTokensResponse;
import com.coinbase.v1.entity.Order;
import com.coinbase.v1.entity.OrderResponse;
import com.coinbase.v1.entity.OrdersResponse;
import com.coinbase.v1.entity.PaymentMethodResponse;
import com.coinbase.v1.entity.PaymentMethodsResponse;
import com.coinbase.v1.entity.Quote;
import com.coinbase.v1.entity.RecurringPayment;
import com.coinbase.v1.entity.RecurringPaymentResponse;
import com.coinbase.v1.entity.RecurringPaymentsResponse;
import com.coinbase.v1.entity.Report;
import com.coinbase.v1.entity.ReportResponse;
import com.coinbase.v1.entity.ReportsResponse;
import com.coinbase.v1.entity.Request;
import com.coinbase.v1.entity.Response;
import com.coinbase.v1.entity.ResponseV1;
import com.coinbase.v1.entity.ResponseV2;
import com.coinbase.v1.entity.RevokeTokenRequest;
import com.coinbase.v1.entity.Token;
import com.coinbase.v1.entity.TokenResponse;
import com.coinbase.v1.entity.Transaction;
import com.coinbase.v1.entity.TransactionResponse;
import com.coinbase.v1.entity.TransactionsResponse;
import com.coinbase.v1.entity.Transfer;
import com.coinbase.v1.entity.TransferResponse;
import com.coinbase.v1.entity.TransfersResponse;
import com.coinbase.v1.entity.User;
import com.coinbase.v1.entity.UserResponse;
import com.coinbase.v1.entity.UsersResponse;
import com.coinbase.v1.exception.CoinbaseException;
import com.coinbase.v1.exception.CredentialsIncorrectException;
import com.coinbase.v1.exception.TwoFactorIncorrectException;
import com.coinbase.v1.exception.TwoFactorRequiredException;
import com.coinbase.v1.exception.UnauthorizedDeviceException;
import com.coinbase.v1.exception.UnauthorizedException;
import com.coinbase.v1.exception.UnspecifiedAccount;
import com.coinbase.v2.models.OAuth;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.spotPrice.SpotPrice;
import com.coinbase.v2.models.transactions.Transactions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.money.Money;
import org.joda.time.DateTime;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import au.com.bytecode.opencsv.CSVReader;
import okio.Buffer;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Coinbase {

    private static final ObjectMapper objectMapper = ObjectMapperProvider.createDefaultMapper();

    private URL _baseApiUrl;
    private URL _baseOAuthUrl;
    private URL _baseV2ApiUrl;
    private String _accountId;
    private String _apiKey;
    private String _apiSecret;
    private String _accessToken;
    private SSLContext _sslContext;
    private SSLSocketFactory _socketFactory;
    private CallbackVerifier _callbackVerifier;
    private static Coinbase _instance = null;

    private Coinbase() {
        try {
            _baseApiUrl = new URL("https://coinbase.com/api/v1/");
            _baseOAuthUrl = new URL("https://www.coinbase.com/oauth/");
            _baseV2ApiUrl = new URL("https://api.coinbase.com/v2/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        _sslContext = CoinbaseSSL.getSSLContext();
        _socketFactory = _sslContext.getSocketFactory();
        _callbackVerifier = new CallbackVerifierImpl();
    }

    public static void init(String apiKey, String apiSecret) {
        getInstance()._apiKey = apiKey;
        getInstance()._apiSecret = apiSecret;
    }

    /**
     * Retrieve the coinbase singleton object.
     *
     * @return Coinbase object
     *
     */
    public static Coinbase getInstance() {
        if (_instance == null) {
            _instance = new Coinbase();
        }

        return _instance;
    }

    Coinbase(CoinbaseBuilder builder) {

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
                _baseOAuthUrl = new URL("https://www.coinbase.com/oauth/");
            }
            if (_baseV2ApiUrl == null) {
                _baseV2ApiUrl = new URL("https://api.coinbase.com/v2/");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // Register BTC as a currency since Android won't let joda read from classpath resources
        try {
            CurrencyUnit.registerCurrency("BTC", -1, 8, new ArrayList<String>());
        } catch (IllegalArgumentException ex) {
        }

        if (_sslContext != null) {
            _socketFactory = _sslContext.getSocketFactory();
        } else {
            _sslContext = CoinbaseSSL.getSSLContext();
            _socketFactory = _sslContext.getSocketFactory();
        }

        if (_callbackVerifier == null) {
            _callbackVerifier = new CallbackVerifierImpl();
        }
    }

    
    public User getUser() throws IOException, CoinbaseException {
        URL usersUrl;
        try {
            usersUrl = new URL(_baseApiUrl, "users");
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
            accountBalanceUrl = new URL(_baseApiUrl, "accounts/" + accountId + "/balance");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return deserialize(doHttp(accountBalanceUrl, "GET", null), Money.class);
    }

    
    public void setPrimaryAccount(String accountId) throws CoinbaseException, IOException {
        URL setPrimaryUrl;
        try {
            setPrimaryUrl = new URL(_baseApiUrl, "accounts/" + accountId + "/primary");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        post(setPrimaryUrl, new Request(), Response.class);
    }

    
    public void deleteAccount(String accountId) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseApiUrl, "accounts/" + accountId);
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
            accountsUrl = new URL(_baseApiUrl, "accounts");
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
            accountUrl = new URL(_baseApiUrl, "accounts/" + accountId);
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
            spotPriceUrl = new URL(_baseApiUrl, "prices/spot_rate?currency=" + URLEncoder.encode(currency.getCurrencyCode(), "UTF-8"));
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(spotPriceUrl, "GET", null), Money.class);
    }

    
    public Quote getBuyQuote(Money amount) throws IOException, CoinbaseException {
        return getBuyQuote(amount, null);
    }

    
    public Quote getBuyQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException {
        String qtyParam;
        if (amount.getCurrencyUnit().getCode().equals("BTC")) {
            qtyParam = "qty";
        } else {
            qtyParam = "native_qty";
        }

        URL buyPriceUrl;
        try {
            buyPriceUrl = new URL(
                    _baseApiUrl,
                    "prices/buy?" + qtyParam + "=" + URLEncoder.encode(amount.getAmount().toPlainString(), "UTF-8") +
                            (_accountId != null ? "&account_id=" + _accountId : "") +
                            (paymentMethodId != null ? "&payment_method_id=" + paymentMethodId : "")
            );
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(buyPriceUrl, "GET", null), Quote.class);
    }

    
    public Quote getSellQuote(Money amount) throws IOException, CoinbaseException {
        return getSellQuote(amount, null);
    }

    
    public Quote getSellQuote(Money amount, String paymentMethodId) throws IOException, CoinbaseException {
        String qtyParam;
        if (amount.getCurrencyUnit().getCode().equals("BTC")) {
            qtyParam = "qty";
        } else {
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

    
    public TransactionsResponse getTransactions() throws IOException, CoinbaseException {
        return getTransactions(1);
    }

    
    public Transaction getTransaction(String id) throws IOException, CoinbaseException {
        URL transactionUrl;
        try {
            transactionUrl = new URL(_baseApiUrl, "transactions/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return get(transactionUrl, TransactionResponse.class).getTransaction();
    }

    
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

    
    public void resendRequest(String id) throws CoinbaseException, IOException {
        URL resendRequestUrl;
        try {
            resendRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/resend_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        put(resendRequestUrl, newAccountSpecificRequest(), Response.class);
    }

    
    public void deleteRequest(String id) throws CoinbaseException, IOException {
        URL cancelRequestUrl;
        try {
            cancelRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/cancel_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        delete(cancelRequestUrl, Response.class);
    }

    
    public Transaction completeRequest(String id) throws CoinbaseException, IOException {
        URL completeRequestUrl;
        try {
            completeRequestUrl = new URL(_baseApiUrl, "transactions/" + id + "/complete_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return put(completeRequestUrl, newAccountSpecificRequest(), TransactionResponse.class).getTransaction();
    }

    
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

    
    public TransfersResponse getTransfers() throws IOException, CoinbaseException {
        return getTransfers(1);
    }

    
    public Transaction transferMoneyBetweenAccounts(String amount, String toAccountId) throws CoinbaseException, IOException {
        URL transferMoneyURL;
        try {
            transferMoneyURL = new URL(_baseApiUrl, "transactions/transfer_money");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        Transaction transaction = new Transaction();
        transaction.setTo(toAccountId);
        transaction.setTransferBitcoinAmountString(amount);

        Request request = newAccountSpecificRequest();
        request.setTransaction(transaction);

        return post(transferMoneyURL, request, TransactionResponse.class).getTransaction();
    }

    
    public Transfer sell(Money amount) throws CoinbaseException, IOException {
        return sell(amount, null);
    }

    
    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        return sell(amount, paymentMethodId, null);
    }

    
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

    
    public Transfer buy(Money amount) throws CoinbaseException, IOException {
        return buy(amount, null);
    }

    
    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException, IOException {
        return buy(amount, paymentMethodId, null);
    }

    
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

    
    public Transfer commitTransfer(String accountId, String transactionId) throws CoinbaseException, IOException {
        URL commitUrl;
        try {
            commitUrl = new URL(_baseApiUrl, "transfers/" + transactionId + "/commit");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }

        Request request = new Request();
        request.setAccountId(accountId);

        return post(commitUrl, request, TransferResponse.class).getTransfer();
    }

    
    public Order getOrder(String idOrCustom) throws IOException, CoinbaseException {
        URL orderUrl;
        try {
            orderUrl = new URL(_baseApiUrl, "orders/" + idOrCustom);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid order id/custom");
        }
        return get(orderUrl, OrderResponse.class).getOrder();
    }

    
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

    
    public OrdersResponse getOrders() throws IOException, CoinbaseException {
        return getOrders(1);
    }

    
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

    
    public AddressesResponse getAddresses() throws IOException, CoinbaseException {
        return getAddresses(1);
    }

    
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

    
    public ContactsResponse getContacts() throws IOException, CoinbaseException {
        return getContacts(1);
    }

    
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

    
    public ContactsResponse getContacts(String query) throws IOException, CoinbaseException {
        return getContacts(query, 1);
    }

    
    public Map<String, BigDecimal> getExchangeRates() throws IOException, CoinbaseException {
        URL ratesUrl;
        try {
            ratesUrl = new URL(_baseApiUrl, "currencies/exchange_rates");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(ratesUrl, "GET", null), new TypeReference<HashMap<String, BigDecimal>>() {
        });
    }

    
    public List<CurrencyUnit> getSupportedCurrencies() throws IOException, CoinbaseException {
        URL currenciesUrl;
        try {
            currenciesUrl = new URL(_baseApiUrl, "currencies");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        List<List<String>> rawResponse =
                deserialize(doHttp(currenciesUrl, "GET", null), new TypeReference<List<List<String>>>() {
                });

        List<CurrencyUnit> result = new ArrayList<CurrencyUnit>();
        for (List<String> currency : rawResponse) {
            try {
                result.add(CurrencyUnit.getInstance(currency.get(1)));
            } catch (IllegalCurrencyException ex) {
            }
        }

        return result;
    }

    
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
            } catch (IOException e) {
            }
        }

        return result;
    }

    
    public List<HistoricalPrice> getHistoricalPrices() throws CoinbaseException, IOException {
        return getHistoricalPrices(1);
    }

    
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

    
    public Order createOrderForButton(String buttonCode) throws CoinbaseException, IOException {
        URL createOrderForButtonUrl;
        try {
            createOrderForButtonUrl = new URL(_baseApiUrl, "buttons/" + buttonCode + "/create_order");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid button code");
        }

        return post(createOrderForButtonUrl, new Request(), OrderResponse.class).getOrder();
    }

    
    public PaymentMethodsResponse getPaymentMethods() throws IOException, CoinbaseException {
        URL paymentMethodsUrl;
        try {
            paymentMethodsUrl = new URL(_baseV2ApiUrl, "payment-methods");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        HashMap<String, String> headers = getV2VersionHeaders();

        return get(paymentMethodsUrl, headers, PaymentMethodsResponse.class);
    }

    
    public PaymentMethodResponse getPaymentMethod(String id) throws CoinbaseException, IOException {
        URL paymentMethodURL;
        try {
            paymentMethodURL = new URL(_baseV2ApiUrl, "payment-methods/" + id);
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        HashMap<String, String> headers = getV2VersionHeaders();

        return get(paymentMethodURL, headers, PaymentMethodResponse.class);
    }

    
    public void deletePaymentMethod(String id) throws CoinbaseException, IOException {
        URL deleteURL;

        try {
            deleteURL = new URL(_baseV2ApiUrl, "payment-methods/" + id);
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        HashMap<String, String> headers = getV2VersionHeaders();

        doHttp(deleteURL, "DELETE", null, headers);
    }

    
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

    
    public RecurringPaymentsResponse getSubscribers() throws IOException, CoinbaseException {
        return getSubscribers(1);
    }

    
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

    
    public RecurringPaymentsResponse getRecurringPayments() throws IOException, CoinbaseException {
        return getRecurringPayments(1);
    }

    
    public RecurringPayment getRecurringPayment(String id) throws CoinbaseException, IOException {
        URL recurringPaymentUrl;
        try {
            recurringPaymentUrl = new URL(_baseApiUrl, "recurring_payments/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid payment id");
        }

        return get(recurringPaymentUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    
    public RecurringPayment getSubscriber(String id) throws CoinbaseException, IOException {
        URL subscriberUrl;
        try {
            subscriberUrl = new URL(_baseApiUrl, "subscribers/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid subscriber id");
        }

        return get(subscriberUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }

    
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

    
    public AddressResponse generateReceiveAddress() throws CoinbaseException, IOException {
        return generateReceiveAddress(null);
    }

    
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

    
    public Token createToken() throws CoinbaseException, IOException {
        URL tokensUrl;
        try {
            tokensUrl = new URL(_baseApiUrl, "tokens");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return post(tokensUrl, new Request(), TokenResponse.class).getToken();
    }

    
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

    
    public ApplicationsResponse getApplications() throws IOException, CoinbaseException {
        URL applicationsUrl;
        try {
            applicationsUrl = new URL(_baseApiUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(applicationsUrl, ApplicationsResponse.class);
    }

    
    public Application getApplication(String id) throws IOException, CoinbaseException {
        URL applicationUrl;
        try {
            applicationUrl = new URL(_baseApiUrl, "oauth/applications/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid application id");
        }
        return get(applicationUrl, ApplicationResponse.class).getApplication();
    }

    
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

    
    public ReportsResponse getReports() throws IOException, CoinbaseException {
        return getReports(1);
    }

    
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

    
    public AccountChangesResponse getAccountChanges() throws IOException, CoinbaseException {
        return getAccountChanges(1);
    }

    
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
        request.setRedirectUri(redirectUri != null ? redirectUri : "2_legged");

        return post(tokenUrl, request, OAuthTokensResponse.class);
    }

    
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

    
    public boolean verifyCallback(String body, String signature) {
        return _callbackVerifier.verifyCallback(body, signature);
    }

    private void doHmacAuthentication(URL url, String body, HttpsURLConnection conn) throws IOException {
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
        return doHttp(url, method, requestBody, null);
    }

    private String doHttp(URL url, String method, Object requestBody, HashMap<String, String> headers) throws IOException, CoinbaseException {
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

        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
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
            String str = IOUtils.toString(is, "UTF-8");
            return str;
        } catch (IOException e) {
            if (HttpsURLConnection.HTTP_PAYMENT_REQUIRED == conn.getResponseCode()) {
                throw new TwoFactorRequiredException();
            }
            es = conn.getErrorStream();
            String errorBody = null;
            if (es != null) {
                errorBody = IOUtils.toString(es, "UTF-8");
                if (errorBody != null && conn.getContentType().toLowerCase().contains("json")) {
                    Response coinbaseResponse;
                    try {
                        coinbaseResponse = deserialize(errorBody, ResponseV1.class);
                    } catch (Exception ex) {
                        try {
                            coinbaseResponse = deserialize(errorBody, ResponseV2.class);
                        } catch (Exception ex1) {
                            throw new CoinbaseException(errorBody);
                        }
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
        return get(url, null, responseClass);
    }

    private <T extends Response> T get(URL url, HashMap<String, String> headers, Class<T> responseClass) throws IOException, CoinbaseException {
        return handleErrors(deserialize(doHttp(url, "GET", null, headers), responseClass));
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

    private HashMap<String, String> getV2VersionHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("CB-Version", "2015-03-20");
        return headers;
    }


    private Interceptor buildOAuthInterceptor() {
        return new Interceptor() {
            
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + _accessToken)
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    private Interceptor buildHmacAuthInterceptor() {
        return new Interceptor() {
            
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request request = chain.request();

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
                String method = request.method().toUpperCase();
                String path = request.url().getFile();
                String body = "";
                if (request.body() != null) {
                    final com.squareup.okhttp.Request requestCopy = request.newBuilder().build();
                    final Buffer buffer = new Buffer();
                    requestCopy.body().writeTo(buffer);
                    body = buffer.readUtf8();
                }

                String message = timestamp + method + path + body;
                Mac mac;
                try {
                    mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(_apiSecret.getBytes(), "HmacSHA256"));
                } catch (Throwable t) {
                    throw new IOException(t);
                }

                String signature = new String(Hex.encodeHex(mac.doFinal(message.getBytes())));

                com.squareup.okhttp.Request newRequest = request.newBuilder()
                        .addHeader("CB-ACCESS-KEY", _apiKey)
                        .addHeader("CB-ACCESS_SIGN", signature)
                        .addHeader("CB-ACCESS-TIMESTAMP", timestamp)
                        .build();

                return chain.proceed(newRequest);
            }
        };
    }

    protected Interceptor buildVersionInterceptor() {
        return new Interceptor() {
            
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("CB-VERSION", com.coinbase.ApiConstants.VERSION)
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    protected Interceptor languageInterceptor() {
        return new Interceptor() {
            
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    private com.coinbase.ApiInterface getApiService() {
        OkHttpClient client = new OkHttpClient();

        client.setSslSocketFactory(_sslContext.getSocketFactory());
        client.setConnectionSpecs(Collections.singletonList(ConnectionSpec.MODERN_TLS));

        // Disable SPDY, causes issues on some Android versions
        client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));

        if (_accessToken != null)
            client.interceptors().add(buildOAuthInterceptor());
        if (_apiKey != null && _apiSecret != null)
            client.interceptors().add(buildHmacAuthInterceptor());

        client.interceptors().add(buildVersionInterceptor());

        client.interceptors().add(languageInterceptor());

        String url = com.coinbase.ApiConstants.BASE_URL_PRODUCTION + "/" + com.coinbase.ApiConstants.SERVER_VERSION + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        com.coinbase.ApiInterface service = retrofit.create(com.coinbase.ApiInterface.class);

        return service;
    }

    /**
     * Obtain OAuth Authorization Code
     *
     * @param email The user's email
     * @param password the user's password
     */
    public Call getAuthCode(String email, String password, final Callback<OAuth> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.CLIENT_ID, _apiKey);
        params.put(ApiConstants.CLIENT_SECRET, _apiSecret);
        params.put(ApiConstants.EMAIL, email);
        params.put(ApiConstants.PASSWORD, password);

        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.getAuthCode(params);
        call.enqueue(new Callback<OAuth>() {

            public void onResponse(retrofit.Response<OAuth> response, Retrofit retrofit) {
                if (response.isSuccess())
                    _accessToken = response.body().getCode();

                if (callback != null)
                    callback.onResponse(response, retrofit);
            }


            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Retrieve the current user and their settings.
     *
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-user">Online Documentation</a>
     */
    public Call getUser(final Callback<com.coinbase.v2.models.user.User> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.getUser();
        call.enqueue(new Callback<com.coinbase.v2.models.user.User>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.user.User> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

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
    public Call updateUser(String name, String timeZone, String nativeCurrency, final Callback<com.coinbase.v2.models.user.User> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.NAME, name);
        params.put(ApiConstants.TIME_ZONE, timeZone);
        params.put(ApiConstants.NATIVE_CURRENCY, nativeCurrency);

        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.updateUser(params);
        call.enqueue(new Callback<com.coinbase.v2.models.user.User>() {

            public void onResponse(retrofit.Response<com.coinbase.v2.models.user.User> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }


            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Retrieve an account belonging to this user
     * @param accountId account ID for the account to retrieve
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-an-account">Online Documentation</a>
     */
    public Call getAccount(String accountId, final Callback<com.coinbase.v2.models.account.Account> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.getAccount(accountId);
        call.enqueue(new Callback<com.coinbase.v2.models.account.Account>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.account.Account> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Retrieve a list of accounts belonging to this user
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#list-accounts">Online Documentation</a>
     */
    public Call getAccounts(final Callback<Accounts> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();

        Call call = apiInterface.getAccounts();
        call.enqueue(new Callback<Accounts>() {
            
            public void onResponse(retrofit.Response<Accounts> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

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
                                final Callback<Transactions> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();

        Call call = apiInterface.getTransactions(accountId, expandOptions, options);
        call.enqueue(new Callback<Transactions>() {
            
            public void onResponse(retrofit.Response<Transactions> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Retrieve details of an individual transaction.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the transaction id or idem field value
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-transaction">Online Documentation</a>
     */
    public Call getTransaction(String accountId, String transactionId, final Callback<com.coinbase.v2.models.transactions.Transaction> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        List<String> expandOptions = Arrays.asList(com.coinbase.ApiConstants.FROM, com.coinbase.ApiConstants.TO, com.coinbase.ApiConstants.BUY, com.coinbase.ApiConstants.SELL);
        Call call = apiInterface.getTransaction(accountId, transactionId, expandOptions);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.transactions.Transaction> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Complete a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be completed
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#complete-request-money">Online Documentation</a>
     */
    public Call completeRequest(String accountId, String transactionId, final Callback<Void> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.completeRequest(accountId, transactionId);
        call.enqueue(new Callback<Void>() {
            
            public void onResponse(retrofit.Response<Void> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Resend emails for a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be resent
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#re-send-request-money">Online Documentation</a>
     */
    public Call resendRequest(String accountId, String transactionId, final Callback<Void> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.resendRequest(accountId, transactionId);
        call.enqueue(new Callback<Void>() {
            
            public void onResponse(retrofit.Response<Void> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Cancel a money request.
     * @param accountId account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be cancelled
     * @param callback callback interface
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#cancel-request-money">Online Documentation</a>
     */
    public Call cancelRequest(String accountId, String transactionId, final Callback<Void> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.cancelRequest(accountId, transactionId);
        call.enqueue(new Callback<Void>() {
            
            public void onResponse(retrofit.Response<Void> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Send money to an email address or bitcoin address
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#send-money">Online Documentation</a>
     */
    public Call sendMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.SEND);
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.sendMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.transactions.Transaction> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Request money from an email address or bitcoin address
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#request-money">Online Documentation</a>
     */
    public Call requestMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.REQUEST);
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.requestMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.transactions.Transaction> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Transfer bitcoin between two of a user’s accounts
     * @param accountId account ID that the transaction belongs to
     * @param params endpoint parameters
     * @return call object
     *
     * @see <a href="https://developers.coinbase.com/api/v2#transfer-money-between-accounts">Online Documentation</a>
     */
    public Call transferMoney(String accountId, HashMap<String, Object> params, final Callback<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, ApiConstants.TRANSFER);
        ApiInterface apiInterface = getApiService();
        Call call = apiInterface.transferMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {
            
            public void onResponse(retrofit.Response<com.coinbase.v2.models.transactions.Transaction> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }

    /**
     * Retrieve the current spot price of 1 BTC
     *
     * @param currency the currency in which to retrieve the price
     * @return the spot price of 1 BTC in the specified currency
     *
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getSpotPrice(String currency, final Callback<SpotPrice> callback) {
        com.coinbase.ApiInterface apiInterface = getApiService();
        Call call = apiInterface.getSpotPrice(currency);
        call.enqueue(new Callback<SpotPrice>() {
            
            public void onResponse(retrofit.Response<SpotPrice> response, Retrofit retrofit) {
                if (callback != null)
                    callback.onResponse(response, retrofit);
            }

            
            public void onFailure(Throwable t) {
                if (callback != null)
                    callback.onFailure(t);
            }
        });

        return call;
    }
}
