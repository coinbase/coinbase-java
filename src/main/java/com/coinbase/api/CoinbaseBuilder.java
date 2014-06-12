package com.coinbase.api;

public class CoinbaseBuilder {

    String access_token;

    public Coinbase build() throws Exception {
	return new CoinbaseImpl(this);
    }

    public CoinbaseBuilder withAccessToken(String access_token) {
	this.access_token = access_token;
	return this;
    }

}
