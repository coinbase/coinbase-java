/*
 * Copyright 2018 Coinbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.coinbase.ApiConstants.Headers;
import com.coinbase.coinbase_java.BuildConfig;
import com.coinbase.errors.CoinbaseErrorResponse;
import com.coinbase.errors.CoinbaseException;
import com.coinbase.errors.CoinbaseOAuthException;
import com.coinbase.errors.OAuthError;
import com.coinbase.network.ApiCall;
import com.coinbase.network.ApiCallAdapterFactory;
import com.coinbase.resources.accounts.AccountResource;
import com.coinbase.resources.accounts.AccountsApi;
import com.coinbase.resources.accounts.AccountsApiRx;
import com.coinbase.resources.accounts.Currency;
import com.coinbase.resources.addresses.AddressResource;
import com.coinbase.resources.addresses.AddressesApi;
import com.coinbase.resources.addresses.AddressesApiRx;
import com.coinbase.resources.auth.AccessToken;
import com.coinbase.resources.auth.AuthApi;
import com.coinbase.resources.auth.AuthApiRx;
import com.coinbase.resources.auth.AuthResource;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.base.DynamicResourceDeserializer;
import com.coinbase.resources.buys.BuysResource;
import com.coinbase.resources.currencies.CurrenciesApi;
import com.coinbase.resources.currencies.CurrenciesApiRx;
import com.coinbase.resources.currencies.CurrenciesResource;
import com.coinbase.resources.deposits.DepositsResource;
import com.coinbase.resources.paymentmethods.PaymentMethodResource;
import com.coinbase.resources.paymentmethods.PaymentMethodsApi;
import com.coinbase.resources.paymentmethods.PaymentMethodsApiRx;
import com.coinbase.resources.prices.PricesApi;
import com.coinbase.resources.prices.PricesApiRx;
import com.coinbase.resources.prices.PricesResource;
import com.coinbase.resources.rates.ExchangeRatesApi;
import com.coinbase.resources.rates.ExchangeRatesApiRx;
import com.coinbase.resources.rates.ExchangeRatesResource;
import com.coinbase.resources.sells.SellsResource;
import com.coinbase.resources.time.TimeApi;
import com.coinbase.resources.time.TimeApiRx;
import com.coinbase.resources.time.TimeResource;
import com.coinbase.resources.trades.PlaceTradeOrderBody;
import com.coinbase.resources.trades.PlaceTradeOrderBodySerializer;
import com.coinbase.resources.trades.Trade;
import com.coinbase.resources.trades.TradesApi;
import com.coinbase.resources.trades.TradesApiRx;
import com.coinbase.resources.trades.TradesDeserializer;
import com.coinbase.resources.transactions.TransactionsApi;
import com.coinbase.resources.transactions.TransactionsApiRx;
import com.coinbase.resources.transactions.TransactionsResource;
import com.coinbase.resources.transactions.entities.CryptoAddress;
import com.coinbase.resources.transactions.entities.CryptoNetwork;
import com.coinbase.resources.users.UserResource;
import com.coinbase.resources.users.UsersApi;
import com.coinbase.resources.users.UsersApiRx;
import com.coinbase.resources.withdrawals.WithdrawalsResource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.coinbase.ApiConstants.OAuth.OAUTH;

/**
 * Coinbase instance configures network setting, handles authentication and tokens.
 * <p>
 * After Coinbase object is created and configured, it can be used to obtain resource classes
 * located at {@link com.coinbase.resources} package (like {@link AuthResource}). Having an instance
 * of particular resource allows you to perform communication with Coinbase server for a corresponding
 * aspect.
 * <p>
 * Use {@link CoinbaseBuilder} to build Coinbase object
 */
public class Coinbase {

    public static final int READ_TIMEOUT = 30;
    public static final int CONNECTION_TIMEOUT = 30;
    public static final String OAUTH_URL = "https://www.coinbase.com/oauth/";
    public static final String BASE_API_URL = "https://api.coinbase.com/v2/";
    protected static final String PREFS_NAME = "com.coinbase.android.sdk";
    protected static final String KEY_LOGIN_CSRF_TOKEN = "com.coinbase.android.sdk.login_csrf_token";
    protected static final int BTC_DECIMAL_PLACES = 8;
    private static final String LOG_TAG = "Coinbase";
    private static final int PERSISTENT_CACHE_SIZE = 5 * 1024 * 1024; // 5MB

    protected Context context;

