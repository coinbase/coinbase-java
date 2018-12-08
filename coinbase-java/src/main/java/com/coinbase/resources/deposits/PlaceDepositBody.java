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

package com.coinbase.resources.deposits;

import androidx.annotation.NonNull;

import com.coinbase.resources.trades.PlaceTradeOrderBody;

/**
 * Request for creating a new deposit.
 *
 * @see DepositsResource#placeDeposit(String, PlaceDepositBody, com.coinbase.resources.trades.Trade.ExpandField...)
 */
public class PlaceDepositBody extends PlaceTradeOrderBody<PlaceDepositBody> {

    /**
     * Creates a new PlaceDepositBody instance.
     *
     * @param currency      currency for the amount.
     * @param paymentMethod The ID of the payment method that should be used for the deposit.
     *                      Payment methods can be listed using the
     *                      {@link com.coinbase.resources.paymentmethods.PaymentMethodResource#getPaymentMethod(String,
     *                      com.coinbase.resources.paymentmethods.models.PaymentMethod.ExpandField...)
     *                      get payment methods
     *                      }.
     * @param amount        deposit amount.
     */
    public PlaceDepositBody(@NonNull String currency,
                            @NonNull String paymentMethod,
                            @NonNull String amount) {
        super(amount, currency, paymentMethod);
    }
}
