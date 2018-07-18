package com.coinbase;

import com.coinbase.v2.models.paymentMethods.Data;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapterFactory} that provides fallback deserialization semantics for enums.
 */
class EnumFallbackTypeAdapterFactory implements TypeAdapterFactory {

    static EnumFallbackTypeAdapterFactory create() {
       return new EnumFallbackTypeAdapterFactory() ;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (Data.Type.class.isAssignableFrom(rawType)) {
            return PaymentDataEnumFallbackAdapter.FACTORY.create(gson, type);
        }
        return null;
    }
}
