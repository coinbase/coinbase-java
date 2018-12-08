
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

package com.coinbase.resources.rates;

import java.util.Map;

public class ExchangeRates {

    private String currency;
    private Map<String, String> rates;

    /**
     * Returns the currency for these exchange rates.
     *
     * @return currency for which these rates are for.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the map of rates consisting of name of the another currency to amount.
     *
     * @return exchange rates.
     */
    public Map<String, String> getRates() {
        return rates;
    }

}
