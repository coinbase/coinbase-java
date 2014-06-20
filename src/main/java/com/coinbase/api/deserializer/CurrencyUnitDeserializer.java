package com.coinbase.api.deserializer;

import java.io.IOException;

import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CurrencyUnitDeserializer extends StdDeserializer<CurrencyUnit> {

    public CurrencyUnitDeserializer() {
	super(CurrencyUnit.class);
    }

    @Override
    public CurrencyUnit deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
	    JsonProcessingException {
	
	CurrencyUnit result = null;
	try {
	    result = CurrencyUnit.getInstance(jp.getText());
	} catch (IllegalCurrencyException ex) {}
	
	return result;
    }

}
