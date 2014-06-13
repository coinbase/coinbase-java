package com.coinbase.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.joda.money.Money;

import com.coinbase.api.auth.HmacClientFilter;
import com.coinbase.api.entity.Request;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.User;

class CoinbaseImpl implements Coinbase {

    private static final String API_BASE_URL = "https://coinbase.com/api/v1/";
    private Client _client = null;
    private WebTarget _base_target = null;

    CoinbaseImpl(CoinbaseBuilder builder) throws Exception {

	ClientConfig clientConfig = new ClientConfig();

	clientConfig.register(ObjectMapperProvider.class);
	clientConfig.register(JacksonFeature.class);

	if (builder.access_token != null) {
	    clientConfig.register(OAuth2ClientSupport.feature(builder.access_token));
	} else if (builder.api_key != null && builder.api_secret != null) {
	    clientConfig.property(HmacClientFilter.API_KEY_NAME, builder.api_key);
	    clientConfig.property(HmacClientFilter.API_SECRET_NAME, builder.api_secret);
	    clientConfig.register(HmacClientFilter.class);
	}

	_client = ClientBuilder.newBuilder().withConfig(clientConfig)
		.sslContext(CoinbaseSSL.context()).build();

	_base_target = _client.target(API_BASE_URL);

    }

    public User getUser() {
	WebTarget usersTarget = _base_target.path("users");
	Response response = get(usersTarget);
	return response.getUsers().get(0).getUser();
    }
    
    public Transaction getTransaction(String id) {
	WebTarget txTarget = _base_target.path("transactions/" + id);
	Response response = get(txTarget);
	return response.getTransaction();
    }
    
    public Transaction requestMoney(Transaction transaction) {

	WebTarget requestMoneyTarget = _base_target.path("transactions/request_money");

	// Massage amount
	Money amount = transaction.getAmount();
	transaction.setAmount(null);

	transaction.setAmountString(amount.getAmount().toPlainString());
	transaction.setAmountCurrencyIso(amount.getCurrencyUnit().getCurrencyCode());
	
	Request request = new Request();
	request.setTransaction(transaction);

	Response response = post(requestMoneyTarget, request);
	return response.getTransaction();

    }

    private Response get(WebTarget target) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
    }
    
    private Response post(WebTarget target, Object entity) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).post(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

}
