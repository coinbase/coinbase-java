package com.coinbase.api;

public class CoinbaseBuilder {

    String access_token;
    String api_key;
    String api_secret;
    String acct_id;

    /**
     * Build a new Coinbase client object with the specified options
     * 
     * @return a new Coinbase client object
     */
    public Coinbase build() {
	return new CoinbaseImpl(this);
    }

    /**
     * Specify an access token to be used for authenticated requests
     * 
     * Coinbase client objects built using an access token are thread-safe
     * 
     * @param access_token the OAuth access token
     * 
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withAccessToken(String access_token) {
	this.access_token = access_token;
	return this;
    }

    /**
     * Specify the HMAC api key and secret to be used for authenticated requests
     * 
     * Having more than one client with the same api/secret globally is unsupported
     * and will result in sporadic auth errors as the nonce is calculated from the system time.
     * 
     * @param api_key the HMAC API Key
     * @param api_secret the HMAC API Secret
     * 
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
     * 
     * @return this CoinbaseBuilder object
     */
    public CoinbaseBuilder withAccountId(String acct_id) {
	this.acct_id = acct_id;
	return this;
    }

}
