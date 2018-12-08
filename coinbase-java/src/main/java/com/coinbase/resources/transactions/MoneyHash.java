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

package com.coinbase.resources.transactions;

/**
 * Money values are represented by a hash object which contains {@link #getAmount() amount}
 * and {@link #getCurrency() currency} fields. Amount is always returned as a string
 * which you should be careful when parsing to have correct decimal precision.
 * Bitcoin, Bitcoin Cash, Litecoin and Ethereum values will have 8 decimal points
 * and fiat currencies will have two.
 */
public class MoneyHash {

    private String amount;
    private String currency;

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}