    // region Networking fields
    /**
     * Main {@link Retrofit} instance to make networking calls.
     */
    protected URL baseOAuthUrl;
    protected URL baseApiUrl;
    protected final Cache diskCache;
    protected volatile Coinbase.TokenListener tokenListener;
    protected Scheduler backgroundScheduler;
    protected SSLContext sslContext;
    protected SSLSocketFactory sslSocketFactory;
    protected Set<String> cryptoAddressNames = new HashSet<>();
    protected Set<String> cryptoNetworkNames = new HashSet<>();
    protected Gson gson;
    private Retrofit retrofit;
    private HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
    private ExecutorService httpExecutorService;
    private boolean autorefresh;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private String apiVersionCode = null;
    // endregion

    // region Authentication fields
    protected volatile String accessToken;
    protected volatile String refreshToken;
    protected String clientId;
    protected String clientSecret;
    // endregion

    // region Resource fields
    private CurrenciesResource currenciesResource;
    private ExchangeRatesResource exchangeRatesResource;
    private PricesResource pricesResource;
    private TimeResource timeResource;
    private UserResource userResource;
    private PaymentMethodResource paymentMethodResource;
    private AuthResource authResource;
    private AccountResource accountResource;
    private AddressResource addressResource;
    private TransactionsResource transactionsResource;
    private BuysResource buysResource;
    private SellsResource sellsResource;
    private DepositsResource depositsResource;
    private WithdrawalsResource withdrawalResource;
    // endregion

