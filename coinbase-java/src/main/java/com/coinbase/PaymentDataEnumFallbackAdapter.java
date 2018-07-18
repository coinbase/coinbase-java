package com.coinbase;

import com.coinbase.v2.models.paymentMethods.Data;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A delegate {@link TypeAdapter} that defaults {@link Data#type} to {@link Data.Type#UNKNOWN} when
 * an unrecognized value is read.
 */
class PaymentDataEnumFallbackAdapter extends TypeAdapter<Data.Type> {

    private final Gson gson;
    private final TypeToken<Data.Type> typeToken;

    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked") // We use a runtime check to make sure the 'T's equal
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Data.Type.class
                    ? (TypeAdapter<T>) new PaymentDataEnumFallbackAdapter(gson, (TypeToken<Data.Type>) typeToken)
                    : null;
        }
    };

    private PaymentDataEnumFallbackAdapter(Gson gson, TypeToken<Data.Type> typeToken) {
        this.gson = gson;
        this.typeToken = typeToken;
    }

    @Override
    public void write(JsonWriter out, Data.Type value) throws IOException {
        gson.getDelegateAdapter(FACTORY, typeToken).write(out, value);
    }

    @Override
    public Data.Type read(JsonReader in) throws IOException {
       Data.Type value = gson.getDelegateAdapter(FACTORY, typeToken).read(in);
       if (value == null) value = Data.Type.UNKNOWN;
       return value;
    }
}
