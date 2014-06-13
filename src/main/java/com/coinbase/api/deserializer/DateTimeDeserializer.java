package com.coinbase.api.deserializer;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateTimeDeserializer extends StdDeserializer<DateTime> {

    protected DateTimeDeserializer() {
	super(DateTime.class);
    }

    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
	    JsonProcessingException {
	
	DateTime result;
	try {
	    result = DateTime.parse(jp.getText());
	} catch (IllegalArgumentException ex) {
	    throw new JsonParseException("Could not construct DateTime from string",
		    jp.getCurrentLocation(), ex);
	}
	
	return result;
    }

}