    /**
     * Creates an instance of {@link Coinbase} class with default values.
     * <p>
     * Same as:
     * <pre class="prettyprint">
     * CoinbaseBuilder.withPublicDataAccess(context).build();
     * </pre>
     * For the default parameters values refer the API of {@link CoinbaseBuilder} class.
     *
     * @param context application context.
     */
    protected Coinbase(Context context) {
        this.context = context;
        try {
            baseApiUrl = new URL(ApiConstants.BASE_URL_PRODUCTION + "/v2/");
            baseOAuthUrl = new URL(OAUTH_URL);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

        sslContext = CoinbaseSSL.getSSLContext();
        sslSocketFactory = sslContext.getSocketFactory();
        backgroundScheduler = Schedulers.io();
        diskCache = new Cache(context.getCacheDir(), PERSISTENT_CACHE_SIZE);
        gson = createGson();
    }

    /**
     * Creates an instance of {@link Coinbase} class with configuration taken from
     * {@link CoinbaseBuilder}.
     *
     * @param builder an instance of {@link CoinbaseBuilder} to get configuration from.
     */
    @SuppressWarnings("unchecked")
    protected Coinbase(CoinbaseBuilder builder) {
        context = builder.context;

        baseOAuthUrl = builder.baseOauthUrl;
        clientId = builder.clientId;
        clientSecret = builder.clientSecret;
        accessToken = builder.accessToken;
        refreshToken = builder.refreshToken;
        clientSecret = builder.clientSecret;
        clientId = builder.clientId;
        tokenListener = builder.tokenListener;
        autorefresh = builder.autorefresh;
        apiVersionCode = builder.apiVersionCode;

        sslContext = builder.sslContext;
        baseApiUrl = builder.baseApiUrl;

        cryptoAddressNames.addAll(builder.cryptoAddressNames);
        cryptoNetworkNames.addAll(builder.cryptoNetworkNames);

        int cacheSize = builder.cacheSize;
        diskCache = new Cache(context.getCacheDir(), cacheSize > 0 ? cacheSize : PERSISTENT_CACHE_SIZE);

        httpExecutorService = builder.httpExecutorService;
        backgroundScheduler = httpExecutorService == null ? Schedulers.io() : Schedulers.from(httpExecutorService);

        loggingLevel = builder.loggingLevel != null ? builder.loggingLevel : HttpLoggingInterceptor.Level.NONE;

        try {
            if (baseOAuthUrl == null) {
                baseOAuthUrl = new URL(OAUTH_URL);
            }
            if (baseApiUrl == null) {
                baseApiUrl = new URL(BASE_API_URL);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (sslContext != null) {
            sslSocketFactory = sslContext.getSocketFactory();
        } else {
            sslContext = CoinbaseSSL.getSSLContext();
            sslSocketFactory = sslContext.getSocketFactory();
        }
        gson = createGson();
    }

    /**
     * Set {@link ExecutorService} for executing network calls asynchronously. The executor
     * will be used to create a {@link Dispatcher#Dispatcher(ExecutorService) Dispatcher} object.
     * <p>
     * If not provided, {@link Dispatcher#Dispatcher() Dispatcher()} constructor is used.
     *
     * @param executor scheduler to use for background work
     */
    public void setNetworkingExecutor(ExecutorService executor) {
        this.httpExecutorService = executor;
        backgroundScheduler = Schedulers.from(executor);
    }

    /**
     * Provide client id and client secret to allow {@link Coinbase} to authorize under user
     * account via {@link #beginAuthorization(Context, Uri, String...)}.
     * <p>
     * <b>Calling this method disables token auto refresh</b>
     * <p>
     * If new client id and secret passed all previous auth info is wiped out.
     *
     * @param clientId     the client ID you received after registering your application.
     * @param clientSecret the client secret you received after registering your application.
     */
    public void setClientIdAndSecret(@NonNull String clientId,
                                     @NonNull String clientSecret) {
        if (!clientId.equals(this.clientId)) {
            clearAuthInfo();
        }
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Provide access token that will be used in all authentication requests, so user do not
     * have to go through authentication process to get access to account related data.
     * <p>
     * <b>NOTE: don't use this method when auto refresh is ON.</b>
     * <p>
     * This method is meant to be used when auto-refresh is not needed but account related
     * data should still be accessible.
     *
     * @param accessToken the access token to use while getting account related data.
     * @throws IllegalStateException if auto-refresh is enabled and there is no refresh token.
     */
    public void setAccessToken(@Nullable String accessToken) {
        if (autorefresh && TextUtils.isEmpty(refreshToken)) {
            throw new IllegalStateException("Providing access token without refresh token with "
                    + "auto-refresh enabled might cause auto-refresh fails. Please disable auto-refresh"
                    + "or provide auto-refresh token first.");
        }
        this.accessToken = accessToken;
    }

    /**
     * Specify refresh token to be used for refresh current access and refresh tokens whenever
     * access token is expired.
     *
     * @param refreshToken (optional) refresh token to use to exchange for new tokens.
     */
    public void setRefreshToken(@NonNull String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Sets the refresh token {@link TokenListener listener} that will be notified on tokens refreshed
     * or revoked.
     *
     * @param tokenListener token listener.
     */
    public void setRefreshTokenListener(@Nullable TokenListener tokenListener) {
        this.tokenListener = tokenListener;
    }

    /**
     * Enables auto-refresh. In case there are no tokens yet, they will be automatically collected
     * after successful authorization.
     * <p>
     * By default auto-refresh is disabled.
     *
     * @param enabled auto-refresh token enabled.
     */
    public void setAutorefrashedEnabled(boolean enabled) {
        this.autorefresh = enabled;
    }

    /**
     * Gets an {@link #baseOAuthUrl} value.
     *
     * @return {@link #baseOAuthUrl} value.
     */
    public URL getOauthUrl() {
        return baseOAuthUrl;
    }

    @VisibleForTesting
    public String getAccessToken() {
        return accessToken;
    }

    //region Authorization

    /**
     * Returns <code>true</code> if {@link Coinbase} is authorized to perform account related requests.
     * <code>false</code> otherwise.
     *
     * @return is {@link Coinbase} authorized
     */
    public boolean isAuthorized() {
        return accessToken != null || refreshToken != null;
    }

    /**
     * Convenience method for beginning OAuth flow, with {@link #beginAuthorization(Context, AuthorizationRequest)}
     * if you want to user default authorization params.
     *
     * @param context     context to fire up an {@link Intent} to a system to start an
     *                    activity.
     * @param redirectUrl redirect url.
     * @param scopes      authorization scopes.
     * @see AuthorizationRequest
     */
    public void beginAuthorization(@NonNull Context context,
                                   @NonNull Uri redirectUrl,
                                   @NonNull String... scopes) {
        beginAuthorization(context, new AuthorizationRequest(redirectUrl, scopes));
    }

    /**
     * A convenience method to start authorization flow via browser or Android app.
     * <p>
     * Fires up a new {@link Intent} to start an {@link android.app.Activity Activity}
     * with {@link Intent#ACTION_VIEW ACTION_VIEW} for OAuth.
     * <p>
     * If the OAuth flow is successful result is delivered via start {@link android.app.Activity Activity}
     * {@link Intent} with {@link Intent#ACTION_VIEW ACTION_VIEW} and {@link Uri} set to what was
     * passed to {@link AuthorizationRequest#redirectUri AuthorizationRequest.redirectUri}.
     * <p>
     * Temporary access code will be attached to {@link Intent} {@link Uri} as a query. To complete
     * authorization temporary access code should be exchanged for the access and refresh tokens
     * via {@link AuthResource#getTokens(String, String, String, String)} method.
     * <p>
     * Use convenient {@link #completeAuthorization(Intent)} method to get tokens
     * from auth result intent.
     *
     * @param context              context to fire up an {@link Intent} to a system to start an
     *                             activity.
     * @param authorizationRequest {@link AuthorizationRequest request} authorization with options.
     * @see #buildAuthorizationUri(AuthorizationRequest)
     */
    public void beginAuthorization(@NonNull Context context,
                                   @NonNull AuthorizationRequest authorizationRequest) {
        final Uri androidUri = buildAuthorizationUri(authorizationRequest);

        final Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(androidUri);

        context.startActivity(intent);
    }

    /**
     * Builds an Uri to start authorization flow. This is particularly useful when you want to implement
     * OAuth flow inside your application WebView or a custom Chrome tab.
     *
     * @param authorizationRequest {@link AuthorizationRequest request} authorization with options.
     * @return uri for authorization flow.
     * @see #beginAuthorization(Context, AuthorizationRequest)
     */
    public Uri buildAuthorizationUri(@NonNull AuthorizationRequest authorizationRequest) {
        checkClientIdAndSecret();
        setCsrfToken(null); // Reset token to use new one for each session.

        return authorizationRequest
                .getAuthorizationUri(baseOAuthUrl, clientId)
                .buildUpon()
                .appendQueryParameter(ApiConstants.STATE, getCsrfToken())
                .build();
    }

    private void checkClientIdAndSecret() {
        if (TextUtils.isEmpty(clientId) || TextUtils.isEmpty(clientSecret)) {
            throw new IllegalStateException("Client id and client secret should be provided "
                    + "before authorization starts");
        }
    }

    @SuppressLint("ApplySharedPref")
    private void setCsrfToken(@Nullable String token) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_LOGIN_CSRF_TOKEN, token).commit();
    }

    synchronized String getCsrfToken() {
        final SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String loginCsrfToken = preferences.getString(KEY_LOGIN_CSRF_TOKEN, null);
        if (loginCsrfToken == null) {
            final SecureRandom secureRandom = new SecureRandom();
            loginCsrfToken = Integer.toHexString(secureRandom.nextInt());
            setCsrfToken(loginCsrfToken);
        }
        return loginCsrfToken;
    }

    /**
     * Process an OAuth result {@link Intent} and attempt to get an {@link AccessToken}.
     *
     * @param intent OAuth result {@link Intent}.
     * @return {@link ApiCall} with auth data.
     */
    public ApiCall<AccessToken> completeAuthorization(@NonNull Intent intent) {
        return getAuthResource().getTokens(clientId,
                clientSecret,
                getAuthCode(intent),
                getRedirectUrl(intent));
    }

    /**
     * Rx version of {@link #completeAuthorization(Intent)}.
     *
     * @param intent OAuth result {@link Intent}.
     * @return {@link Single} with auth data.
     */
    public Single<AccessToken> completeAuthorizationRx(@NonNull Intent intent) {
        return getAuthResource().getTokensRx(clientId,
                clientSecret,
                getAuthCode(intent),
                getRedirectUrl(intent));
    }

    /**
     * Process an OAuth result and attempt to get an {@link AccessToken}.
     *
     * @param redirectUri a redirectUri parsed from the result uri at the end of oauth
     * @param code the auth code returned from the result uri at the end of oauth
     * @return {@link ApiCall} with auth data.
     */
    public ApiCall<AccessToken> completeAuthorizationWithRedirectAndCode(@NonNull String redirectUri, @NonNull String code) {
        return getAuthResource().getTokens(clientId,
                clientSecret,
                code,
                redirectUri);
    }

    /**
     * Rx version of {@link #completeAuthorizationWithRedirectAndCode(String, String)}.
     *
     * @param redirectUri a redirectUri parsed from the result uri at the end of oauth
     * @param code the auth code returned from the result uri at the end of oauth 
     * @return {@link Single} with auth data.
     */
    public Single<AccessToken> completeAuthorizationWithRedirectAndCodeRx(@NonNull String redirectUri, @NonNull String code) {
        return getAuthResource().getTokensRx(clientId,
                clientSecret,
                code,
                redirectUri);
    }

    private String getRedirectUrl(Intent intent) {
        final Uri resultUri = getIntentUri(intent);
        return getRedirectUrlFromUri(resultUri);
    }

    public String getRedirectUrlFromUri(Uri resultUri) {

        final String csrfToken = resultUri.getQueryParameter(ApiConstants.STATE);
        if (csrfToken == null || !csrfToken.equals(getCsrfToken())) {
            throw new IllegalArgumentException("CSRF Detected!");
        }
        return resultUri.buildUpon().clearQuery().build().toString();
    }

    private String getAuthCode(Intent intent) {
        final Uri resultUri = getIntentUri(intent);
        return getAuthCodeFromUri(resultUri);
    }

    public String getAuthCodeFromUri(Uri resultUri) {
        final String authCode = resultUri.getQueryParameter("code");
        if (authCode == null) {
            final String errorDescription = resultUri.getQueryParameter("error_description");
            throw new IllegalArgumentException(errorDescription != null
                    ? errorDescription
                    : "Invalid auth code!");
        }
        return authCode;
    }

    private Uri getIntentUri(Intent intent) {
        final Uri resultUri = intent.getData();
        if (resultUri == null) {
            throw new IllegalArgumentException("No redirect URI in intent!");
        }
        return resultUri;
    }

    /**
     * Clears current tokens to make a local log out.
     */
    public void logout() {
        clearAuthInfo();
    }

    //endregion

    // region Resources setup

    /**
     * Provides lazily initialized instance of {@link CurrenciesResource}.
     *
     * @return an instance of {@link CurrenciesResource}
     */
    public CurrenciesResource getCurrenciesResource() {
        if (currenciesResource == null) {
            currenciesResource = initResource(CurrenciesResource::new, CurrenciesApi.class, CurrenciesApiRx.class);
        }
        return currenciesResource;
    }

    /**
     * Provides lazily initialized instance of {@link ExchangeRatesResource}.
     *
     * @return an instance of {@link ExchangeRatesResource}
     */
    public ExchangeRatesResource getExchangeRatesResource() {
        if (exchangeRatesResource == null) {
            exchangeRatesResource = initResource(ExchangeRatesResource::new, ExchangeRatesApi.class, ExchangeRatesApiRx.class);
        }
        return exchangeRatesResource;
    }

    /**
     * Provides lazily initialized instance of {@link PricesResource}.
     *
     * @return an instance of {@link PricesResource}
     */
    public PricesResource getPricesResource() {
        if (pricesResource == null) {
            pricesResource = initResource(PricesResource::new, PricesApi.class, PricesApiRx.class);
        }
        return pricesResource;
    }

    /**
     * Provides lazily initialized instance of {@link TimeResource}.
     *
     * @return an instance of {@link TimeResource}
     */
    public TimeResource getTimeResource() {
        if (timeResource == null) {
            timeResource = initResource(TimeResource::new, TimeApi.class, TimeApiRx.class);
        }
        return timeResource;
    }

    /**
     * Provides lazily initialized instance of {@link UserResource}.
     *
     * @return an instance of {@link UserResource}
     */
    public UserResource getUserResource() {
        if (userResource == null) {
            userResource = initResource(UserResource::new, UsersApi.class, UsersApiRx.class);
        }
        return userResource;
    }

    /**
     * Provides lazily initialized instance of {@link AuthResource}.
     *
     * @return an instance of {@link AuthResource}
     */
    public AuthResource getAuthResource() {
        if (authResource == null) {
            authResource = initResource(AuthResource::new, AuthApi.class, AuthApiRx.class);
        }
        return authResource;
    }

    /**
     * Provides lazily initialized instance of {@link AccountResource}.
     *
     * @return an instance of {@link AccountResource}
     */
    public AccountResource getAccountResource() {
        if (accountResource == null) {
            accountResource = initResource(AccountResource::new, AccountsApi.class, AccountsApiRx.class);
        }
        return accountResource;
    }

    /**
     * Provides lazily initialized instance of {@link AddressResource}.
     *
     * @return an instance of {@link AddressResource}
     */
    public AddressResource getAddressResource() {
        if (addressResource == null) {
            addressResource = initResource(AddressResource::new, AddressesApi.class, AddressesApiRx.class);
        }
        return addressResource;
    }

    /**
     * Provides lazily initialized instance of {@link TransactionsResource}.
     *
     * @return an instance of {@link TransactionsResource}
     */
    public TransactionsResource getTransactionsResource() {
        if (transactionsResource == null) {
            transactionsResource = initResource(TransactionsResource::new, TransactionsApi.class, TransactionsApiRx.class);
        }
        return transactionsResource;
    }

    /**
     * Provides lazily initialized instance of {@link BuysResource}.
     *
     * @return an instance of {@link BuysResource}
     */
    public BuysResource getBuysResource() {
        if (buysResource == null) {
            buysResource = initResource(BuysResource::new, TradesApi.class, TradesApiRx.class);
        }
        return buysResource;
    }

    /**
     * Provides lazily initialized instance of {@link SellsResource}.
     *
     * @return an instance of {@link SellsResource}
     */
    public SellsResource getSellsResource() {
        if (sellsResource == null) {
            sellsResource = initResource(SellsResource::new, TradesApi.class, TradesApiRx.class);
        }
        return sellsResource;
    }

    /**
     * Provides lazily initialized instance of {@link DepositsResource}.
     *
     * @return an instance of {@link DepositsResource}
     */
    public DepositsResource getDepositsResource() {
        if (depositsResource == null) {
            depositsResource = initResource(DepositsResource::new, TradesApi.class, TradesApiRx.class);
        }
        return depositsResource;
    }

    /**
     * Provides lazily initialized instance of {@link WithdrawalsResource}.
     *
     * @return an instance of {@link WithdrawalsResource}
     */
    public WithdrawalsResource getWithdrawalsResource() {
        if (withdrawalResource == null) {
            withdrawalResource = initResource(WithdrawalsResource::new, TradesApi.class, TradesApiRx.class);
        }
        return withdrawalResource;
    }

    /**
     * Provides lazily initialized instance of {@link PaymentMethodResource}.
     *
     * @return an instance of {@link PaymentMethodResource}
     */
    public PaymentMethodResource getPaymentMethodResource() {
        if (paymentMethodResource == null) {
            paymentMethodResource = initResource(PaymentMethodResource::new, PaymentMethodsApi.class, PaymentMethodsApiRx.class);
        }
        return paymentMethodResource;
    }

    /**
     * Creates an instance of a specific resource.
     *
     * @param constructor resource constructor.
     * @param api         api interface.
     * @param rxApi       rx api interface.
     * @param <R>         resource type.
     * @param <T>         api interface type.
     * @param <Trx>       rx api interface type.
     * @return newly created resource instance.
     */
    protected <R, T, Trx> R initResource(BiFunction<T, Trx, R> constructor, Class<T> api, Class<Trx> rxApi) {
        try {
            final Retrofit retrofit = getRetrofit();
            return constructor.apply(retrofit.create(api), retrofit.create(rxApi));
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Could not instantiate resource", e);
            throw new IllegalArgumentException(e);
        }
    }

    // endregion

    //region Network setup

    private String getPackageVersionName() {
        final String packageName = context.getPackageName();
        final String versionName = getVersionName() + ApiConstants.PATH_SEPARATOR + getVersionCode();
        return packageName + ApiConstants.PATH_SEPARATOR + versionName;
    }

    /**
     * Determines SDK version name from build config.
     *
     * @return version name.
     */
    protected String getVersionName() {
        return String.valueOf(BuildConfig.VERSION_NAME);
    }

    /**
     * Determines SDK version code from build config.
     *
     * @return version code.
     */
    protected String getVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    /**
     * Determines the api version code
     *
     * @return api version code
     */
    protected String getApiVersionCode() {
        return apiVersionCode == null ? ApiConstants.VERSION : apiVersionCode;
    }

    /**
     * Provides an interceptor to inject Coinbase server requests with headers containing client info.
     *
     * @return client info {@link Interceptor}
     */
    protected Interceptor clientInfoInterceptor() {
        return chain -> chain.proceed(chain
                .request()
                .newBuilder()
                .addHeader(Headers.CB_VERSION, ApiConstants.VERSION)
                .addHeader(Headers.CB_CLIENT, getPackageVersionName())
                .addHeader(Headers.APP_VERSION, getVersionName())
                .addHeader(Headers.APP_BUILD_NUMBER, getVersionCode())
                .addHeader(Headers.ACCEPT_LANGUAGE, Locale.getDefault().getLanguage())
                .build());
    }

    /**
     * Provides an interceptor to inject Coinbase server requests with auth header.
     *
     * @return auth header {@link Interceptor}
     */
    protected Interceptor authHeaderInterceptor() {
        return chain -> {
            final Request.Builder builder = insertAccessToken(chain.request().newBuilder());
            return chain.proceed(builder.build());
        };
    }

    /**
     * Provides an interceptor that saves access and refresh tokens (if {@link #setAutorefrashedEnabled(boolean)
     * allowed}) from response.
     *
     * @return save tokens interceptor.
     */
    protected Interceptor saveAuthTokensInterceptor() {
        return chain -> {
            final Request request = chain.request();
            final Response response = chain.proceed(request);
            final String encodedPath = request.url().encodedPath();

            if (encodedPath.startsWith(ApiConstants.OAuth.TOKEN)) {
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE); // Request the entire body.
                final String responseBody = source.buffer().clone().readUtf8(); // Clone buffer before reading from it.
                if (response.isSuccessful()) {
                    onTokensReceived(responseBody);
                } else {
                    onTokensFail(responseBody);
                }
            }
            if (encodedPath.startsWith(ApiConstants.OAuth.REVOKE) && response.isSuccessful()) {
                onTokenRevoked();
            }
            return response;
        };
    }

    /**
     * Provides an interceptor that refresh tokens if {@link #setAutorefrashedEnabled(boolean)
     * allowed}.
     *
     * @return tokens auto-refresh interceptor.
     */
    protected Interceptor tokenRefreshInterceptor() {
        return chain -> {
            final Request request = chain.request();
            final Request.Builder builder = request.newBuilder();
            final Response response = chain.proceed(request);
            if (shouldDoAutoRefresh(request, response)) {
                String originalRefreshToken = refreshToken;
                doRefreshToken(originalRefreshToken);
                // if token is refreshed successfully redo request
                if (!TextUtils.isEmpty(refreshToken) && !refreshToken.equals(originalRefreshToken)) {
                    insertAccessToken(builder);
                    return chain.proceed(builder.build());
                }
            }
            return response;
        };
    }

    /**
     * Provides an interceptor that converts Coinbase server error response to {@link CoinbaseException}
     * or {@link CoinbaseOAuthException} with parsed error body.
     *
     * @return error handling interceptor.
     */
    protected Interceptor errorHandlingInterceptor() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (!response.isSuccessful() && !shouldDoAutoRefresh(request, response)) {
                if (request.url().pathSegments().contains(OAUTH)) {
                    OAuthError errorResponse = gson.fromJson(response.body().string(), OAuthError.class);
                    throw new CoinbaseOAuthException(errorResponse);
                } else {
                    CoinbaseErrorResponse errorResponse = gson.fromJson(response.body().string(), CoinbaseErrorResponse.class);
                    throw new CoinbaseException(errorResponse.getErrors());
                }
            }
            return response;
        };
    }

