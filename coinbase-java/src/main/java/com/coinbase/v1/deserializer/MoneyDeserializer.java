package com.coinbase.v1.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        Money result = null;

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        String currency = null;
        String amount = null;
        Long cents = null;

        if (node.has("currency")) {
            currency = node.get("currency").textValue();
        } else if (node.has("currency_iso")) {
            currency = node.get("currency_iso").textValue();
        }

        if (node.has("amount")) {
            amount = node.get("amount").textValue();
        } else if (node.has("cents")) {
            cents = node.get("cents").longValue();
        }

        if (currency == null || (amount == null && cents == null)) {
            throw new JsonParseException("Wrong format for Money", jsonParser.getCurrentLocation());
        }

        if (amount != null) {
            try {
                result = Money.of(CurrencyUnit.of(currency),
                        Double.valueOf(amount));
            } catch (ArithmeticException e) {

                Double doubleValue = Double.valueOf(amount);
                Long longValue = doubleValue.longValue();

                result = Money.of(CurrencyUnit.of(currency),
                        BigDecimal.valueOf(longValue));
            }
        } else {
            result = Money.ofMinor(CurrencyUnit.of(currency), cents);
        }

        return result;

    }
}
