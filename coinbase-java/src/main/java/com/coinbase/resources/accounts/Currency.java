/*
 * Copyright 2018 Coinbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase.resources.accounts;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Represents account (crypto) currency.
 */
public class Currency {

    private String code;
    private String name;
    private String color;
    private Integer exponent;
    private String type;
    private String addressRegex;
    private String assetId;

    /**
     * Returns currency abbreviation (e.g USD or BTC).
     *
     * @return currency abbreviation.
     */
    public String getCode() {
        return code;
    }

    /**
     * Human readable name of this currency (e.g. 'Bitcoin').
     *
     * @return name of this currency.
     */
    public String getName() {
        return name;
    }

    /**
     * Color of this currency as shown in Coinbase apps.
     *
     * @return color of this currency.
     */
    public String getColor() {
        return color;
    }

    /**
     * Indicates how many decimal digits are valid after a separator {@code (.)}.
     * Usually it's 2 for fiat and 8 for crypto currency.
     *
     * @return how many decimal digits are valid.
     */
    public Integer getExponent() {
        return exponent;
    }

    /**
     * Type of this currency: {@code fiat} or {@code crypto}.
     *
     * @return type of the currency.
     */
    public String getType() {
        return type;
    }

    /**
     * A regular expression to check a validity of this crypto currency address.
     * <p>
     * <b>NOTE:</b> This property is not present for fiat currencies.
     *
     * @return regular expression for this currency or null if it's not a crypto currency
     */
    public String getAddressRegex() {
        return addressRegex;
    }

    /**
     * An asset id that can be used to get a particular crypto asset
     *
     * @return asset id that matches particular crypto asset
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * Custom deserializer for {@link Currency} class.
     * In some cases the {@link Account account} model may have the {@code currency} field
     * represented as a {@code String} so there is a need to wrap it in the {@code Currency} object
     * for consistency.
     */
    public static class CurrencyDeserializationFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> defaultTypeAdapter = gson.getDelegateAdapter(this, type);
            if (type.getRawType().equals(Currency.class)) {
                final TypeAdapter<Currency> delegateAdapter = gson.getDelegateAdapter(this, TypeToken.get(Currency.class));
                //noinspection unchecked
                return (TypeAdapter<T>) new TypeAdapter<Currency>() {
                    @Override
                    public void write(JsonWriter out, Currency value) throws IOException {
                        delegateAdapter.write(out, value);
                    }

                    @Override
                    public Currency read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.STRING) {
                            final Currency currency = new Currency();
                            currency.code = in.nextString();
                            return currency;
                        }

                        return delegateAdapter.read(in);
                    }
                };
            }
            return defaultTypeAdapter;
        }
    }
}
