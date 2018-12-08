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

package com.coinbase.resources.currencies;

/**
 * Represents a known currency.
 */
public class Currency {

    private String id;
    private String name;
    private String minSize;

    /**
     * Returns currency abbreviation (e.g USD or BTC).
     *
     * @return currency id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns human readable name for this currency (e.g Bitcoin).
     *
     * @return name of the currency.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the minimal amount available for transactions in this currency. Usually 0.01 for fiat currencies and
     * 0.00000001 for crypto.
     *
     * @return minimal amount available for transactions in this currency.
     */
    public String getMinSize() {
        return minSize;
    }
}
