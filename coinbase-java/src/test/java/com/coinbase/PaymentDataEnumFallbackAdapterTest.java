package com.coinbase;


import com.coinbase.v2.models.paymentMethods.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Since {@link Data} doesn't obey the object contract these tests can't compare an expected model
 * to the one Gson hands us. For now we will just assert {@link Data#type} is as expected.
 */
public class PaymentDataEnumFallbackAdapterTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(EnumFallbackTypeAdapterFactory.create())
            .create();

    @Test
    public void write_shouldSucceed() {
        Data data = new Data();
        data.setId("fb987d66-569b-5e46-a0b2-9db2cd4b53d2");
        data.setType(Data.Type.CREDIT_CARD);

        String serialized = gson.toJson(data);

        String expected = "{" +
                    "\"id\":\"fb987d66-569b-5e46-a0b2-9db2cd4b53d2\"," +
                    "\"type\":\"credit_card\"," +
                    "\"recurring_options\":[]" +
                "}";
        assertEquals(expected, serialized);
    }

    @Test
    public void read_knownTypeValue_shouldSucceed() {
        String received = "{" +
                "\"id\":\"fb987d66-569b-5e46-a0b2-9db2cd4b53d2\"," +
                "\"type\":\"credit_card\"," +
                "\"recurring_options\":[]" +
                "}";

        Data deserialized = gson.fromJson(received, Data.class);

        assertEquals(Data.Type.CREDIT_CARD, deserialized.getType());
    }

    @Test
    public void read_unknownTypeValue_shouldDefaultUnknown() {
        String received = "{" +
                "\"id\":\"fb987d66-569b-5e46-a0b2-9db2cd4b53d2\"," +
                "\"type\":\"big_spender\"," +
                "\"recurring_options\":[]" +
                "}";

        Data deserialized = gson.fromJson(received, Data.class);

        assertEquals(Data.Type.UNKNOWN, deserialized.getType());
    }
}