    private boolean shouldDoAutoRefresh(Request request, Response response) {
        return autorefresh && response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
                && !request.url().pathSegments().contains(OAUTH)
                && !TextUtils.isEmpty(refreshToken);
    }

    private Request.Builder insertAccessToken(final Request.Builder builder) {
        if (accessToken != null) {
            builder.header(Headers.AUTHORIZATION, "Bearer " + accessToken);
        }
        return builder;
    }

    private synchronized void doRefreshToken(String originalRefreshToken) throws IOException {
        if (originalRefreshToken.equals(refreshToken)) {
            try {
                getAuthResource().refreshTokens(clientId, clientSecret, refreshToken).execute();
            } catch (CoinbaseOAuthException e) {
                Log.w(LOG_TAG, "Could not refresh token", e);
            }
        }
    }

    private void onTokensReceived(String responseBody) {
        AccessToken token = gson.fromJson(responseBody, AccessToken.class);
        accessToken = token.getAccessToken();
        if (autorefresh) {
            refreshToken = token.getRefreshToken();
            if (tokenListener != null) {
                uiHandler.post(() -> tokenListener.onNewTokensAvailable(token));
            }
        }
    }

    private void onTokensFail(String responseBody) {
        if (tokenListener != null) {
            OAuthError errorResponse = gson.fromJson(responseBody, OAuthError.class);
            uiHandler.post(() -> tokenListener.onRefreshFailed(new CoinbaseOAuthException(errorResponse)));
        }
        clearAuthInfo();
    }

