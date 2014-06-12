package com.coinbase.api.entity;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
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
	Users users = mapper.readValue(in, Users.class);
	
	List<Users.UserNode> userNodeList = users.getUsers();
	assertEquals(1, userNodeList.size());
	
	Users.UserNode.User user = userNodeList.get(0).getUser();

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

}
