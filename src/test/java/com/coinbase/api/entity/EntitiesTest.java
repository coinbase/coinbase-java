package com.coinbase.api.entity;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.coinbase.api.CoinbaseSSL;
import com.coinbase.api.ObjectMapperProvider;
import com.coinbase.api.deserializer.MoneyDeserializer;
import com.coinbase.api.exception.CoinbaseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class EntitiesTest {
    
    private ObjectMapper mapper;

    @Before
    public void setUp() {
	mapper = ObjectMapperProvider.createDefaultMapper();
    }

    @Test
    public void users() throws Exception {
	
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/users.json");
	Response response = mapper.readValue(in, Response.class);
	
	List<UserNode> userNodeList = response.getUsers();
	assertEquals(1, userNodeList.size());
	
	User user = userNodeList.get(0).getUser();

	assertEquals("512db383f8182bd24d000001", user.getId());
	assertEquals("User One", user.getName());
	assertEquals("user1@example.com", user.getEmail());
	assertEquals("Pacific Time (US & Canada)", user.getTimeZone());
	assertEquals("USD", user.getNativeCurrency());
	assertEquals(Money.parse("BTC 49.76"), user.getBalance());
	assertEquals(1, user.getBuyLevel().intValue());
	assertEquals(1, user.getSellLevel().intValue());
	assertEquals(Money.parse("BTC 10"), user.getBuyLimit());
	assertEquals(Money.parse("BTC 100"), user.getSellLimit());	
	
    }
    
    @Test
    public void transaction() throws Exception {
	
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transaction.json");
	Response r = mapper.readValue(in, Response.class);
	
	Transaction t = r.getTransaction();

	assertEquals("5018f833f8182b129c00002f", t.getId());
	assertEquals(DateTime.parse("2012-08-01T02:34:43-07:00"), t.getCreatedAt());
	assertEquals(Money.parse("BTC -1.1"), t.getAmount());
	assertTrue(t.isRequest());
	assertEquals("pending", t.getStatus());
	
	User sender = t.getSender();
	
	assertEquals("5011f33df8182b142400000e", sender.getId());
	assertEquals("User Two", sender.getName());
	assertEquals("user2@example.com", sender.getEmail());
	
	User recipient = t.getRecipient();
	
	assertEquals("5011f33df8182b142400000a", recipient.getId());
	assertEquals("User One", recipient.getName());
	assertEquals("user1@example.com", recipient.getEmail());	
	
    }

    @Test
    public void transactions() throws Exception {

	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transactions.json");
	Response r = mapper.readValue(in, Response.class);

	User current_user = r.getCurrentUser();
	assertEquals("5011f33df8182b142400000e", current_user.getId());
	assertEquals("User Two", current_user.getName());
	assertEquals("user2@example.com", current_user.getEmail());

	assertEquals(Money.parse("BTC 50"), r.getBalance());
	assertEquals(Money.parse("USD 500"), r.getNativeBalance());
	assertEquals(2, r.getTotalCount());
	assertEquals(1, r.getNumPages());
	assertEquals(1, r.getCurrentPage());

	List<TransactionNode> txs = r.getTransactions();
	assertEquals(2, txs.size());

	Transaction tx1 = txs.get(0).getTransaction();
	assertEquals("5018f833f8182b129c00002f", tx1.getId());

	Transaction tx2 = txs.get(1).getTransaction();
	assertEquals("5018f833f8182b129c00002e", tx2.getId());
    }

    @Test
    public void transfers() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transfers.json");
	Response r = mapper.readValue(in, Response.class);
	
	assertEquals(1, r.getTotalCount());
	assertEquals(1, r.getNumPages());
	assertEquals(1, r.getCurrentPage());
	List<TransferNode> transfers = r.getTransfers();
	assertEquals(1, transfers.size());
	Transfer t = transfers.get(0).getTransfer();
	assertEquals(Transfer.Type.BUY, t.getType());
	assertEquals("QPCUCZHR", t.getCode());
	assertEquals(DateTime.parse("2013-02-27T23:28:18-08:00"), t.getCreatedAt());
	assertEquals(Money.parse("USD 0.14"), t.getFees().get("coinbase"));
	assertEquals(Money.parse("USD 0.15"), t.getFees().get("bank"));
	assertEquals(DateTime.parse("2013-03-05T18:00:00-08:00"), t.getPayoutDate());
	assertEquals("5011f33df8182b142400000e", t.getTransactionId());
	assertEquals(Transfer.Status.PENDING, t.getStatus());
	assertEquals(Money.parse("BTC 1"), t.getBtc());
	assertEquals(Money.parse("USD 13.55"), t.getSubtotal());
	assertEquals(Money.parse("USD 13.84"), t.getTotal());
	assertEquals("Paid for with $13.84 from Test xxxxx3111.", t.getDescription());
    }

    @Test
    public void quote() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/quote.json");
	Quote q = mapper.readValue(in, Quote.class);
	
	assertEquals(Money.parse("USD 10.10"), q.getSubtotal());
	assertEquals(2, q.getFees().size());
	assertEquals(Money.parse("USD 0.10"), q.getFees().get("coinbase"));
	assertEquals(Money.parse("USD 0.15"), q.getFees().get("bank"));
	assertEquals(Money.parse("USD 10.35"), q.getTotal());
    }

    @Test
    public void addresses() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/addresses.json");
	Response r = mapper.readValue(in, Response.class);

	assertNull(r.isSuccess());

	List<AddressNode> addresses = r.getAddresses();
	assertEquals(2, addresses.size());
	
	Address a1 = addresses.get(0).getAddress();
	assertEquals("moLxGrqWNcnGq4A8Caq8EGP4n9GUGWanj4", a1.getAddress());
	assertNull(a1.getCallbackUrl());
	assertNull(a1.getLabel());
	assertEquals(DateTime.parse("2013-05-09T23:07:08-07:00"), a1.getCreatedAt());
	
	Address a2 = addresses.get(1).getAddress();
	assertEquals("mwigfecvyG4MZjb6R5jMbmNcs7TkzhUaCj", a2.getAddress());
	assertEquals("http://localhost/callback", a2.getCallbackUrl());
	assertEquals("My Label", a2.getLabel());
	assertEquals(DateTime.parse("2013-05-09T17:50:37-07:00"), a2.getCreatedAt());
    }

    @Test
    public void accounts() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/accounts.json");
	Response r = mapper.readValue(in, Response.class);

	List<Account> accounts = r.getAccounts();
	assertEquals(2, accounts.size());

	Account a1 = accounts.get(0);
	assertEquals("536a541fa9393bb3c7000023", a1.getId());
	assertEquals("My Wallet", a1.getName());
	assertEquals(Money.parse("BTC 50"), a1.getBalance());
	assertEquals(Money.parse("USD 500.12"), a1.getNativeBalance());
	assertEquals(DateTime.parse("2014-05-07T08:41:19-07:00"), a1.getCreatedAt());
	assertTrue(a1.isPrimary());
	assertTrue(a1.isActive());
	
	Account a2 = accounts.get(1);
	assertEquals("536a541fa9393bb3c7000034", a2.getId());
	assertEquals("Savings", a2.getName());
	assertEquals(Money.parse("BTC 0"), a2.getBalance());
	assertEquals(Money.parse("USD 0"), a2.getNativeBalance());
	assertEquals(DateTime.parse("2014-05-07T08:50:10-07:00"), a2.getCreatedAt());
	assertFalse(a2.isPrimary());
	assertTrue(a2.isActive());
    }

    // TODO DI to make tests better

    @Test(expected = CoinbaseException.class)
    public void errorResponse() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/error.json");
	Response response = mapper.readValue(in, Response.class);
	
	assertEquals("Test Error", response.getErrors());
	
	if (response.hasErrors()) {
	    throw new CoinbaseException(response.getErrors());
	}

	if (response.isSuccess() != null && !response.isSuccess()) {
	    throw new CoinbaseException("Unknown error");
	}

    }

    @Test(expected = CoinbaseException.class)
    public void errorsResponse() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/errors.json");
	Response response = mapper.readValue(in, Response.class);
	
	assertEquals("Test Error 1, Test Error 2", response.getErrors());
	
	if (response.hasErrors()) {
	    throw new CoinbaseException(response.getErrors());
	}

	if (response.isSuccess() != null && !response.isSuccess()) {
	    throw new CoinbaseException("Unknown error");
	}

    }

    @Test(expected = CoinbaseException.class)
    public void falseSuccessResponse() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/false_success.json");
	Response response = mapper.readValue(in, Response.class);
	
	if (response.hasErrors()) {
	    throw new CoinbaseException(response.getErrors());
	}

	if (response.isSuccess() != null && !response.isSuccess()) {
	    throw new CoinbaseException("Unknown error");
	}

    }

}
