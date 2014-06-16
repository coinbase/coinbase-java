package com.coinbase.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.coinbase.api.auth.HmacClientFilter;
import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.Request;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnknownAccount;

class CoinbaseImpl implements Coinbase {

    private static final String API_BASE_URL = "https://coinbase.com/api/v1/";
    private Client _client = null;
    private String _account_id = null;
    private WebTarget _base_target = null;
    private WebTarget _account_specific_target = null;
    private WebTarget _authenticated_target = null;

    CoinbaseImpl(CoinbaseBuilder builder) throws Exception {

	ClientConfig clientConfig = new ClientConfig();

	clientConfig.register(ObjectMapperProvider.class);
	clientConfig.register(JacksonFeature.class);

	_client = ClientBuilder.newBuilder().withConfig(clientConfig)
		.sslContext(CoinbaseSSL.context()).build();

	_base_target = _client.target(API_BASE_URL);

	_authenticated_target = _client.target(API_BASE_URL);
	if (builder.access_token != null) {
	    _authenticated_target.register(OAuth2ClientSupport.feature(builder.access_token));
	} else if (builder.api_key != null && builder.api_secret != null) {
	    _authenticated_target.property(HmacClientFilter.API_KEY_NAME, builder.api_key);
	    _authenticated_target.property(HmacClientFilter.API_SECRET_NAME, builder.api_secret);
	    _authenticated_target.register(HmacClientFilter.class);
	}

	if (builder.acct_id != null) {
	    _account_id = builder.acct_id;
	    _account_specific_target = _authenticated_target.queryParam("account_id", builder.acct_id);
	} else {
	    _account_specific_target = _authenticated_target;
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
    
    public Transaction requestMoney(Transaction transaction) throws CoinbaseException {

	WebTarget requestMoneyTarget = _authenticated_target.path("transactions/request_money");

	serializeAmount(transaction);
	
	Request request = newRequest();
	request.setTransaction(transaction);

	Response response = post(requestMoneyTarget, request);
	return response.getTransaction();

    }

    public Transaction sendMoney(Transaction transaction) throws CoinbaseException {
	WebTarget sendMoneyTarget = _authenticated_target.path("transactions/send_money");
	
	serializeAmount(transaction);
	
	Request request = newRequest();
	request.setTransaction(transaction);
	
	Response response = post(sendMoneyTarget, request);
	return response.getTransaction();
    }

    public Response getTransactions(int page) {
	WebTarget transactionsTarget = _account_specific_target.path("transactions").queryParam("page", page);
	return get(transactionsTarget);
    }

    public Response getTransactions() {
	return getTransactions(1);
    }

    public void resendRequest(String id) throws CoinbaseException {
	WebTarget resendRequestTarget = _authenticated_target.path("transactions/" + id + "/resend_request");
	put(resendRequestTarget, newRequest());
    }

    public void deleteRequest(String id) throws CoinbaseException {
	WebTarget deleteRequestTarget = _authenticated_target.path("transactions/" + id + "/cancel_request");
	delete(deleteRequestTarget);
    }

    public Transaction completeRequest(String id) throws CoinbaseException {
	WebTarget completeRequestTarget = _authenticated_target.path("transactions/" + id + "/complete_request");
	Response response = put(completeRequestTarget, newRequest());
	return response.getTransaction();
    }

    public Response getTransfers(int page) {
	WebTarget transfersTarget = _account_specific_target.path("transfers").queryParam("page", page);
	return get(transfersTarget);
    }

    public Response getTransfers() {
	return getTransfers(1);
    }

    public Response getAddresses(int page) {
	WebTarget addressesTarget = _account_specific_target.path("addresses").queryParam("page", page);
	return get(addressesTarget);
    }

    public Response getAddresses() {
	return getAddresses(1);
    }

    public Money getSpotPrice(CurrencyUnit currency) {
	WebTarget spotPriceTarget = _base_target.path("prices/spot_rate").queryParam("currency", currency.getCurrencyCode());
	return spotPriceTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Money.class);
    }

    public Quote getBuyQuote(Money btcAmount) {
	WebTarget buyPriceTarget = _base_target.path("prices/buy").queryParam("qty", btcAmount.getAmount().toPlainString());
	return buyPriceTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Quote.class);
    }

