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

package com.coinbase.resources.buys;

import com.coinbase.resources.trades.Trade;
import com.coinbase.resources.transactions.MoneyHash;

/**
 * Buy resource model.
 * <p>
 * Buy resource represents a purchase of bitcoin, bitcoin cash, litecoin or ethereum using a payment method (either a bank or a fiat account).
 * Each committed buy also has an associated transaction.
 */
public class Buy extends Trade {

    private MoneyHash total;
    private Boolean requiresCompletionStep;
    private Boolean isFirstBuy;
    private Boolean instant;

    /**
     * Fiat amount with fees.
     *
     * @return fiat amount with fees.
     */
    public MoneyHash getTotal() {
        return total;
    }

    /**
     * Does this buy requires a completion step.
     *
     * @return {@code true} if this buy requires completion.
     */
    public Boolean getRequiresCompletionStep() {
        return requiresCompletionStep;
    }

    /**
     * Was this a first buy?
     *
     * @return {@code true} for the first buy.
     */
    public Boolean getIsFirstBuy() {
        return isFirstBuy;
    }

    /**
     * Was this buy executed instantly?
     *
     * @return {@code true} for an instant buy.
     */
    public Boolean getInstant() {
        return instant;
    }
}
