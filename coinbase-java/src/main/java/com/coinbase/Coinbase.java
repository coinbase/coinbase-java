package com.coinbase;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.coinbase.auth.AccessToken;
import com.coinbase.cache.OkHttpInMemoryLruCache;
import com.coinbase.coinbase_java.BuildConfig;
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
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.exchangeRates.ExchangeRates;
import com.coinbase.v2.models.paymentMethods.PaymentMethod;
import com.coinbase.v2.models.paymentMethods.PaymentMethods;
import com.coinbase.v2.models.price.Price;
import com.coinbase.v2.models.price.Prices;
import com.coinbase.v2.models.supportedCurrencies.SupportedCurrencies;
import com.coinbase.v2.models.transactions.Transactions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
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
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import au.com.bytecode.opencsv.CSVReader;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class Coinbase {

    protected static final ObjectMapper objectMapper = ObjectMapperProvider.createDefaultMapper();

    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 / 2; //500kb

    protected URL _baseV1ApiUrl;
    protected URL _baseOAuthUrl;
    protected URL _baseV2ApiUrl;
    protected URL _baseApiUrl;
    protected String _accountId;
    protected String _apiKey;
    protected String _apiSecret;
    protected String _accessToken;
    protected SSLContext _sslContext;
    protected SSLSocketFactory _socketFactory;
    protected CallbackVerifier _callbackVerifier;
    protected Scheduler _backgroundScheduler;
    protected Context _context;
    protected OkHttpInMemoryLruCache _cache = new OkHttpInMemoryLruCache(DEFAULT_CACHE_SIZE);


    protected final HashMap<String, Pair<ApiInterface, Retrofit>> mInitializedServices = new HashMap<>();
    protected final HashMap<String, Pair<ApiInterfaceRx, Retrofit>> mInitializedServicesRx = new HashMap<>();


    public Coinbase() {
        try {
            _baseApiUrl = new URL("https://api.coinbase.com/");
            _baseV1ApiUrl = new URL(ApiConstants.BASE_URL_PRODUCTION + "/v1/");
            _baseV2ApiUrl = new URL(ApiConstants.BASE_URL_PRODUCTION + "/v2/");
            _baseOAuthUrl = new URL("https://www.coinbase.com/oauth/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        _sslContext = CoinbaseSSL.getSSLContext();
        _socketFactory = _sslContext.getSocketFactory();
        _callbackVerifier = new CallbackVerifierImpl();
        _backgroundScheduler = Schedulers.io();
    }

    protected OkHttpClient.Builder generateClientBuilder(SSLContext sslContext) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (sslContext != null) {
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory());
        }

        // Disable SPDY, causes issues on some Android versions
        clientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));

        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);

        return clientBuilder;
    }

    public void setBaseUrl(String url) {
        try {
            SSLContext context = SSLContext.getDefault();
            setBaseUrl(url, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBaseUrl(String url, SSLContext sslContext) {
        try {
            _baseApiUrl = new URL(url + "/");
            _baseV1ApiUrl = new URL(url + "/v1/");
            _baseV2ApiUrl = new URL(url + "/v2/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Set this before using any of the methods, otherwise it will have no effect.
     *
     * @param backgroundScheduler
     */
    public void setBackgroundScheduler(Scheduler backgroundScheduler) {
        _backgroundScheduler = backgroundScheduler;
    }

    public void init(Context context, String apiKey, String apiSecret) {
        _apiKey = apiKey;
        _apiSecret = apiSecret;
        _context = context;
        _cache.evictAll();
    }


    public void init(Context context, String accessToken) {
        if (!TextUtils.equals(_accessToken, accessToken)) {
            mInitializedServices.clear();
            mInitializedServicesRx.clear();
        }
        _accessToken = accessToken;
        _context = context;
        _cache.evictAll();
    }

    Coinbase(CoinbaseBuilder builder) {

        _baseV1ApiUrl = builder.base_api_url;
        _baseOAuthUrl = builder.base_oauth_url;
        _apiKey = builder.api_key;
        _apiSecret = builder.api_secret;
        _accessToken = builder.access_token;
        _accountId = builder.acct_id;
        _sslContext = builder.ssl_context;
        _callbackVerifier = builder.callback_verifier;
        _backgroundScheduler = builder.scheduler;
        _cache = new OkHttpInMemoryLruCache(builder.cacheSize > 0 ? builder.cacheSize : DEFAULT_CACHE_SIZE);

        try {
            if (_baseV1ApiUrl == null) {
                _baseV1ApiUrl = new URL("https://coinbase.com/api/v1/");
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
            usersUrl = new URL(_baseV1ApiUrl, "users");
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
                    _baseV1ApiUrl,
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
            accountBalanceUrl = new URL(_baseV1ApiUrl, "accounts/" + accountId + "/balance");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        return deserialize(doHttp(accountBalanceUrl, "GET", null), Money.class);
    }


    public void setPrimaryAccount(String accountId) throws CoinbaseException, IOException {
        URL setPrimaryUrl;
        try {
            setPrimaryUrl = new URL(_baseV1ApiUrl, "accounts/" + accountId + "/primary");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid account id");
        }
        post(setPrimaryUrl, new Request(), Response.class);
    }


    public void deleteAccount(String accountId) throws CoinbaseException, IOException {
        URL accountUrl;
        try {
            accountUrl = new URL(_baseV1ApiUrl, "accounts/" + accountId);
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
            accountsUrl = new URL(_baseV1ApiUrl, "accounts");
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
            accountUrl = new URL(_baseV1ApiUrl, "accounts/" + accountId);
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
            spotPriceUrl = new URL(_baseV1ApiUrl, "prices/spot_rate?currency=" + URLEncoder.encode(currency.getCurrencyCode(), "UTF-8"));
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
            transactionUrl = new URL(_baseV1ApiUrl, "transactions/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return get(transactionUrl, TransactionResponse.class).getTransaction();
    }


    public Transaction requestMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL requestMoneyUrl;
        try {
            requestMoneyUrl = new URL(_baseV1ApiUrl, "transactions/request_money");
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
            resendRequestUrl = new URL(_baseV1ApiUrl, "transactions/" + id + "/resend_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        put(resendRequestUrl, newAccountSpecificRequest(), Response.class);
    }


    public void deleteRequest(String id) throws CoinbaseException, IOException {
        URL cancelRequestUrl;
        try {
            cancelRequestUrl = new URL(_baseV1ApiUrl, "transactions/" + id + "/cancel_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        delete(cancelRequestUrl, Response.class);
    }


    public Transaction completeRequest(String id) throws CoinbaseException, IOException {
        URL completeRequestUrl;
        try {
            completeRequestUrl = new URL(_baseV1ApiUrl, "transactions/" + id + "/complete_request");
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid transaction id");
        }
        return put(completeRequestUrl, newAccountSpecificRequest(), TransactionResponse.class).getTransaction();
    }


    public Transaction sendMoney(Transaction transaction) throws CoinbaseException, IOException {
        URL sendMoneyUrl;
        try {
            sendMoneyUrl = new URL(_baseV1ApiUrl, "transactions/send_money");
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
                    _baseV1ApiUrl,
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
            transferMoneyURL = new URL(_baseV1ApiUrl, "transactions/transfer_money");
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
            sellsUrl = new URL(_baseV1ApiUrl, "sells");
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
            buysUrl = new URL(_baseV1ApiUrl, "buys");
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
            commitUrl = new URL(_baseV1ApiUrl, "transfers/" + transactionId + "/commit");
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
            orderUrl = new URL(_baseV1ApiUrl, "orders/" + idOrCustom);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid order id/custom");
        }
        return get(orderUrl, OrderResponse.class).getOrder();
    }


    public OrdersResponse getOrders(int page) throws IOException, CoinbaseException {
        URL ordersUrl;
        try {
            ordersUrl = new URL(
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
            ratesUrl = new URL(_baseV1ApiUrl, "currencies/exchange_rates");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return deserialize(doHttp(ratesUrl, "GET", null), new TypeReference<HashMap<String, BigDecimal>>() {
        });
    }


    public List<CurrencyUnit> getSupportedCurrencies() throws IOException, CoinbaseException {
        URL currenciesUrl;
        try {
            currenciesUrl = new URL(_baseV1ApiUrl, "currencies");
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
                    _baseV1ApiUrl,
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
            buttonsUrl = new URL(_baseV1ApiUrl, "buttons");
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
            ordersUrl = new URL(_baseV1ApiUrl, "orders");
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
            createOrderForButtonUrl = new URL(_baseV1ApiUrl, "buttons/" + buttonCode + "/create_order");
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
            recurringPaymentUrl = new URL(_baseV1ApiUrl, "recurring_payments/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid payment id");
        }

        return get(recurringPaymentUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }


    public RecurringPayment getSubscriber(String id) throws CoinbaseException, IOException {
        URL subscriberUrl;
        try {
            subscriberUrl = new URL(_baseV1ApiUrl, "subscribers/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid subscriber id");
        }

        return get(subscriberUrl, RecurringPaymentResponse.class).getRecurringPayment();
    }


    public AddressResponse generateReceiveAddress(Address addressParams) throws CoinbaseException, IOException {
        URL generateAddressUrl;
        try {
            generateAddressUrl = new URL(_baseV1ApiUrl, "account/generate_receive_address");
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
            usersUrl = new URL(_baseV1ApiUrl, "users");
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
            usersUrl = new URL(_baseV1ApiUrl, "users");
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
            usersUrl = new URL(_baseV1ApiUrl, "users");
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
            userUrl = new URL(_baseV1ApiUrl, "users/" + userId);
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
            tokensUrl = new URL(_baseV1ApiUrl, "tokens");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        return post(tokensUrl, new Request(), TokenResponse.class).getToken();
    }


    public void redeemToken(String tokenId) throws CoinbaseException, IOException {
        URL redeemTokenUrl;
        try {
            redeemTokenUrl = new URL(_baseV1ApiUrl, "tokens/redeem");
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
            applicationsUrl = new URL(_baseV1ApiUrl, "oauth/applications");
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
            applicationsUrl = new URL(_baseV1ApiUrl, "oauth/applications");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
        return get(applicationsUrl, ApplicationsResponse.class);
    }


    public Application getApplication(String id) throws IOException, CoinbaseException {
        URL applicationUrl;
        try {
            applicationUrl = new URL(_baseV1ApiUrl, "oauth/applications/" + id);
        } catch (MalformedURLException ex) {
            throw new CoinbaseException("Invalid application id");
        }
        return get(applicationUrl, ApplicationResponse.class).getApplication();
    }


    public Report createReport(Report reportParams) throws CoinbaseException, IOException {
        URL reportsUrl;
        try {
            reportsUrl = new URL(_baseV1ApiUrl, "reports");
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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
                    _baseV1ApiUrl,
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


    public Uri getAuthorizationUri(OAuthCodeRequest params) throws CoinbaseException {
        URL authorizeURL;
        Uri builtUri;
        Uri.Builder uriBuilder;

        try {
            authorizeURL = new URL(_baseOAuthUrl, "authorize");
            builtUri = Uri.parse(authorizeURL.toURI().toString());
            uriBuilder = builtUri.buildUpon();
        } catch (URISyntaxException ex) {
            throw new AssertionError(ex);
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }

        uriBuilder.appendQueryParameter("response_type", "code");

        if (params.getClientId() != null) {
            uriBuilder.appendQueryParameter("client_id", params.getClientId());
        } else {
            throw new CoinbaseException("client_id is required");
        }

        if (params.getRedirectUri() != null) {
            uriBuilder.appendQueryParameter("redirect_uri", params.getRedirectUri());
        } else {
            throw new CoinbaseException("redirect_uri is required");
        }

        if (params.getScope() != null) {
            uriBuilder.appendQueryParameter("scope", params.getScope());
        } else {
            throw new CoinbaseException("scope is required");
        }

        if (params.getMeta() != null) {
            OAuthCodeRequest.Meta meta = params.getMeta();

            if (meta.getName() != null) {
                uriBuilder.appendQueryParameter("meta[name]", meta.getName());
            }
            if (meta.getSendLimitAmount() != null) {
                Money sendLimit = meta.getSendLimitAmount();
                uriBuilder.appendQueryParameter("meta[send_limit_amount]", sendLimit.getAmount().toPlainString());
                uriBuilder.appendQueryParameter("meta[send_limit_currency]", sendLimit.getCurrencyUnit().getCurrencyCode());
                if (meta.getSendLimitPeriod() != null) {
                    uriBuilder.appendQueryParameter("meta[send_limit_period]", meta.getSendLimitPeriod().toString());
                }
            }
        }

        return uriBuilder.build();
    }


    public boolean verifyCallback(String body, String signature) {
        return _callbackVerifier.verifyCallback(body, signature);
    }

    protected void doHmacAuthentication(URL url, String body, HttpsURLConnection conn) throws IOException {
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

    protected void doAccessTokenAuthentication(HttpsURLConnection conn) {
        conn.setRequestProperty("Authorization", "Bearer " + _accessToken);
    }

    protected String doHttp(URL url, String method, Object requestBody) throws IOException, CoinbaseException {
        return doHttp(url, method, requestBody, null);
    }

    protected String doHttp(URL url, String method, Object requestBody, HashMap<String, String> headers) throws IOException, CoinbaseException {
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

        if (_accessToken != null) {
            doAccessTokenAuthentication(conn);
        } else if (_apiKey != null && _apiSecret != null) {
            doHmacAuthentication(url, body, conn);
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


    protected static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    protected static <T> T deserialize(String json, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(json, typeReference);
    }

    protected <T extends Response> T get(URL url, Class<T> responseClass) throws IOException, CoinbaseException {
        return get(url, null, responseClass);
    }

    protected <T extends Response> T get(URL url, HashMap<String, String> headers, Class<T> responseClass) throws IOException, CoinbaseException {
        return handleErrors(deserialize(doHttp(url, "GET", null, headers), responseClass));
    }

    protected <T extends Response> T post(URL url, Object entity, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "POST", entity), responseClass));
    }

    protected <T extends Response> T put(URL url, Object entity, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "PUT", entity), responseClass));
    }

    protected <T extends Response> T delete(URL url, Class<T> responseClass) throws CoinbaseException, IOException {
        return handleErrors(deserialize(doHttp(url, "DELETE", null), responseClass));
    }

    protected static <T extends Response> T handleErrors(T response) throws CoinbaseException {
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

    protected Request newAccountSpecificRequest() {
        Request request = new Request();
        if (_accountId != null) {
            request.setAccountId(_accountId);
        }
        return request;
    }

    protected HashMap<String, String> getV2VersionHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("CB-VERSION", com.coinbase.ApiConstants.VERSION);
        headers.put("CB-CLIENT", getPackageVersionName());
        return headers;
    }


    protected Interceptor buildOAuthInterceptor() {
        return new Interceptor() {

            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + _accessToken)
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    protected Interceptor buildHmacAuthInterceptor() {
        return new Interceptor() {

            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
                String method = request.method().toUpperCase();
                String path = request.url().url().getFile();
                String body = "";
                if (request.body() != null) {
                    final okhttp3.Request requestCopy = request.newBuilder().build();
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

                okhttp3.Request newRequest = request.newBuilder()
                        .addHeader("CB-ACCESS-KEY", _apiKey)
                        .addHeader("CB-ACCESS_SIGN", signature)
                        .addHeader("CB-ACCESS-TIMESTAMP", timestamp)
                        .build();

                return chain.proceed(newRequest);
            }
        };
    }

    protected String getPackageVersionName() {
        String packageName = "";
        String versionName = "";

        if (_context != null) {
            packageName = _context.getPackageName();
        }

        try {
            versionName = BuildConfig.VERSION_NAME + "/" + BuildConfig.VERSION_CODE;
        } catch (Throwable t) {

        }

        return packageName + "/" + versionName;
    }

    protected Interceptor buildVersionInterceptor() {

        return new Interceptor() {

            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("CB-VERSION", com.coinbase.ApiConstants.VERSION)
                        .addHeader("CB-CLIENT", getPackageVersionName())
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    protected Interceptor languageInterceptor() {
        return new Interceptor() {

            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }

    /**
     * Interceptor for device info, override this to add device info
     * @return
     */
    protected Interceptor deviceInfoInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request newRequest = chain
                        .request()
                        .newBuilder()
                        .build();

                return chain.proceed(newRequest);
            }
        };
    }

    /**
     * Interceptor for network sniffing, override this to add network sniffing
     * @return
     */
    protected Interceptor networkSniffingInterceptor() {
        return chain -> chain.proceed(chain.request());
    }

    /**
     * Interceptor for logging, override this to add logging
     * @return
     */
    protected Interceptor loggingInterceptor() {
        return chain -> chain.proceed(chain.request());
    }

    protected Pair<ApiInterface, Retrofit> getOAuthApiService() {
        return getService(_baseOAuthUrl.toString());
    }

    protected Pair<ApiInterface, Retrofit> getApiService() {
        return getService(_baseV2ApiUrl.toString());
    }


    private synchronized Pair<ApiInterface, Retrofit> getService(String url) {
        if (mInitializedServices.containsKey(url)) {
            return mInitializedServices.get(url);
        }

        OkHttpClient.Builder clientBuilder = generateClientBuilder(_sslContext);

        if (_accessToken != null)
            clientBuilder.addInterceptor(buildOAuthInterceptor());

        clientBuilder.addInterceptor(buildVersionInterceptor());
        clientBuilder.addInterceptor(languageInterceptor());
        clientBuilder.addInterceptor(deviceInfoInterceptor());
        clientBuilder.addInterceptor(_cache.createInterceptor());
        clientBuilder.addInterceptor(loggingInterceptor());
        clientBuilder.addNetworkInterceptor(networkSniffingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        com.coinbase.ApiInterface service = retrofit.create(com.coinbase.ApiInterface.class);

        Pair<ApiInterface, Retrofit> servicePair = new Pair<>(service, retrofit);
        mInitializedServices.put(url, servicePair);

        return servicePair;
    }

    protected Pair<ApiInterfaceRx, Retrofit> getOAuthApiServiceRx() {
        return getServiceRx(_baseOAuthUrl.toString());
    }

    protected Pair<ApiInterfaceRx, Retrofit> getApiServiceRx() {
        return getServiceRx(_baseV2ApiUrl.toString());
    }


    private synchronized Pair<ApiInterfaceRx, Retrofit> getServiceRx(String url) {
        if (mInitializedServicesRx.containsKey(url)) {
            return mInitializedServicesRx.get(url);
        }

        OkHttpClient.Builder clientBuilder = generateClientBuilder(_sslContext);

        if (_accessToken != null)
            clientBuilder.addInterceptor(buildOAuthInterceptor());

        clientBuilder.addInterceptor(buildVersionInterceptor());
        clientBuilder.addInterceptor(languageInterceptor());
        clientBuilder.addInterceptor(deviceInfoInterceptor());
        clientBuilder.addInterceptor(_cache.createInterceptor());
        clientBuilder.addInterceptor(loggingInterceptor());
        clientBuilder.addNetworkInterceptor(networkSniffingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addCallAdapterFactory((_backgroundScheduler == null) ?
                        RxJavaCallAdapterFactory.create() : RxJavaCallAdapterFactory.createWithScheduler(_backgroundScheduler))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        com.coinbase.ApiInterfaceRx service = retrofit.create(com.coinbase.ApiInterfaceRx.class);

        Pair<ApiInterfaceRx, Retrofit> servicePair = new Pair<>(service, retrofit);
        mInitializedServicesRx.put(url, servicePair);

        return servicePair;
    }

    /**
     * Refresh OAuth token
     *
     * @param clientId
     * @param clientSecret
     * @param refreshToken
     * @param callback
     * @return call object
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    public Call refreshTokens(String clientId,
                              String clientSecret,
                              String refreshToken,
                              final CallbackWithRetrofit<AccessToken> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.CLIENT_ID, clientId);
        params.put(ApiConstants.CLIENT_SECRET, clientSecret);
        params.put(ApiConstants.REFRESH_TOKEN, refreshToken);
        params.put(ApiConstants.GRANT_TYPE, ApiConstants.REFRESH_TOKEN);

        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getOAuthApiService();
        Call call = apiRetrofitPair.first.refreshTokens(params);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, retrofit2.Response<AccessToken> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);

                if (response != null && response.body() != null)
                    _accessToken = response.body().getAccessToken();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Refresh OAuth token
     *
     * @param clientId
     * @param clientSecret
     * @param refreshToken
     * @return observable object emitting acesstoken/retrofit pair
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    public Observable<Pair<retrofit2.Response<AccessToken>, Retrofit>> refreshTokensRx(String clientId,
                                                                                       String clientSecret,
                                                                                       String refreshToken) {
        HashMap<String, Object> params = getRefreshTokensParams(clientId, clientSecret, refreshToken);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getOAuthApiServiceRx();
        Observable<retrofit2.Response<AccessToken>> userObservable = apiRetrofitPair.first.refreshTokens(params);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(userObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Revoke OAuth token
     *
     * @param callback
     * @return call object
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    public Call revokeToken(final CallbackWithRetrofit<Void> callback) {
        if (_accessToken == null) {
            Log.w("Coinbase Error", "This client must have been initialized with an access token in order to call revokeToken()");
            return null;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.TOKEN, _accessToken);

        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getOAuthApiService();
        Call call = apiRetrofitPair.first.revokeToken(params);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    _accessToken = null;
                }

                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Revoke OAuth token
     *
     * @return observable object emitting void/retrofit pair
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> revokeTokenRx() {
        if (_accessToken == null) {
            Log.w("Coinbase Error", "This client must have been initialized with an access token in order to call revokeToken()");
            return null;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.TOKEN, _accessToken);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getOAuthApiServiceRx();
        Observable<retrofit2.Response<Void>> revokeObservable = apiRetrofitPair.first.revokeToken(params);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(revokeObservable,
                retrofitObservable,
                (a, b) -> {
                    _accessToken = null;
                    return new Pair<>(a, b);
                });
    }


    /**
     * Retrieve the current user and their settings.
     *
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-user">Online Documentation</a>
     */
    public Call getUser(final CallbackWithRetrofit<com.coinbase.v2.models.user.User> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        Call call = apiRetrofitPair.first.getUser();
        call.enqueue(new Callback<com.coinbase.v2.models.user.User>() {

            public void onResponse(Call<com.coinbase.v2.models.user.User> call, retrofit2.Response<com.coinbase.v2.models.user.User> response) {
                if (callback != null) {
                    callback.onResponse(call, response, apiRetrofitPair.second);
                }
            }


            public void onFailure(Call<com.coinbase.v2.models.user.User> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Retrieve the current user and their settings.
     *
     * @return observable object that emits user/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-user">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.user.User>, Retrofit>> getUserRx() {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.user.User>> userObservable = apiRetrofitPair.first.getUser();
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(userObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Modify current user and their preferences
     *
     * @param name           User's public name
     * @param timeZone       Time zone
     * @param nativeCurrency Local currency used to display amounts converted from BTC
     * @param callback       callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#update-current-user">Online Documentation</a>
     */
    public Call updateUser(String name, String timeZone, String nativeCurrency, final CallbackWithRetrofit<com.coinbase.v2.models.user.User> callback) {
        HashMap<String, Object> params = getUpdateUserParams(name, timeZone, nativeCurrency);

        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.updateUser(params);
        call.enqueue(new Callback<com.coinbase.v2.models.user.User>() {

            public void onResponse(Call<com.coinbase.v2.models.user.User> call, retrofit2.Response<com.coinbase.v2.models.user.User> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.user.User> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Modify current user and their preferences
     *
     * @param name           User's public name
     * @param timeZone       Time zone
     * @param nativeCurrency Local currency used to display amounts converted from BTC
     * @return observable object with user/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#update-current-user">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.user.User>, Retrofit>> updateUserRx(String name, String timeZone, String nativeCurrency) {
        HashMap<String, Object> params = getUpdateUserParams(name, timeZone, nativeCurrency);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.user.User>> userObservable = apiRetrofitPair.first.updateUser(params);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(userObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve an account belonging to this user
     *
     * @param accountId account ID for the account to retrieve
     * @param callback  callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#show-an-account">Online Documentation</a>
     */
    public Call getAccount(String accountId, final CallbackWithRetrofit<com.coinbase.v2.models.account.Account> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.getAccount(accountId);
        call.enqueue(new Callback<com.coinbase.v2.models.account.Account>() {

            public void onResponse(Call<com.coinbase.v2.models.account.Account> call, retrofit2.Response<com.coinbase.v2.models.account.Account> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.account.Account> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Retrieve an account belonging to this user
     *
     * @param accountId account ID for the account to retrieve
     * @return observable object emitting account/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#show-an-account">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.account.Account>, Retrofit>> getAccountRx(String accountId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.account.Account>> accountObservable = apiRetrofitPair.first.getAccount(accountId);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(accountObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve a list of accounts belonging to this user
     *
     * @param options  endpoint options
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#list-accounts">Online Documentation</a>
     */
    public Call getAccounts(HashMap<String, Object> options, final CallbackWithRetrofit<Accounts> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        options = cleanQueryMap(options);

        Call call = apiRetrofitPair.first.getAccounts(options);
        call.enqueue(new Callback<Accounts>() {

            public void onResponse(Call<Accounts> call, retrofit2.Response<Accounts> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Accounts> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Retrieve a list of accounts belonging to this user
     *
     * @param options endpoint options
     * @return observable object emitting accounts/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#list-accounts">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Accounts>, Retrofit>> getAccountsRx(HashMap<String, Object> options) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        options = cleanQueryMap(options);

        Observable<retrofit2.Response<Accounts>> accountsObservable = apiRetrofitPair.first.getAccounts(options);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(accountsObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }


    /**
     * Create a new account for user
     *
     * @param options  endpoint options
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#create-account">Online Documentation</a>
     */
    public Call createAccount(HashMap<String, Object> options, final CallbackWithRetrofit<com.coinbase.v2.models.account.Account> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.createAccount(options);
        call.enqueue(new Callback<com.coinbase.v2.models.account.Account>() {

            public void onResponse(Call<com.coinbase.v2.models.account.Account> call, retrofit2.Response<com.coinbase.v2.models.account.Account> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.account.Account> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Create a new account for user
     *
     * @param options endpoint options
     * @return observable object emitting account/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#create-account">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.account.Account>, Retrofit>> createAccountRx(HashMap<String, Object> options) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.account.Account>> accountObservable = apiRetrofitPair.first.createAccount(options);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(accountObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Promote an account as primary account
     *
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#set-account-as-primary">Online Documentation</a>
     */
    public Call setAccountPrimary(String accountId, final CallbackWithRetrofit<Void> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.setAccountPrimary(accountId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Promote an account as primary account
     *
     * @return observable object emitting void/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#set-account-as-primary">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> setAccountPrimaryRx(String accountId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<Void>> observable = apiRetrofitPair.first.setAccountPrimary(accountId);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Modifies user's account
     *
     * @param options  endpoint options
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#update-account">Online Documentation</a>
     */
    public Call updateAccount(String accountId, HashMap<String, Object> options, final CallbackWithRetrofit<com.coinbase.v2.models.account.Account> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.updateAccount(accountId, options);
        call.enqueue(new Callback<com.coinbase.v2.models.account.Account>() {

            public void onResponse(Call<com.coinbase.v2.models.account.Account> call, retrofit2.Response<com.coinbase.v2.models.account.Account> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.account.Account> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Modifies user's account
     *
     * @param options endpoint options
     * @return observable object account/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#update-account">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.account.Account>, Retrofit>> updateAccountRx(String accountId,
                                                                                                                  HashMap<String, Object> options) {

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.account.Account>> accountObservable = apiRetrofitPair.first.updateAccount(accountId, options);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(accountObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Removes user's account. See documentation for restrictions
     *
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#delete-account">Online Documentation</a>
     */
    public Call deleteAccount(String accountId, final CallbackWithRetrofit<Void> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.deleteAccount(accountId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Removes user's account. See documentation for restrictions
     *
     * @return observable object void/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#delete-account">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> deleteAccountRx(String accountId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<Void>> observable = apiRetrofitPair.first.deleteAccount(accountId);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }


    /**
     * Retrieve a list of the user's recent transactions.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param options       endpoint options
     * @param expandOptions expand options
     * @param callback      callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#list-transactions">Online Documentation</a>
     */
    public Call getTransactions(String accountId,
                                HashMap<String, Object> options,
                                List<String> expandOptions,
                                final CallbackWithRetrofit<Transactions> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        options = cleanQueryMap(options);

        Call call = apiRetrofitPair.first.getTransactions(accountId, expandOptions, options);
        call.enqueue(new Callback<Transactions>() {

            public void onResponse(Call<Transactions> call, retrofit2.Response<Transactions> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Transactions> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Retrieve a list of the user's recent transactions.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param options       endpoint options
     * @param expandOptions expand options
     * @return observable object emitting transactions/retrofit object
     * @see <a href="https://developers.coinbase.com/api/v2#list-transactions">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Transactions>, Retrofit>> getTransactionsRx(String accountId,
                                                                                          HashMap<String, Object> options,
                                                                                          List<String> expandOptions) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        options = cleanQueryMap(options);

        Observable<retrofit2.Response<Transactions>> transactionObservable = apiRetrofitPair.first.getTransactions(accountId, expandOptions, options);
        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(transactionObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve details of an individual transaction.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the transaction id or idem field value
     * @param callback      callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-transaction">Online Documentation</a>
     */
    public Call getTransaction(String accountId, String transactionId, final CallbackWithRetrofit<com.coinbase.v2.models.transactions.Transaction> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        List<String> expandOptions = getTransactionExpandOptions();
        Call call = apiRetrofitPair.first.getTransaction(accountId, transactionId, expandOptions);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {

            public void onResponse(Call<com.coinbase.v2.models.transactions.Transaction> call, retrofit2.Response<com.coinbase.v2.models.transactions.Transaction> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transactions.Transaction> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Retrieve details of an individual transaction.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the transaction id or idem field value
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-transaction">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>, Retrofit>> getTransactionRx(String accountId,
                                                                                                                            String transactionId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        List<String> expandOptions = getTransactionExpandOptions();

        Observable<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>> transactionObservable =
                apiRetrofitPair.first.getTransaction(accountId, transactionId, expandOptions);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(transactionObservable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Complete a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be completed
     * @param callback      callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#complete-request-money">Online Documentation</a>
     */
    public Call completeRequest(String accountId, String transactionId, final CallbackWithRetrofit<Void> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.completeRequest(accountId, transactionId);
        call.enqueue(new Callback<Void>() {

            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Void> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Complete a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be completed
     * @return observable object emitting void/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#complete-request-money">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> completeRequestRx(String accountId, String transactionId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<Void>> observable = apiRetrofitPair.first.completeRequest(accountId, transactionId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }


    /**
     * Resend emails for a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be resent
     * @param callback      callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#re-send-request-money">Online Documentation</a>
     */
    public Call resendRequest(String accountId, String transactionId, final CallbackWithRetrofit<Void> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.resendRequest(accountId, transactionId);
        call.enqueue(new Callback<Void>() {

            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Void> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Resend emails for a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be resent
     * @return observable object emitting void/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#re-send-request-money">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> resendRequestRx(String accountId, String transactionId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<Void>> observable = apiRetrofitPair.first.resendRequest(accountId, transactionId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Cancel a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be cancelled
     * @param callback      callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#cancel-request-money">Online Documentation</a>
     */
    public Call cancelRequest(String accountId, String transactionId, final CallbackWithRetrofit<Void> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.cancelTransaction(accountId, transactionId);
        call.enqueue(new Callback<Void>() {

            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Void> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Cancel a money request.
     *
     * @param accountId     account ID that the transaction belongs to
     * @param transactionId the id of the request money transaction to be cancelled
     * @return observable object emitting void/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#cancel-request-money">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Void>, Retrofit>> cancelRequestRx(String accountId, String transactionId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<Void>> observable = apiRetrofitPair.first.cancelTransaction(accountId, transactionId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Send money to an email address or bitcoin address
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#send-money">Online Documentation</a>
     */
    public Call sendMoney(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.SEND);
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.sendMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {

            public void onResponse(Call<com.coinbase.v2.models.transactions.Transaction> call, retrofit2.Response<com.coinbase.v2.models.transactions.Transaction> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transactions.Transaction> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Send money to an email address or bitcoin address
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return observable object emitting transaction/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#send-money">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>, Retrofit>> sendMoneyRx(String accountId,
                                                                                                                       HashMap<String, Object> params) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.SEND);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>> observable = apiRetrofitPair.first.sendMoney(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Request money from an email address or bitcoin address
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#request-money">Online Documentation</a>
     */
    public Call requestMoney(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.REQUEST);
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.requestMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {

            public void onResponse(Call<com.coinbase.v2.models.transactions.Transaction> call, retrofit2.Response<com.coinbase.v2.models.transactions.Transaction> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transactions.Transaction> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Request money from an email address or bitcoin address
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return observable object emitting transaction/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#request-money">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>, Retrofit>> requestMoneyRx(String accountId,
                                                                                                                          HashMap<String, Object> params) {
        params.put(com.coinbase.ApiConstants.TYPE, com.coinbase.ApiConstants.REQUEST);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>> observable = apiRetrofitPair.first.requestMoney(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Transfer bitcoin between two of a user’s accounts
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#transfer-money-between-accounts">Online Documentation</a>
     */
    public Call transferMoney(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transactions.Transaction> callback) {
        params.put(com.coinbase.ApiConstants.TYPE, ApiConstants.TRANSFER);
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.transferMoney(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transactions.Transaction>() {

            public void onResponse(Call<com.coinbase.v2.models.transactions.Transaction> call, retrofit2.Response<com.coinbase.v2.models.transactions.Transaction> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transactions.Transaction> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Transfer bitcoin between two of a user’s accounts
     *
     * @param accountId account ID that the transaction belongs to
     * @param params    endpoint parameters
     * @return observable object emitting transaction/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#transfer-money-between-accounts">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>, Retrofit>> transferMoneyRx(String accountId, HashMap<String, Object> params) {
        params.put(com.coinbase.ApiConstants.TYPE, ApiConstants.TRANSFER);

        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transactions.Transaction>> observable = apiRetrofitPair.first.transferMoney(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));

    }

    /**
     * Buys user-defined amount of bitcoin.
     *
     * @param accountId account ID that the buy belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#buy-bitcoin">Online Documentation</a>
     */
    public Call buyBitcoin(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.buyBitcoin(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Buys user-defined amount of bitcoin.
     *
     * @param accountId account ID that the buy belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#buy-bitcoin">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> buyBitcoinRx(String accountId, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.buyBitcoin(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Commits a buy that is created in commit: false state.
     *
     * @param accountId account ID that the buy belongs to
     * @param buyId     buy ID that the buy belongs to
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-buy">Online Documentation</a>
     */

    public Call commitBuyBitcoin(String accountId, String buyId, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.commitBuyBitcoin(accountId, buyId);

        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Commits a buy that is created in commit: false state.
     *
     * @param accountId account ID that the buy belongs to
     * @param buyId     buy ID that the buy belongs to
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-buy">Online Documentation</a>
     */

    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> commitBuyBitcoinRx(String accountId, String buyId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.commitBuyBitcoin(accountId, buyId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Sells user-defined amount of bitcoin.
     *
     * @param accountId account ID that the sell belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#sell-bitcoin">Online Documentation</a>
     */
    public Call sellBitcoin(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.sellBitcoin(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Sells user-defined amount of bitcoin.
     *
     * @param accountId account ID that the sell belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#sell-bitcoin">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> sellBitcoinRx(String accountId, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.sellBitcoin(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Commits a sell that is created in commit: false state.
     *
     * @param accountId account ID that the sell belongs to
     * @param sellId    sell ID that the sell belongs to
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-sell">Online Documentation</a>
     */

    public Call commitSellBitcoin(String accountId, String sellId, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.commitSellBitcoin(accountId, sellId);

        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Commits a sell that is created in commit: false state.
     *
     * @param accountId account ID that the sell belongs to
     * @param sellId    sell ID that the sell belongs to
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-sell">Online Documentation</a>
     */

    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> commitSellBitcoinRx(String accountId, String sellId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();
        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.commitSellBitcoin(accountId, sellId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve the current sell price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getSellPrice(String baseCurrency, String fiatCurrency,
                             HashMap<String, Object> params, final CallbackWithRetrofit<Price> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        params = cleanQueryMap(params);

        Call call = apiRetrofitPair.first.getSellPrice(baseCurrency, fiatCurrency, params);
        call.enqueue(new Callback<Price>() {

            public void onResponse(Call<Price> call, retrofit2.Response<Price> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Price> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Retrieve the current sell price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return observable object emitting price/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Price>, Retrofit>> getSellPriceRx(String baseCurrency, String fiatCurrency, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        params = cleanQueryMap(params);
        Observable<retrofit2.Response<Price>> observable = apiRetrofitPair.first.getSellPrice(baseCurrency, fiatCurrency, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve the current buy price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       optional HashMap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getBuyPrice(String baseCurrency, String fiatCurrency,
                            HashMap<String, Object> params, final CallbackWithRetrofit<Price> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        params = cleanQueryMap(params);

        Call call = apiRetrofitPair.first.getBuyPrice(baseCurrency, fiatCurrency, params);
        call.enqueue(new Callback<Price>() {

            public void onResponse(Call<Price> call, retrofit2.Response<Price> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Price> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Retrieve the current buy price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       optional HashMap of params as indicated in api docs
     * @return observable object emitting price/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Price>, Retrofit>> getBuyPriceRx(String baseCurrency, String fiatCurrency, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        params = cleanQueryMap(params);

        Observable<retrofit2.Response<Price>> observable = apiRetrofitPair.first.getBuyPrice(baseCurrency, fiatCurrency, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve the current spot price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getSpotPrice(String baseCurrency, String fiatCurrency,
                             HashMap<String, Object> params, final CallbackWithRetrofit<Price> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        params = cleanQueryMap(params);

        Call call = apiRetrofitPair.first.getSpotPrice(baseCurrency, fiatCurrency, params);
        call.enqueue(new Callback<Price>() {

            public void onResponse(Call<Price> call, retrofit2.Response<Price> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Price> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Retrieve the current spot price of 1 BTC
     *
     * @param baseCurrency the digital currency in which to retrieve the price against
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return observable object emitting price/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Price>, Retrofit>> getSpotPriceRx(String baseCurrency, String fiatCurrency, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        params = cleanQueryMap(params);

        Observable<retrofit2.Response<Price>> observable = apiRetrofitPair.first.getSpotPrice(baseCurrency, fiatCurrency, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Retrieve the spot prices for all supported currencies for the given fiatCurrency
     *
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return observable object emitting price/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Call getSpotPrices(String fiatCurrency,
                             HashMap<String, Object> params, final CallbackWithRetrofit<Prices> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        params = cleanQueryMap(params);

        Call call = apiRetrofitPair.first.getSpotPrices(fiatCurrency, params);
        call.enqueue(new Callback<Prices>() {

            public void onResponse(Call<Prices> call, retrofit2.Response<Prices> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<Prices> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Retrieve the spot prices for all supported currencies for the given fiatCurrency
     *
     * @param fiatCurrency the currency in which to retrieve the price
     * @param params       HashMap of params as indicated in api docs
     * @return observable object emitting price/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<Prices>, Retrofit>> getSpotPricesRx(String fiatCurrency, HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        params = cleanQueryMap(params);

        Observable<retrofit2.Response<Prices>> observable = apiRetrofitPair.first.getSpotPrices(fiatCurrency, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Generate new address for an account
     *
     * @param accountId the accountId of the account
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#create-address">Online Documentation</a>
     */

    public Call generateAddress(String accountId, final CallbackWithRetrofit<com.coinbase.v2.models.address.Address> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.generateAddress(accountId);
        call.enqueue(new Callback<com.coinbase.v2.models.address.Address>() {

            public void onResponse(Call<com.coinbase.v2.models.address.Address> call, retrofit2.Response<com.coinbase.v2.models.address.Address> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            public void onFailure(Call<com.coinbase.v2.models.address.Address> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Generate new address for an account
     *
     * @param accountId the accountId of the account
     * @return observable object emitting address/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#create-address">Online Documentation</a>
     */

    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.address.Address>, Retrofit>> generateAddressRx(String accountId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.address.Address>> observable = apiRetrofitPair.first.generateAddress(accountId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Deposits user-defined amount of funds to a fiat account.
     *
     * @param accountId account ID that the deposit belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#deposit-funds">Online Documentation</a>
     */
    public Call depositFunds(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.depositFunds(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Deposits user-defined amount of funds to a fiat account.
     *
     * @param accountId account ID that the deposit belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#deposit-funds">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> depositFundsRx(String accountId,
                                                                                                                    HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.depositFunds(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Commits a deposit that is created in commit: false state.
     *
     * @param accountId account ID that the deposit belongs to
     * @param depositId deposit ID that the deposit belongs to
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-deposit">Online Documentation</a>
     */

    public Call commitDeposit(String accountId, String depositId, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.commitDeposit(accountId, depositId);

        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Commits a deposit that is created in commit: false state.
     *
     * @param accountId account ID that the deposit belongs to
     * @param depositId deposit ID that the deposit belongs to
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-deposit">Online Documentation</a>
     */

    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> commitDepositRx(String accountId, String depositId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.commitDeposit(accountId, depositId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Withdraws user-defined amount of funds from a fiat account.
     *
     * @param accountId account ID that the withdrawal belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#withdraw-funds">Online Documentation</a>
     */
    public Call withdrawFunds(String accountId, HashMap<String, Object> params, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.withdrawFunds(accountId, params);
        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Withdraws user-defined amount of funds from a fiat account.
     *
     * @param accountId account ID that the withdrawal belongs to
     * @param params    hashmap of params as indicated in api docs
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#withdraw-funds">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> withdrawFundsRx(String accountId,
                                                                                                                     HashMap<String, Object> params) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.withdrawFunds(accountId, params);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Commits a withdrawal that is created in commit: false state.
     *
     * @param accountId  account ID that the withdrawal belongs to
     * @param withdrawId deposit ID that the withdrawal belongs to
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-deposit">Online Documentation</a>
     */

    public Call commitWithdraw(String accountId, String withdrawId, final CallbackWithRetrofit<com.coinbase.v2.models.transfers.Transfer> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.commitWithdraw(accountId, withdrawId);

        call.enqueue(new Callback<com.coinbase.v2.models.transfers.Transfer>() {

            public void onResponse(Call<com.coinbase.v2.models.transfers.Transfer> call, retrofit2.Response<com.coinbase.v2.models.transfers.Transfer> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<com.coinbase.v2.models.transfers.Transfer> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Commits a withdrawal that is created in commit: false state.
     *
     * @param accountId  account ID that the withdrawal belongs to
     * @param withdrawId deposit ID that the withdrawal belongs to
     * @return observable object emitting transfer/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#commit-a-deposit">Online Documentation</a>
     */

    public Observable<Pair<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>, Retrofit>> commitWithdrawRx(String accountId, String withdrawId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<com.coinbase.v2.models.transfers.Transfer>> observable = apiRetrofitPair.first.commitWithdraw(accountId, withdrawId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Show current user’s payment method.
     *
     * @param paymentMethodId paymentMethod ID for the account to retrieve
     * @param callback        callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-payment-method">Online Documentation</a>
     */
    public Call getPaymentMethod(String paymentMethodId, final CallbackWithRetrofit<PaymentMethod> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();
        Call call = apiRetrofitPair.first.getPaymentMethod(paymentMethodId);
        call.enqueue(new Callback<PaymentMethod>() {

            public void onResponse(Call<PaymentMethod> call, retrofit2.Response<PaymentMethod> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<PaymentMethod> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Show current user’s payment method.
     *
     * @param paymentMethodId paymentMethod ID for the account to retrieve
     * @return observable object paymentmethod/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-payment-method">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<PaymentMethod>, Retrofit>> getPaymentMethodRx(String paymentMethodId) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<PaymentMethod>> observable = apiRetrofitPair.first.getPaymentMethod(paymentMethodId);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Lists current user’s payment methods.
     *
     * @param options  endpoint options
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#list-payment-methods">Online Documentation</a>
     */
    public Call getPaymentMethods(HashMap<String, Object> options, final CallbackWithRetrofit<PaymentMethods> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        options = cleanQueryMap(options);

        Call call = apiRetrofitPair.first.getPaymentMethods(options);
        call.enqueue(new Callback<PaymentMethods>() {

            public void onResponse(Call<PaymentMethods> call, retrofit2.Response<PaymentMethods> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }


            public void onFailure(Call<PaymentMethods> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Lists current user’s payment methods.
     *
     * @param options endpoint options
     * @return observable object emitting paymentmethods/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#list-payment-methods">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<PaymentMethods>, Retrofit>> getPaymentMethodsRx(HashMap<String, Object> options) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        options = cleanQueryMap(options);

        Observable<retrofit2.Response<PaymentMethods>> observable = apiRetrofitPair.first.getPaymentMethods(options);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Get current exchange rates.
     *
     * @param currency base currency (Default: USD)
     * @param callback callback interface
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#get-exchange-rates">Online Documentation</a>
     */
    public Call getExchangeRates(HashMap<String, Object> currency, final CallbackWithRetrofit<ExchangeRates> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        currency = cleanQueryMap(currency);

        Call call = apiRetrofitPair.first.getExchangeRates(currency);
        call.enqueue(new Callback<ExchangeRates>() {

            public void onResponse(Call<ExchangeRates> call, retrofit2.Response<ExchangeRates> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }

    /**
     * Get current exchange rates.
     *
     * @param currency base currency (Default: USD)
     * @return observable object emitting exchangerates/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#get-exchange-rates">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<ExchangeRates>, Retrofit>> getExchangeRatesRx(HashMap<String, Object> currency) {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        currency = cleanQueryMap(currency);

        Observable<retrofit2.Response<ExchangeRates>> observable = apiRetrofitPair.first.getExchangeRates(currency);

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Get a list of known currencies.
     *
     * @return call object
     * @see <a href="https://developers.coinbase.com/api/v2#currencies">Online Documentation</a>
     */
    public Call getSupportedCurrencies(final CallbackWithRetrofit<SupportedCurrencies> callback) {
        final Pair<ApiInterface, Retrofit> apiRetrofitPair = getApiService();

        Call call = apiRetrofitPair.first.getSupportedCurrencies();
        call.enqueue(new Callback<SupportedCurrencies>() {

            public void onResponse(Call<SupportedCurrencies> call, retrofit2.Response<SupportedCurrencies> response) {
                if (callback != null)
                    callback.onResponse(call, response, apiRetrofitPair.second);
            }

            public void onFailure(Call<SupportedCurrencies> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(call, t);
            }
        });

        return call;
    }


    /**
     * Get a list of known currencies.
     *
     * @return observable object emitting supportedcurrencies/retrofit pair
     * @see <a href="https://developers.coinbase.com/api/v2#currencies">Online Documentation</a>
     */
    public Observable<Pair<retrofit2.Response<SupportedCurrencies>, Retrofit>> getSupportedCurrenciesRx() {
        final Pair<ApiInterfaceRx, Retrofit> apiRetrofitPair = getApiServiceRx();

        Observable<retrofit2.Response<SupportedCurrencies>> observable = apiRetrofitPair.first.getSupportedCurrencies();

        Observable<Retrofit> retrofitObservable = Observable.just(apiRetrofitPair.second);
        return Observable.combineLatest(observable,
                retrofitObservable,
                (a, b) -> new Pair<>(a, b));
    }

    /**
     * Remove any null values from the HashMap. If the HashMap is itself null, return an empty HashMap.
     * This is due to a difference between retrofit 1 and retrofit 2.  Retrofit 1 would quietly remove any
     * null query params and handle the query map itself being null. Retrofit2 throws an exception and fails
     * the request.
     *
     * @param options
     * @return
     */
    protected HashMap<String, Object> cleanQueryMap(HashMap<String, Object> options) {
        if (options == null) {
            return new HashMap<>();
        } else {
            HashMap optionsCopy = new HashMap<>(options);
            for (String key : options.keySet()) {
                if (optionsCopy.get(key) == null) {
                    optionsCopy.remove(key);
                }
            }
            return optionsCopy;
        }
    }

    private HashMap<String, Object> getRefreshTokensParams(String clientId, String clientSecret, String refreshToken) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiConstants.CLIENT_ID, clientId);
        params.put(ApiConstants.CLIENT_SECRET, clientSecret);
        params.put(ApiConstants.REFRESH_TOKEN, refreshToken);
        params.put(ApiConstants.GRANT_TYPE, ApiConstants.REFRESH_TOKEN);
        return params;
    }

    private HashMap<String, Object> getUpdateUserParams(String name, String timeZone, String nativeCurrency) {
        HashMap<String, Object> params = new HashMap<>();

        if (name != null)
            params.put(ApiConstants.NAME, name);

        if (timeZone != null)
            params.put(ApiConstants.TIME_ZONE, timeZone);

        if (nativeCurrency != null)
            params.put(ApiConstants.NATIVE_CURRENCY, nativeCurrency);

        return params;
    }

    private List<String> getTransactionExpandOptions() {
        return Arrays.asList(com.coinbase.ApiConstants.FROM,
                com.coinbase.ApiConstants.TO,
                com.coinbase.ApiConstants.BUY,
                com.coinbase.ApiConstants.SELL);
    }
}
