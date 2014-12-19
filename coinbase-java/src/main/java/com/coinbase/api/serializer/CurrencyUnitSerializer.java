package com.coinbase.api.serializer;

import java.io.IOException;

import org.joda.money.CurrencyUnit;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CurrencyUnitSerializer extends StdSerializer<CurrencyUnit> {

    public CurrencyUnitSerializer() {
	super(CurrencyUnit.class);
    }

    @Override
    public void serialize(CurrencyUnit value, JsonGenerator jgen, SerializerProvider provider)
	    throws IOException, JsonGenerationException {
	jgen.writeString(value.getCurrencyCode());	
    }

}
