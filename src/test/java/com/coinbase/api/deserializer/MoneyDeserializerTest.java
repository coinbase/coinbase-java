package com.coinbase.api.deserializer;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MoneyDeserializerTest {

	private ObjectMapper mapper;
	
	@Before
    public void setUp() {
		mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Money.class, new MoneyDeserializer());
		mapper.registerModule(module);
    }
	
	@Test
	public void usd() throws Exception {
		String json = "{\"amount\": \"49.76\", \"currency\": \"USD\"}";
		Money money = mapper.readValue(json, Money.class);
		assertEquals(Money.parse("USD 49.76"), money);
	}
	
	@Test
	public void btc() throws Exception {
		String json = "{\"amount\": \"49.76987601\", \"currency\": \"BTC\"}";
		Money money = mapper.readValue(json, Money.class);
		assertEquals(Money.of(CurrencyUnit.of("BTC"), new BigDecimal("49.76987601")), money);
	}
	
	@Test(expected = JsonParseException.class)
	public void invalidJson() throws Exception {
		String json = "{\"name\": \"Alex\", \"Nationality\": \"Canadian\"}";
		Money money = mapper.readValue(json, Money.class);
	}
	
	@Test(expected = JsonParseException.class)
	public void unknownCurrency() throws Exception {
		String json = "{\"amount\": \"49.76987601\", \"currency\": \"WTF\"}";
		Money money = mapper.readValue(json, Money.class);
	}

}
