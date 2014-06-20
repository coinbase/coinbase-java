package com.coinbase.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.money.Money;

import com.coinbase.api.auth.HmacClientFilter;
import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountResponse;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.entity.AddressesResponse;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.ButtonResponse;
import com.coinbase.api.entity.ContactsResponse;
import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.OrderResponse;
import com.coinbase.api.entity.OrdersResponse;
import com.coinbase.api.entity.PaymentMethodsResponse;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.RecurringPaymentsResponse;
import com.coinbase.api.entity.Request;
import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionResponse;
import com.coinbase.api.entity.TransactionsResponse;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransferResponse;
import com.coinbase.api.entity.TransfersResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.entity.UsersResponse;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnspecifiedAccount;
import com.fasterxml.jackson.databind.type.TypeFactory;

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
	return get(usersTarget, UsersResponse.class).getUsers().get(0).getUser();
    }

    public Transaction getTransaction(String id) {
	WebTarget txTarget = _account_specific_target.path("transactions/" + id);
	return get(txTarget, TransactionResponse.class).getTransaction();
    }

    public Order getOrder(String idOrCustom) {
	WebTarget orderTarget = _account_specific_target.path("orders/" + idOrCustom);
	return get(orderTarget, OrderResponse.class).getOrder();
    }

    public Transaction requestMoney(Transaction transaction) throws CoinbaseException {

	WebTarget requestMoneyTarget = _authenticated_target.path("transactions/request_money");

	serializeAmount(transaction);
	
	Request request = newRequest();
	request.setTransaction(transaction);

	return post(requestMoneyTarget, request, TransactionResponse.class).getTransaction();
    }

    public Transaction sendMoney(Transaction transaction) throws CoinbaseException {
	WebTarget sendMoneyTarget = _authenticated_target.path("transactions/send_money");
	
	serializeAmount(transaction);
	
	Request request = newRequest();
	request.setTransaction(transaction);
	
	return post(sendMoneyTarget, request, TransactionResponse.class).getTransaction();
    }

    public OrdersResponse getOrders(int page) {
	WebTarget ordersTarget = _account_specific_target.path("orders").queryParam("page", page);
	return get(ordersTarget, OrdersResponse.class);
    }

    public OrdersResponse getOrders() {
	return getOrders(1);
    }

    public TransactionsResponse getTransactions(int page) {
	WebTarget transactionsTarget = _account_specific_target.path("transactions").queryParam("page", page);
	return get(transactionsTarget, TransactionsResponse.class);
    }

    public TransactionsResponse getTransactions() {
	return getTransactions(1);
    }

    public void resendRequest(String id) throws CoinbaseException {
	WebTarget resendRequestTarget = _authenticated_target.path("transactions/" + id + "/resend_request");
	put(resendRequestTarget, newRequest(), Response.class);
    }

    public void deleteRequest(String id) throws CoinbaseException {
	WebTarget deleteRequestTarget = _authenticated_target.path("transactions/" + id + "/cancel_request");
	delete(deleteRequestTarget, Response.class);
    }

    public Transaction completeRequest(String id) throws CoinbaseException {
	WebTarget completeRequestTarget = _authenticated_target.path("transactions/" + id + "/complete_request");
	return put(completeRequestTarget, newRequest(), TransactionResponse.class).getTransaction();
    }

    public TransfersResponse getTransfers(int page) {
	WebTarget transfersTarget = _account_specific_target.path("transfers").queryParam("page", page);
	return get(transfersTarget, TransfersResponse.class);
    }

    public TransfersResponse getTransfers() {
	return getTransfers(1);
    }

    public AddressesResponse getAddresses(int page) {
	WebTarget addressesTarget = _account_specific_target.path("addresses").queryParam("page", page);
	return get(addressesTarget, AddressesResponse.class);
    }

    public AddressesResponse getAddresses() {
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

    public AccountsResponse getAccounts() {
	return getAccounts(1, 25, false);
    }

    public AccountsResponse getAccounts(int page) {
	return getAccounts(page, 25, false);
    }

    public AccountsResponse getAccounts(int page, int limit) {
	return getAccounts(page, limit, false);
    }

    public AccountsResponse getAccounts(int page, int limit, boolean includeInactive) {
	WebTarget addressesTarget =
		_authenticated_target
		.path("accounts")
		.queryParam("page", page)
		.queryParam("limit", limit)
		.queryParam("all_accounts", includeInactive);
		
	return get(addressesTarget, AccountsResponse.class);
    }

    public Money getBalance(String accountId) {
	WebTarget balanceTarget = _authenticated_target.path("accounts/" + accountId + "/balance");
	return balanceTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Money.class);
    }

    public void setPrimaryAccount(String accountId) throws CoinbaseException {
	WebTarget setPrimaryTarget = _authenticated_target.path("accounts/" + accountId + "/primary");
	post(setPrimaryTarget, new Request(), Response.class);
    }

    public void deleteAccount(String accountId) throws CoinbaseException {
	WebTarget accountTarget = _authenticated_target.path("accounts/" + accountId);
	delete(accountTarget, Response.class);
    }

    public void updateAccount(String accountId, Account account) throws CoinbaseException {
	WebTarget accountTarget = _authenticated_target.path("accounts/" + accountId);
	
	Request request = new Request();
	request.setAccount(account);
	put(accountTarget, request, Response.class);
    }

    public Money getBalance() throws UnspecifiedAccount {
	if (_account_id != null) {
	    return getBalance(_account_id);
	} else {
	    throw new UnspecifiedAccount();
	}
    }

    public void setPrimaryAccount() throws CoinbaseException {
	if (_account_id != null) {
	    setPrimaryAccount(_account_id);
	} else {
	    throw new UnspecifiedAccount();
	}
    }

    public void deleteAccount() throws CoinbaseException {
	if (_account_id != null) {
	    deleteAccount(_account_id);
	} else {
	    throw new UnspecifiedAccount();
	}
    }

    public void updateAccount(Account account) throws CoinbaseException {
	if (_account_id != null) {
	    updateAccount(_account_id, account);
	} else {
	    throw new UnspecifiedAccount();
	}
    }

    public Account createAccount(Account account) throws CoinbaseException {
	WebTarget accountsTarget = _authenticated_target.path("accounts");
	return post(accountsTarget, account, AccountResponse.class).getAccount();
    }

    // TODO test that account_specific_target still works here
    public Button createButton(Button button) throws CoinbaseException {
	WebTarget buttonsTarget = _authenticated_target.path("buttons");
	
	Request request = newRequest();
	request.setButton(serializePrice(button));
	
	return post(buttonsTarget, request, ButtonResponse.class).getButton();
    }

    public Order createOrder(Button button) throws CoinbaseException {
	WebTarget ordersTarget = _authenticated_target.path("orders");
	
	Request request = newRequest();
	request.setButton(serializePrice(button));
	
	return post(ordersTarget, request, OrderResponse.class).getOrder();
    }

    public Order createOrderForButton(String buttonCode) throws CoinbaseException {
	WebTarget orderForButtonTarget = _authenticated_target.path("buttons/" + buttonCode + "/create_order");
	return post(orderForButtonTarget, new Request(), OrderResponse.class).getOrder();
    }

    public ContactsResponse getContacts(int page) {
	WebTarget contactsTarget =
		_authenticated_target
		.path("contacts")
		.queryParam("page", page);
	
	return get(contactsTarget, ContactsResponse.class);
    }

    public ContactsResponse getContacts() {
	return getContacts(1);
    }

    public ContactsResponse getContacts(String query, int page) {
	WebTarget contactsTarget =
		_authenticated_target
		.path("contacts")
		.queryParam("query", query)
		.queryParam("page", page);
	
	return get(contactsTarget, ContactsResponse.class);
    }

    public ContactsResponse getContacts(String query) {
	return getContacts(query, 1);
    }

    public Transfer sell(Money amount) throws CoinbaseException {
	if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
	    throw new CoinbaseException(
		    "Cannot sell " + amount.getCurrencyUnit().getCode()
		    + " Only BTC amounts are supported for sells"
		    );
	}
	
	WebTarget sellTarget = _authenticated_target.path("sells");
	
	Request request = newRequest();
	request.setQty(amount.getAmount().doubleValue());
	
	return post(sellTarget, request, TransferResponse.class).getTransfer();
    }

    public Transfer sell(Money amount, String paymentMethodId) throws CoinbaseException {
	if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
	    throw new CoinbaseException(
		    "Cannot sell " + amount.getCurrencyUnit().getCode()
		    + " Only BTC amounts are supported for sells"
		    );
	}
	
	WebTarget sellTarget = _authenticated_target.path("sells");
	
	Request request = newRequest();
	request.setQty(amount.getAmount().doubleValue());
	request.setPaymentMethodId(paymentMethodId);
	
	return post(sellTarget, request, TransferResponse.class).getTransfer();
    }

    public Transfer buy(Money amount) throws CoinbaseException {
	if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
	    throw new CoinbaseException(
		    "Cannot buy " + amount.getCurrencyUnit().getCode()
		    + " Only BTC amounts are supported for buys"
		    );
	}
	
	WebTarget buyTarget = _authenticated_target.path("buys");
	
	Request request = newRequest();
	request.setQty(amount.getAmount().doubleValue());
	
	return post(buyTarget, request, TransferResponse.class).getTransfer();
    }

    public Transfer buy(Money amount, String paymentMethodId) throws CoinbaseException {
	if (!amount.getCurrencyUnit().getCode().equals("BTC")) {
	    throw new CoinbaseException(
		    "Cannot buy " + amount.getCurrencyUnit().getCode()
		    + " Only BTC amounts are supported for buys"
		    );
	}
	
	WebTarget buyTarget = _authenticated_target.path("buys");
	
	Request request = newRequest();
	request.setQty(amount.getAmount().doubleValue());
	request.setPaymentMethodId(paymentMethodId);
	
	return post(buyTarget, request, TransferResponse.class).getTransfer();
    }

    public PaymentMethodsResponse getPaymentMethods() {
	WebTarget paymentMethodsTarget = _authenticated_target.path("payment_methods");
	return get(paymentMethodsTarget, PaymentMethodsResponse.class);
    }

    public RecurringPaymentsResponse getSubscribers(int page) {
	WebTarget subscribersTarget = _account_specific_target.path("subscribers").queryParam("page", page);
	return get(subscribersTarget, RecurringPaymentsResponse.class);
    }

    public RecurringPaymentsResponse getSubscribers() {
	return getSubscribers(1);
    }

    public RecurringPaymentsResponse getRecurringPayments(int page) {
	// TODO can this endpoint take account_id?
	WebTarget recurringPaymentsTarget = _authenticated_target.path("recurring_payments").queryParam("page", page);
	return get(recurringPaymentsTarget, RecurringPaymentsResponse.class);
    }

    public RecurringPaymentsResponse getRecurringPayments() {
	return getRecurringPayments(1);
    }

    public Map<String, BigDecimal> getExchangeRates() {
	WebTarget exchangeRatesTarget = _base_target.path("currencies/exchange_rates");
	return exchangeRatesTarget
		.request(MediaType.APPLICATION_JSON_TYPE)
		.get(new GenericType<HashMap<String, BigDecimal>>() {});
    }

    public List<CurrencyUnit> getSupportedCurrencies() {
	WebTarget supportedCurrenciesTarget = _base_target.path("currencies");
	
	List<List<String>> rawResponse =
		supportedCurrenciesTarget
		.request(MediaType.APPLICATION_JSON_TYPE)
		.get(new GenericType<List<List<String>>>() {});
	
	List<CurrencyUnit> result = new ArrayList<CurrencyUnit>();
	for (List<String> currency : rawResponse) {
	    try {
		result.add(CurrencyUnit.getInstance(currency.get(1)));
	    } catch (IllegalCurrencyException ex) {}
	}
	
	return result;
    }

    private static <T extends Response> T get(WebTarget target, Class<T> responseClass) {
	return target.request(MediaType.APPLICATION_JSON_TYPE).get(responseClass);
    }

    private static <T extends Response> T post(WebTarget target, Object entity, Class<T> responseClass) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).post(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), responseClass));
    }

    private static <T extends Response> T put(WebTarget target, Object entity, Class<T> responseClass) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).put(
		Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), responseClass));
    }

    private static <T extends Response> T delete(WebTarget target, Class<T> responseClass) throws CoinbaseException {
	return handleErrors(target.request(MediaType.APPLICATION_JSON_TYPE).delete(responseClass));
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

    private static <T extends Response> T handleErrors(T response) throws CoinbaseException {
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
