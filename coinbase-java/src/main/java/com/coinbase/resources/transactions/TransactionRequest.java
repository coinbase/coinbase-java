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
 * Base class for sending transactions (Send money, Transfer money or Request money).
 *
 * @see MoneyRequest
 * @see SendMoneyRequest
 */
public abstract class TransactionRequest {

    protected final String type;
    protected final String to;
    protected final String amount;
    protected final String currency;

    TransactionRequest(String requestType, String to, String amount, String currency) {
        this.type = requestType;
        this.currency = currency;
        this.amount = amount;
        this.to = to;
    }

    /**
     * Type of the transaction to send.
     *
     * @return type of the transaction to send.
     * @see Transaction#TYPE_SEND
     * @see Transaction#TYPE_REQUEST
     */
    public String getType() {
        return type;
    }

    /**
     * A bitcoin address, bitcoin cash address, litecoin address, ethereum address, or an email of the recipient.
     *
     * @return receiving party of the transaction.
     */
    public String getTo() {
        return to;
    }

    /**
     * Amount to be sent.
     *
     * @return amount to be sent.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Currency for the {@link #getAmount() amount}.
     *
     * @return currency for the amount.
     */
    public String getCurrency() {
        return currency;
    }
}
