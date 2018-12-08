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

package com.coinbase.resources.sells;

import com.coinbase.resources.trades.Trade;
import com.coinbase.resources.transactions.MoneyHash;

/**
 * Sell resource model.
 * <p>
 * Sell resource represents a sell of bitcoin, bitcoin cash, litecoin or ethereum using a payment method
 * (either a bank or a fiat account). Each committed sell also has an associated transaction.
 */
public class Sell extends Trade {

    private MoneyHash total;
    private Boolean instant;

    /**
     * Fiat amount with fees.
     *
     * @return fiat amount with fees
     */
    public MoneyHash getTotal() {
        return total;
    }

    /**
     * Was this sell executed instantly?
     *
     * @return {@code true} for an instant sell.
     */
    public Boolean getInstant() {
        return instant;
    }
}
