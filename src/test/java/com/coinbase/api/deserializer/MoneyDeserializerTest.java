package com.coinbase.api.deserializer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import com.coinbase.api.ObjectMapperProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MoneyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
	mapper = ObjectMapperProvider.createDefaultMapper();
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

    @Test
    public void btc_iso_code() throws Exception {
	String json = "{\"amount\": \"49.76987601\", \"currency_iso\": \"BTC\"}";
	Money money = mapper.readValue(json, Money.class);
	assertEquals(Money.of(CurrencyUnit.of("BTC"), new BigDecimal("49.76987601")), money);
    }

    @Test
    public void btc_long_cents() throws Exception {
	String json = "{\"cents\": 4976987601, \"currency_iso\": \"BTC\"}";
	Money money = mapper.readValue(json, Money.class);
	assertEquals(Money.of(CurrencyUnit.of("BTC"), new BigDecimal("49.76987601")), money);
    }

    @Test(expected = JsonProcessingException.class)
    public void invalidJson() throws Exception {
	String json = "{\"name\": \"Alex\", \"Nationality\": \"Canadian\"}";
	Money money = mapper.readValue(json, Money.class);
    }

    @Test(expected = JsonProcessingException.class)
    public void unknownCurrency() throws Exception {
	String json = "{\"amount\": \"49.76987601\", \"currency\": \"WTF\"}";
	Money money = mapper.readValue(json, Money.class);
    }

}
