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

package com.coinbase.resources.trades;

import androidx.annotation.NonNull;

/**
 * Base class for placing trade requests like {@link com.coinbase.resources.deposits.DepositsResource
 * Deposits}, {@link com.coinbase.resources.withdrawals.WithdrawalsResource Withdrawals},
 * {@link com.coinbase.resources.buys.BuysResource Buys} and {@link com.coinbase.resources.sells.SellsResource Sells}.
 * <p>
 * <b>NOTE:</b> parameters set to {@code null} will note be sent to the server.
 *
 * @param <T> trade type.
 */
public abstract class PlaceTradeOrderBody<T extends PlaceTradeOrderBody> {

    private String amount;
    private String currency;
    private String paymentMethod;
    private Boolean commit;

    /**
     * Creates new place trade order body instance.
     *
     * @param amount        amount without fees.
     * @param currency      currency for the amount.
     * @param paymentMethod payment method id.
     */
    public PlaceTradeOrderBody(String amount, String currency, String paymentMethod) {
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    /**
     * Currency for the {@code amount}.
     *
     * @param currency currency for the {@code amount}.
     * @return this place trade order body instance.
     * @see #PlaceTradeOrderBody(String, String, String)
     */
    public T setCurrency(@NonNull String currency) {
        this.currency = currency;
        return getThis();
    }

    /**
     * Sets the payment method ID for this request.
     *
     * @param paymentMethod payment method id.
     * @return this place trade order body instance.
     */
    public T setPaymentMethod(@NonNull String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return getThis();
    }

    /**
     * If set to {@code false}, this buy will not be immediately completed.
     * Use the commit call to complete it. Default value: {@code true}.
     *
     * @param commit (optional) whether to commit this request.
     * @return this place order body instance.
     */
    public T setCommit(Boolean commit) {
        this.commit = commit;
        return getThis();
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Boolean getCommit() {
        return commit;
    }

    @SuppressWarnings("unchecked")
    private T getThis() {
        return (T) this;
    }
}
