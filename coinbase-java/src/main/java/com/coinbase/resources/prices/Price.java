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

package com.coinbase.resources.prices;

/**
 * Represents a price of a specified crypto currency (base) in a specified fiat currency.
 */
public class Price {

    private String base;
    private String amount;
    private String currency;

    /**
     * Code of the base crypto currency for which this price is for.
     *
     * @return code of crypto currency for which this price is for.
     */
    public String getBase() {
        return base;
    }

    /**
     * The amount in {@link #getCurrency() fiat currrency}.
     *
     * @return the amount in fiat currency.
     * @see #getCurrency()
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Fiat currency in which amount is in.
     *
     * @return fiat currency.
     */
    public String getCurrency() {
        return currency;
    }

}
