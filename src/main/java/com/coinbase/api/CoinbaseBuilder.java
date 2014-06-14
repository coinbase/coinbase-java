package com.coinbase.api;

public class CoinbaseBuilder {

    String access_token;
    String api_key;
    String api_secret;
    String acct_id;

    public Coinbase build() throws Exception {
	return new CoinbaseImpl(this);
    }

    public CoinbaseBuilder withAccessToken(String access_token) {
	this.access_token = access_token;
	return this;
    }

    public CoinbaseBuilder withApiKey(String api_key, String api_secret) {
	this.api_key = api_key;
	this.api_secret = api_secret;
	return this;
    }

    public CoinbaseBuilder withAccountId(String acct_id) {
	this.acct_id = acct_id;
	return this;
    }

}
