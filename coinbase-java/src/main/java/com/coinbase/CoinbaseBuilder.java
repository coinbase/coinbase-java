package com.coinbase;

import java.net.URL;

import javax.net.ssl.SSLContext;

import rx.Scheduler;

public class CoinbaseBuilder {

    String access_token;
    String api_key;
    String api_secret;
    String acct_id;
    SSLContext ssl_context;
    URL base_oauth_url;
    URL base_api_url;
    com.coinbase.CallbackVerifier callback_verifier;
    Scheduler scheduler;
    int cacheSize;

    /**
     * Build a new Coinbase client object with the specified options
     *
     * @return a new Coinbase client object
     */
    public Coinbase build() {
        return new Coinbase(this);
    }

    /**
     * Specify an access token to be used for authenticated requests
     * <p>
     * Coinbase client objects built using an access token are thread-safe
     *
     * @param access_token the OAuth access token
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withAccessToken(String access_token) {
        this.access_token = access_token;
        return this;
    }

    /**
     * Specify the HMAC api key and secret to be used for authenticated requests
     * <p>
     * Having more than one client with the same api/secret globally is unsupported
     * and will result in sporadic auth errors as the nonce is calculated from the system time.
     *
     * @param api_key    the HMAC API Key
     * @param api_secret the HMAC API Secret
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withApiKey(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
        return this;
    }

    /**
     * Specify the account id to be used for account-specific requests
     *
     * @param acct_id the account id
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withAccountId(String acct_id) {
        this.acct_id = acct_id;
        return this;
    }

    /**
     * Specify the verifier used to verify merchant callbacks
     *
     * @param callback_verifier
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withCallbackVerifier(CallbackVerifier callback_verifier) {
        this.callback_verifier = callback_verifier;
        return this;
    }

    /**
     * Specify the ssl context to be used when creating SSL sockets
     *
     * @param ssl_context the SSLContext to be used
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withSSLContext(SSLContext ssl_context) {
        this.ssl_context = ssl_context;
        return this;
    }

    /**
     * Specify the base URL to be used for API requests
     * <p>
     * By default, this is 'https://coinbase.com/api/v1/'
     *
     * @param base_api_url the base URL to use for API requests. Must return an instance of javax.net.ssl.HttpsURLConnection on openConnection.
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withBaseApiURL(URL base_api_url) {
        this.base_api_url = base_api_url;
        return this;
    }

    /**
     * Specify the base URL to be used for OAuth requests
     * <p>
     * By default, this is 'https://coinbase.com/oauth/'
     *
     * @param base_oauth_url the base URL to use for OAuth requests. Must return an instance of javax.net.ssl.HttpsURLConnection on openConnection.
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withBaseOAuthURL(URL base_oauth_url) {
        this.base_oauth_url = base_oauth_url;
        return this;
    }

    /**
     * Optional - specify the rx scheduler to run the subscriber (i.e. network io) requests on.
     * If you don't specify one, it's up to you to call subscribeOn with the scheduler you'd like to use.
     * <p>
     * By default, this is 'https://coinbase.com/oauth/'
     *
     * @param scheduler the rx.Scheduler to run the background requests on.
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * Optional - specify the cache size to use for the lru in-memory cache
     * If you don't specify one, the default cache size of 500kb will be used. In practice you shouldn't need more than 250kb.
     * @param cacheSize an int representing the size in BYTES
     *
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder cacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }
}
