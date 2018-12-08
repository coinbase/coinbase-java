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
 * Request for sending money.
 *
 * @see TransactionsResource#sendMoney(String, String, SendMoneyRequest, Transaction.ExpandField...)
 */
public class SendMoneyRequest extends TransactionRequest {

    // Optional fields.

    private String description;
    private Boolean skipNotifications;
    private String fee;
    private String idem;
    private Boolean toFinancialInstitution;
    private String financialInstitutionWebsite;

    /**
     * Creates new SendMoneyRequest instance.
     *
     * @param to       a bitcoin address, bitcoin cash address, litecoin address, ethereum address, or an email of the recipient
     * @param amount   amount to be sent.
     * @param currency currency for the amount.
     */
    public SendMoneyRequest(@NonNull String to, @NonNull String amount, @NonNull String currency) {
        super(Transaction.TYPE_SEND, to, amount, currency);
    }

    /**
     * Notes to be included in the email that the recipient receives
     *
     * @param description transaction description.
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setDescription(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Don’t send notification emails for small amounts (e.g. tips).
     *
     * @param skipNotifications whether to skip notifications.
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setSkipNotifications(@Nullable Boolean skipNotifications) {
        this.skipNotifications = skipNotifications;
        return this;
    }

    /**
     * Transaction fee in BTC/ETH/LTC if you would like to pay it. Fees can be added as a string, such as 0.0005
     *
     * @param fee transaction fee
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setFee(@Nullable String fee) {
        this.fee = fee;
        return this;
    }

    /**
     * <b>[Recommended]</b>  A token to ensure <a href="http://en.wikipedia.org/wiki/Idempotence">idempotence</a>.
     * If a previous transaction with the same idem parameter already exists for this sender,
     * that previous transaction will be returned and a new one will not be created.
     * Max length 100 characters.
     *
     * @param idem idempotence token.
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setIdem(@Nullable String idem) {
        this.idem = idem;
        return this;
    }

    /**
     * Whether this send is to another financial institution or exchange.
     * Required if this send is to an address and is valued at over USD$3000.
     *
     * @param toFinancialInstitution {@code true} if sending to another financial institution or exchange.
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setToFinancialInstitution(@Nullable Boolean toFinancialInstitution) {
        this.toFinancialInstitution = toFinancialInstitution;
        return this;
    }

    /**
     * The website of the financial institution or exchange.
     * Required if {@link #setToFinancialInstitution(Boolean) toFinancialInstitution} is true.
     *
     * @param financialInstitutionWebsite website of the financial institution or exchange.
     * @return this {@link SendMoneyRequest} instance.
     */
    public SendMoneyRequest setFinancialInstitutionWebsite(@Nullable String financialInstitutionWebsite) {
        this.financialInstitutionWebsite = financialInstitutionWebsite;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getSkipNotifications() {
        return skipNotifications;
    }

    public String getFee() {
        return fee;
    }

    public String getIdem() {
        return idem;
    }

    public Boolean getToFinancialInstitution() {
        return toFinancialInstitution;
    }

    public String getFinancialInstitutionWebsite() {
        return financialInstitutionWebsite;
    }
}
