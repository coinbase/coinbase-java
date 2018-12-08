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
 * Request object for {@link com.coinbase.resources.buys.BuysResource buys} and
 * {@link com.coinbase.Scope.Wallet.Sells sells}.
 * <p>
 * <b>NOTE:</b> parameters set to {@code null} will note be sent to the server.
 *
 * @see TradesResource
 * @see com.coinbase.resources.buys.BuysResource
 * @see com.coinbase.resources.sells.SellsResource
 */
public final class TransferOrderBody extends PlaceTradeOrderBody<TransferOrderBody> {

    // Optional.
    private String total;
    private Boolean agreeBtcAmountVaries;
    private Boolean quote;

    /**
     * Create Buy or Sell request with {@code amount} without fees. Alternatively you can create Buy or Sell
     * request with {@link #withTotal(String, String, String) total}, specifying amount that include fees.
     *
     * @param amount        buy/sell amount without fees.
     * @param currency      currency for the amount.
     * @param paymentMethod The ID of the payment method that should be used for the buy.
     *                      Payment methods can be listed using the
     *                      {@link com.coinbase.resources.paymentmethods.PaymentMethodResource#getPaymentMethods(
     *com.coinbase.PaginationParams,
     *                      com.coinbase.resources.paymentmethods.models.PaymentMethod.ExpandField...)
     *                      get payment methods}
     * @return new {@link TransferOrderBody transfer body instance}.
     */
    public static TransferOrderBody withAmount(@NonNull String amount,
                                               @NonNull String currency,
                                               @NonNull String paymentMethod) {
        return new TransferOrderBody(amount, currency, paymentMethod);
    }

    /**
     * Create Buy or Sell request with {@code total} including fees. This is an alternative to creating
     * request with {@link #withAmount(String, String, String) amount}.
     *
     * @param total         buy/sell amount including fees.
     * @param currency      currency for the amount.
     * @param paymentMethod The ID of the payment method that should be used for the buy.
     *                      Payment methods can be listed using the
     *                      {@link com.coinbase.resources.paymentmethods.PaymentMethodResource#getPaymentMethods(
     *com.coinbase.PaginationParams,
     *                      com.coinbase.resources.paymentmethods.models.PaymentMethod.ExpandField...)
     *                      get payment methods}
     * @return new {@link TransferOrderBody transfer body instance}.
     */
    public static TransferOrderBody withTotal(@NonNull String total,
                                              @NonNull String currency,
                                              @NonNull String paymentMethod) {
        final TransferOrderBody body = new TransferOrderBody(null, currency, paymentMethod);
        body.setTotal(total);
        return body;
    }

    private TransferOrderBody(String amount, String currency, String paymentMethod) {
        super(amount, currency, paymentMethod);
    }

    /**
     * Sets buy/sell amount without fees.
     *
     * @param total buy/sell amount including fees.
     * @return this {@link TransferOrderBody} instance.
     */
    private TransferOrderBody setTotal(@NonNull String total) {
        this.total = total;
        return this;
    }

    /**
     * Whether or not you would still like to buy if you have to wait for your money to arrive to lock in a price.
     *
     * @param agreeBtcAmountVaries (optional) Whether or not you would still like to buy if
     *                             you have to wait for your money to arrive to lock in a price.
     * @return this {@link TransferOrderBody} instance.
     */
    public TransferOrderBody setAgreeBtcAmountVaries(Boolean agreeBtcAmountVaries) {
        this.agreeBtcAmountVaries = agreeBtcAmountVaries;
        return this;
    }

    public TransferOrderBody setQuote(Boolean quote) {
        this.quote = quote;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public Boolean getAgreeBtcAmountVaries() {
        return agreeBtcAmountVaries;
    }

    public Boolean getQuote() {
        return quote;
    }
}
