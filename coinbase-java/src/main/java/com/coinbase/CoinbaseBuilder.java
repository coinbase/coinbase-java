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

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.Coinbase.TokenListener;
import com.coinbase.resources.transactions.Transaction;
import com.coinbase.resources.transactions.entities.CryptoAddress;
import com.coinbase.resources.transactions.entities.CryptoNetwork;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLContext;

import okhttp3.Dispatcher;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Builder class for {@link Coinbase} objects.
 * <p>
 * Example:
 * <pre class="prettyprint">
 * CoinbaseBuilder.withPublicDataAccess(context).build();
 * </pre>
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public class CoinbaseBuilder<T extends CoinbaseBuilder<T>> {

    final Context context;

    String accessToken;
    String clientId;
    String clientSecret;
    String refreshToken;
    TokenListener tokenListener;
    boolean autorefresh;

    SSLContext sslContext;
    URL baseOauthUrl;
    URL baseApiUrl;
    int cacheSize;
    ExecutorService httpExecutorService;
    HttpLoggingInterceptor.Level loggingLevel;
    Set<String> cryptoAddressNames = new HashSet<>();
    Set<String> cryptoNetworkNames = new HashSet<>();

    //region Authentication set up

    /**
     * This configuration method is meant to be used in case you only need data from
     * {@link com.coinbase.resources.currencies.CurrenciesResource CurrenciesResource},
     * {@link com.coinbase.resources.rates.ExchangeRatesResource ExchangeRatesResource},
     * {@link com.coinbase.resources.time.TimeResource TimeResource} and
     * {@link com.coinbase.resources.prices.PricesResource PricesResource}.
     *
     * @param context application context for {@link Coinbase}
     * @return new CoinbaseBuilder
     * @see #withClientIdAndSecret(Context, String, String)
     * @see #withAccessToken(Context, String, String, String)
     * @see #withTokenAutoRefresh(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String, String, String, Coinbase.TokenListener)
     */
    public static CoinbaseBuilder withPublicDataAccess(@NonNull Context context) {
        return new CoinbaseBuilder(context);
    }

    /**
     * Provide client id and client secret to allow {@link Coinbase} to authorize under user
     * account via {@link Coinbase#beginAuthorization(Context, Uri, String...)}.
     * <p>
     * This configuration method is meant to be used in case you do not want the auto refresh
     * functionality of {@link Coinbase} and do not have an access token.
     *
     * @param context      application context for {@link Coinbase}
     * @param clientId     the client ID you received after registering your application.
     * @param clientSecret the client secret you received after registering your application.
     * @return this CoinbaseBuilder object
     * @see #withPublicDataAccess(Context)
     * @see #withAccessToken(Context, String, String, String)
     * @see #withTokenAutoRefresh(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String, String, String, Coinbase.TokenListener)
     */
    public static CoinbaseBuilder withClientIdAndSecret(@NonNull Context context,
                                                        @NonNull String clientId,
                                                        @NonNull String clientSecret) {
        return new CoinbaseBuilder(context)
                .clientIdAndSecret(clientId, clientSecret);
    }

    /**
     * Specify an access token to be used for authenticated requests. You are also required to
     * provide client id and client secret that were used to get access token.
     * <p>
     * Client id and client secret and access token can be provided directly to {@link Coinbase} object with
     * {@link Coinbase#setAccessToken(String) setAccessToken()}.
     * <p>
     * This configuration method is meant to be used in case you do not want the auto refresh
     * functionality of {@link Coinbase} and already have an access token, so user do not have
     * to go through authentication process to get access to account related data.
     * <p>
     * <b>Be aware that after access token is expired all calls that require authorization will
     * fail with {@link com.coinbase.errors.CoinbaseException CoinbaseException}.</b>
     *
     * @param context      application context for {@link Coinbase}
     * @param clientId     the client ID you received after registering your application.
     * @param clientSecret the client secret you received after registering your application.
     * @param accessToken  the access token to use while getting getting account related data.
     * @return new CoinbaseBuilder
     * @see #withPublicDataAccess(Context)
     * @see #withClientIdAndSecret(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String, String, String, Coinbase.TokenListener)
     */
    public static CoinbaseBuilder withAccessToken(@NonNull Context context,
                                                  @NonNull String clientId,
                                                  @NonNull String clientSecret,
                                                  @Nullable String accessToken) {
        return withClientIdAndSecret(context, clientId, clientSecret)
                .accessToken(accessToken);
    }

    /**
     * Has the same effect as invocation of {@link #withTokenAutoRefresh(Context, String, String,
     * String, String, Coinbase.TokenListener) withTokenAutoRefresh(clientId, clientSecret, null,
     * null, null)}.
     * <p>
     * This configuration method is meant to be used in case you want the auto refresh
     * functionality of {@link Coinbase} but do not want to store tokens and prefer user to login
     * for every application session.
     *
     * @param context      application context for {@link Coinbase}
     * @param clientId     the client ID you received after registering your application.
     * @param clientSecret the client secret you received after registering your application.
     * @return new CoinbaseBuilder
     * @see #withTokenAutoRefresh(Context, String, String, String, String, Coinbase.TokenListener)
     */
    public static CoinbaseBuilder withTokenAutoRefresh(@NonNull Context context,
                                                       @NonNull String clientId,
                                                       @NonNull String clientSecret) {
        return withTokenAutoRefresh(context, clientId, clientSecret, null, null, null);
    }

    /**
     * Specify an access and refresh tokens to be used for authenticated requests. You are also
     * required to provide client id and client secret that were used to get tokens.
     * <p>
     * Calling this method will setup {@link Coinbase} object to use auto - refresh mechanism, so
     * whenever access token is expired {@link Coinbase} will try to refresh it using refresh
     * token.
     * <p>
     * Every time tokens are refreshed, previous access and refresh tokens become invalid. Use
     * {@link com.coinbase.Coinbase.TokenListener#onNewTokensAvailable(com.coinbase.resources.auth.AccessToken)
     * TokenListener.onNewTokensAvailable()} callback to get and save new access and refresh tokens.
     * If token auto refresh fails, callback
     * {@link com.coinbase.Coinbase.TokenListener#onNewTokensAvailable(com.coinbase.resources.auth.AccessToken)
     * onNewTokensAvailable()} is invoked, also original call will fail with
     * {@link com.coinbase.errors.CoinbaseException CoinbaseException}. When that happens,
     * new tokens can be received via authentication flow (new tokens are automatically applied for
     * auto - refresh).
     * <p>
     * This method also fits in to scenario when you do not have tokens, but still want to use
     * auto - refresh functionality. Just pass {@code null} value instead of access and refresh
     * tokens.
     * <p>
     * <b>Refresh and access tokens are not stored in persistance storage, so it is library consumer
     * responsibility to save it across sessions.</b>
     *
     * @param context       application context for {@link Coinbase}
     * @param clientId      the client ID you received after registering your application.
     * @param clientSecret  the client secret you received after registering your application.
     * @param accessToken   (optional) access token to use while getting getting account related data.
     * @param refreshToken  (optional) refresh token to use to exchange for new tokens.
     * @param tokenListener (optional) tokens updates events listener.
     * @return new CoinbaseBuilder
     * @see #withClientIdAndSecret(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String)
     * @see #withTokenAutoRefresh(Context, String, String, String, String, Coinbase.TokenListener)
     */
    public static CoinbaseBuilder withTokenAutoRefresh(@NonNull Context context,
                                                       @NonNull String clientId,
                                                       @NonNull String clientSecret,
                                                       @Nullable String accessToken,
                                                       @Nullable String refreshToken,
                                                       @Nullable Coinbase.TokenListener tokenListener) {
        CoinbaseBuilder coinbaseBuilder = withAccessToken(context, clientId, clientSecret, accessToken)
                .refreshToken(refreshToken)
                .tokenListener(tokenListener);
        coinbaseBuilder.autorefresh = true;
        return coinbaseBuilder;
    }
    //endregion

    protected CoinbaseBuilder(Context context) {
        this.context = context;
    }

    /**
     * Build a new {@link Coinbase} client object with specified parameters.
     *
     * @return a new {@link Coinbase} object.
     */
    public Coinbase build() {
        return new Coinbase(this);
    }

    /**
     * Sets {@link HttpLoggingInterceptor.Level}. Determines loging level of {@link Coinbase}.
     * <p>
     * If not provided, logging level is set to {@link HttpLoggingInterceptor.Level#NONE Level.NONE}.
     *
     * @param level The logging level.
     * @return this {@link CoinbaseBuilder} object.
     * @see HttpLoggingInterceptor.Level
     */
    public CoinbaseBuilder withLoggingLevel(HttpLoggingInterceptor.Level level) {
        this.loggingLevel = level;
        return getThis();
    }

    /**
     * Set the ssl context to be used when creating SSL sockets.
     * <p>
     * If not provided an instance of {@link CoinbaseSSL} will be used.
     *
     * @param sslContext the SSLContext to be used.
     * @return this CoinbaseBuilder object.
     */
    public T withSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return getThis();
    }

    /**
     * Set the base URL to be used for API requests.
     * <p>
     * If not provided, this is {@link Coinbase#BASE_API_URL}.
     *
     * @param baseApiUrl the base URL to use for API requests. Must return an instance of
     *                   javax.net.ssl.HttpsURLConnection on openConnection.
     * @return this CoinbaseBuilder object.
     */
    public T withBaseApiURL(URL baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
        return getThis();
    }

    /**
     * Set the base URL to be used for OAuth requests.
     * <p>
     * If not provided, this is {@link Coinbase#OAUTH_URL}.
     *
     * @param baseOauthUrl the base URL to use for OAuth requests. Must return an instance of
     *                     javax.net.ssl.HttpsURLConnection on openConnection.
     * @return this CoinbaseBuilder object.
     */
    public T withBaseOAuthURL(URL baseOauthUrl) {
        this.baseOauthUrl = baseOauthUrl;
        return getThis();
    }

    /**
     * Set {@link ExecutorService} for executing network calls asynchronously. The executor
     * will be used to create {@link Dispatcher#Dispatcher(ExecutorService) Dispatche} object.
     * <p>
     * If not provided, {@link Dispatcher#Dispatcher() Dispatcher()} constructor is used.
     *
     * @param executorService service for executing network calls.
     * @return this CoinbaseBuilder object.
     */
    public T withHttpExecutorService(ExecutorService executorService) {
        this.httpExecutorService = executorService;
        return getThis();
    }

    /**
     * Set the cache size to use for the lru in-memory cache.
     * <p>
     * If not provided, the size is set to 500kb. In practice you shouldn't need more than 250kb.
     *
     * @param cacheSize an int representing the size in BYTES.
     * @return this CoinbaseBuilder object.
     */
    public T cacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return getThis();
    }

    /**
     * Add additional cryptocurrencies address names. These names would be used to parse json objects
     * as java {@link CryptoAddress CryptoAddress} objects
     * as {@link Transaction#to Transaction.to} or
     * {@link Transaction#from Transaction.from} fields.
     * <p>
     * Default address names are "bitcoin_address", "bitcoin_cash_address", "litecoin_address"
     * and "ethereum_address".
     *
     * @param names cryptocurrencies address names.
     * @return this CoinbaseBuilder object.
     * @see CryptoAddress CryptoAddress
     */
    public CoinbaseBuilder withCryptCurrencyAddressNames(String... names) {
        cryptoAddressNames.addAll(Arrays.asList(names));
        return getThis();
    }

    /**
     * Add additional cryptocurrencies network names. These names would be used to parse json objects
     * as java {@link CryptoNetwork CryptoNetwork} objects
     * as {@link Transaction#to Transaction.to} or
     * {@link Transaction#from Transaction.from} fields.
     * <p>
     * Default address names are "bitcoin_network", "bitcoin_cash_network", "litecoin_network"
     * and "ethereum_network".
     *
     * @param networks cryptocurrencies address names.
     * @return this CoinbaseBuilder object.
     * @see CryptoNetwork CryptoNetwork
     */
    public T withCryptoNetworkNames(String... networks) {
        cryptoNetworkNames.addAll(Arrays.asList(networks));
        return getThis();
    }

    protected T accessToken(String accessToken) {
        this.accessToken = accessToken;
        return getThis();
    }

    protected T clientIdAndSecret(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        return getThis();
    }

    protected T refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return getThis();
    }

    protected T tokenListener(Coinbase.TokenListener tokenListener) {
        this.tokenListener = tokenListener;
        return getThis();
    }

    @SuppressWarnings("unchecked")
    protected T getThis() {
        return (T) this;
    }
}
