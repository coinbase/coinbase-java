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
import com.fasterxml.jackson.core.JsonParseException;
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
	assertTrue(t.getRequest());
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

}