    public Quote getSellQuote(Money btcAmount) {
	WebTarget buyPriceTarget = _base_target.path("prices/sell").queryParam("qty", btcAmount.getAmount().toPlainString());
	return buyPriceTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Quote.class);
    }

    public Response getAccounts() {
	return getAccounts(1, 25, false);
    }

    public Response getAccounts(int page) {
	return getAccounts(page, 25, false);
    }

    public Response getAccounts(int page, int limit) {
	return getAccounts(page, limit, false);
    }

    public Response getAccounts(int page, int limit, boolean includeInactive) {
	WebTarget addressesTarget =
		_authenticated_target
		.path("accounts")
		.queryParam("page", page)
		.queryParam("limit", limit)
		.queryParam("all_accounts", includeInactive);
		
	return get(addressesTarget);
    }

    public Money getBalance(String accountId) {
	WebTarget balanceTarget = _authenticated_target.path("accounts/" + accountId + "/balance");
	return balanceTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Money.class);
    }

    public void setPrimaryAccount(String accountId) throws CoinbaseException {
	WebTarget setPrimaryTarget = _authenticated_target.path("accounts/" + accountId + "/primary");
	post(setPrimaryTarget, new Request());
    }

    public void deleteAccount(String accountId) throws CoinbaseException {
	WebTarget accountTarget = _authenticated_target.path("accounts/" + accountId);
	delete(accountTarget);
    }

    public void updateAccount(String accountId, Account account) throws CoinbaseException {
	WebTarget accountTarget = _authenticated_target.path("accounts/" + accountId);
	
	Request request = new Request();
	request.setAccount(account);
	put(accountTarget, request);
    }

    public Money getBalance() throws CoinbaseException {
	if (_account_id != null) {
	    return getBalance(_account_id);
	} else {
	    throw new UnknownAccount();
	}
    }

    public void setPrimaryAccount() throws CoinbaseException {
	if (_account_id != null) {
	    setPrimaryAccount(_account_id);
	} else {
	    throw new UnknownAccount();
	}
    }

    public void deleteAccount() throws CoinbaseException {
	if (_account_id != null) {
	    deleteAccount(_account_id);
	} else {
	    throw new UnknownAccount();
	}
    }

    public void updateAccount(Account account) throws CoinbaseException {
	if (_account_id != null) {
	    updateAccount(_account_id, account);
	} else {
	    throw new UnknownAccount();
	}
    }

    public Account createAccount(Account account) throws CoinbaseException {
	WebTarget accountsTarget = _authenticated_target.path("accounts");
	Response response = post(accountsTarget, account);

	return response.getAccount();
    }

    // TODO test that account_specific_target still works here
    public Button createButton(Button button) throws CoinbaseException {
	WebTarget buttonsTarget = _authenticated_target.path("buttons");
	
	Request request = newRequest();
	request.setButton(serializePrice(button));
	
	return post(buttonsTarget, request).getButton();
    }

    public Response getContacts(int page) {
	WebTarget contactsTarget =
		_authenticated_target
		.path("contacts")
		.queryParam("page", page);
	
	return get(contactsTarget);
    }

    public Response getContacts() {
	return getContacts(1);
    }

    public Response getContacts(String query, int page) {
	WebTarget contactsTarget =
		_authenticated_target
		.path("contacts")
		.queryParam("query", query)
		.queryParam("page", page);
	
	return get(contactsTarget);
    }

    public Response getContacts(String query) {
	return getContacts(query, 1);
    }

    private static Response get(WebTarget target) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
    }

    private static Response post(WebTarget target, Object entity) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).post(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Response.class));
    }

    private static Response put(WebTarget target, Object entity) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).put(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Response.class));
    }

    private static Response delete(WebTarget target) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).delete(Response.class));
    }

    private static Transaction serializeAmount(Transaction transaction) throws CoinbaseException {
	if (transaction.getAmount() == null) {
	    throw new CoinbaseException("Amount is a required field");
	}
	
	// Massage amount
	Money amount = transaction.getAmount();
	transaction.setAmount(null);

	transaction.setAmountString(amount.getAmount().toPlainString());
	transaction.setAmountCurrencyIso(amount.getCurrencyUnit().getCurrencyCode());
	
	return transaction;
    }

    private static Button serializePrice(Button button) throws CoinbaseException {
	if (button.getPrice() == null) {
	    throw new CoinbaseException("Price is a required field");
	}
	
	// Massage amount
	Money price = button.getPrice();
	button.setPrice(null);

	button.setPriceString(price.getAmount().toPlainString());
	button.setPriceCurrencyIso(price.getCurrencyUnit().getCurrencyCode());
	
	return button;
    }

    private static Response handleErrors(Response response) throws CoinbaseException {
	if (response.hasErrors()) {
	    throw new CoinbaseException(response.getErrors());
	}

	if (response.isSuccess() != null && !response.isSuccess()) {
	    throw new CoinbaseException("Unknown error");
	}

	return response;
    }

    private Request newRequest() {
	Request request = new Request();
	if (_account_id != null) {
	    request.setAccountId(_account_id);
	}
	return request;
    }

}
