package com.coinbase.api.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MoneyDeserializer extends StdDeserializer<Money> {
	
	public MoneyDeserializer() {
		super(Money.class);
	}
	
	@Override
	public Money deserialize(JsonParser jsonParser, 
	            DeserializationContext deserializationContext) throws IOException {
	
			Money result = null;
		
	        ObjectCodec oc = jsonParser.getCodec();
	        JsonNode node = oc.readTree(jsonParser);
	        
	        String currency = null;
	        String amount= null;
	        
	        if (node.has("currency")) {
	        	currency = node.get("currency").textValue();
	        } else if (node.has("currency_iso")) {
	        	currency = node.get("currency_iso").textValue();
	        }
	        
	        if (node.has("amount")) {
	        	amount = node.get("amount").textValue();
	        }
	        
	        if (currency == null || amount == null) {
	        	throw new JsonParseException("Wrong format for Money", jsonParser.getCurrentLocation());
	        }
	        
	        try {
	        	result = Money.of(CurrencyUnit.of(currency), new BigDecimal(amount));
	        } catch (Exception ex) {
	        	throw new JsonParseException("Could not construct Money from arguments", jsonParser.getCurrentLocation(), ex);
	        }
	        
	        return result;
	        	
	}
}
