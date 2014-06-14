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
    private WebTarget _account_specific_target = null;

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
	
	if (builder.acct_id != null) {
	    _account_specific_target = _base_target.queryParam("account_id", builder.acct_id);
	} else {
	    _account_specific_target = _base_target;
	}
	
	

    }

    public User getUser() {
	WebTarget usersTarget = _base_target.path("users");
	Response response = get(usersTarget);
	return response.getUsers().get(0).getUser();
    }
    
    public Transaction getTransaction(String id) {
	WebTarget txTarget = _account_specific_target.path("transactions/" + id);
	Response response = get(txTarget);
	return response.getTransaction();
    }
    
    public Transaction requestMoney(Transaction transaction) {

	WebTarget requestMoneyTarget = _account_specific_target.path("transactions/request_money");

	serializeAmount(transaction);
	
	Request request = new Request();
	request.setTransaction(transaction);

	Response response = post(requestMoneyTarget, request);
	return response.getTransaction();

    }

    public Transaction sendMoney(Transaction transaction) {
	WebTarget requestMoneyTarget = _account_specific_target.path("transactions/send_money");
	
	serializeAmount(transaction);
	
	Request request = new Request();
	request.setTransaction(transaction);
	
	Response response = post(requestMoneyTarget, request);
	return response.getTransaction();
    }

    public Response getTransactions(int page) {
	WebTarget transactionsTarget = _account_specific_target.path("transactions").queryParam("page", page);
	return get(transactionsTarget);
    }

    public Response getTransactions() {
	return getTransactions(1);
    }

    public void resendRequest(String id) {
	WebTarget resendRequestTarget = _account_specific_target.path("transactions/" + id + "/resend_request");
	put(resendRequestTarget, new Request());
    }

    public void deleteRequest(String id) {
	WebTarget deleteRequestTarget = _account_specific_target.path("transactions/" + id + "/cancel_request");
	delete(deleteRequestTarget);
    }

    public Transaction completeRequest(String id) {
	WebTarget completeRequestTarget = _account_specific_target.path("transactions/" + id + "/complete_request");
	Response response = put(completeRequestTarget, new Request());
	return response.getTransaction();
    }

    public Response getTransfers(int page) {
	WebTarget transfersTarget = _account_specific_target.path("transfers").queryParam("page", page);
	return get(transfersTarget);
    }

    public Response getTransfers() {
	return getTransfers(1);
    }

    private static Response get(WebTarget target) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
    }
    
    private static Response post(WebTarget target, Object entity) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).post(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

    private static Response put(WebTarget target, Object entity) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).put(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

    private static Response delete(WebTarget target) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).delete(Response.class);
    }

    private static void serializeAmount(Transaction transaction) {
	// Massage amount
	Money amount = transaction.getAmount();
	transaction.setAmount(null);

	transaction.setAmountString(amount.getAmount().toPlainString());
	transaction.setAmountCurrencyIso(amount.getCurrencyUnit().getCurrencyCode());
    }

}