    private void onTokenRevoked() {
        if (tokenListener != null) {
            uiHandler.post(() -> tokenListener.onTokenRevoked());
        }
        // If revoke token successful - invalidate auth info.
        // SDK client should logout.
        clearAuthInfo();
    }

    private void clearAuthInfo() {
        accessToken = null;
        refreshToken = null;
    }

    /**
     * Interceptor for network sniffing, override this to add network sniffing
     *
     * @return a network {@link Interceptor}
     */
    protected Interceptor networkSniffingInterceptor() {
        return chain -> chain.proceed(chain.request());
    }

    /**
     * Interceptor for logging, override this to add logging
     *
     * @return {@link Interceptor} for logging
     */
    protected Interceptor loggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    private OkHttpClient.Builder generateClientBuilder(SSLContext sslContext) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (sslContext != null) {
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }

        // Set custom executor (e.g. for testing).
        if (httpExecutorService != null) {
            builder.dispatcher(new Dispatcher(httpExecutorService));
        }

        // Disable SPDY, causes issues on some Android versions
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));

        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        // Add interceptors.
        builder.addInterceptor(tokenRefreshInterceptor())
                .addInterceptor(errorHandlingInterceptor())
                .addInterceptor(saveAuthTokensInterceptor())
                .addInterceptor(loggingInterceptor())
                .addInterceptor(authHeaderInterceptor())
                .addInterceptor(clientInfoInterceptor())
                .addNetworkInterceptor(networkSniffingInterceptor());
        return builder;
    }

    /**
     * Provides configured {@link Retrofit} instance to initiate API interfaces.
     *
     * @return {@link Retrofit} instance
     */
    public Retrofit getRetrofit() {
        if (retrofit != null) {
            return retrofit;
        }

        final OkHttpClient okHttpClient = generateClientBuilder(sslContext)
                .cache(diskCache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl.toString())
                .client(okHttpClient)
                .addCallAdapterFactory(ApiCallAdapterFactory.create())
                .addCallAdapterFactory(backgroundScheduler == null
                        ? RxJava2CallAdapterFactory.create()
                        : RxJava2CallAdapterFactory.createWithScheduler(backgroundScheduler)
                )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @NonNull
    public static GsonBuilder createGsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new Currency.CurrencyDeserializationFactory())
                .registerTypeAdapter(Trade.class, new TradesDeserializer())
                .registerTypeAdapter(PlaceTradeOrderBody.class, new PlaceTradeOrderBodySerializer())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    /**
     * Hook method to configure {@link GsonBuilder} that will be used for parsing responses json.
     *
     * @param gsonBuilder {@link GsonBuilder builder} to configure
     */
    protected void configureGsonBuilder(GsonBuilder gsonBuilder) {
        // This space is left for rent.
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = createGsonBuilder()
                .registerTypeAdapter(DynamicResource.class, initDynamicResourceDeserializer());

        configureGsonBuilder(gsonBuilder);

        return gsonBuilder.create();
    }

    @NonNull
    private DynamicResourceDeserializer initDynamicResourceDeserializer() {
        final DynamicResourceDeserializer deserializer = new DynamicResourceDeserializer();
        // Add crypto currencies addresses.

        // Base crypto currencies:
        deserializer.addTypeMapping("bitcoin_address", CryptoAddress.class);
        deserializer.addTypeMapping("bitcoin_cash_address", CryptoAddress.class);
        deserializer.addTypeMapping("litecoin_address", CryptoAddress.class);
        deserializer.addTypeMapping("ethereum_address", CryptoAddress.class);

        // Custom currencies:
        for (String name : cryptoAddressNames) {
            deserializer.addTypeMapping(name, CryptoAddress.class);
        }

        // Add crypto networks:
        deserializer.addTypeMapping("bitcoin_network", CryptoNetwork.class);
        deserializer.addTypeMapping("bitcoin_cash_network", CryptoNetwork.class);
        deserializer.addTypeMapping("litecoin_network", CryptoNetwork.class);
        deserializer.addTypeMapping("ethereum_network", CryptoNetwork.class);

        // Custom networks:
        for (String name : cryptoNetworkNames) {
            deserializer.addTypeMapping(name, CryptoNetwork.class);
        }

        return deserializer;
    }

    //endregion

    /**
     * Listener of events related to token refresh
     */
    public interface TokenListener {

        /**
         * Invoked once tokens are updated. After callback method invoked, previous refresh and
         * access tokens become invalid.
         *
         * @param accessToken new tokens data
         * @see AccessToken
         */
        void onNewTokensAvailable(AccessToken accessToken);

        /**
         * Invoked if current access token could not be changed to a new one.
         *
         * @param cause the error received from server for refresh token request.
         */
        void onRefreshFailed(CoinbaseOAuthException cause);

        /**
         * Invoked if access token is revoked. Refresh token also become invalid.
         */
        void onTokenRevoked();
    }

}
