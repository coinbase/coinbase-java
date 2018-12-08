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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Model for sending money requests.
 */
public class MoneyRequest extends TransactionRequest {

    private String description;

    /**
     * Creates new MoneyRequest instance.
     *
     * @param to       an email of the recipient.
     * @param amount   amount to be requested.
     * @param currency currency for the amount.
     */
    public MoneyRequest(@NonNull String to, @NonNull String amount, @NonNull String currency) {
        super(Transaction.TYPE_REQUEST, to, amount, currency);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Notes to be included in the email that the recipient receives
     *
     * @param description description.
     * @return this {@link MoneyRequest} instance.
     */
    public MoneyRequest setDescription(@Nullable String description) {
        this.description = description;
        return this;
    }
}
