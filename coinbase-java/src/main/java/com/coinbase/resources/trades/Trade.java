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

import com.coinbase.resources.base.BaseResource;
import com.coinbase.resources.paymentmethods.models.PaymentMethod;
import com.coinbase.resources.transactions.MoneyHash;
import com.coinbase.resources.transactions.Transaction;

import java.util.Date;

/**
 * Abstract class for transactional operations.
 * Includes {@link com.coinbase.resources.buys.Buy}, {@link com.coinbase.resources.sells.Sell},
 * {@link com.coinbase.resources.deposits.Deposit} or {@link com.coinbase.resources.withdrawals.Withdrawal}.
 */
public abstract class Trade extends BaseResource {

    //region Status

    public static final String STATUS_CREATED = "created";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELED = "canceled";

    //endregion

    private String status;
    private Transaction transaction;
    private PaymentMethod paymentMethod;
    private String userReference;
    private Date createdAt;
    private Date updatedAt;
    private Boolean committed;
    private Date payoutAt;
    private MoneyHash fee;
    private MoneyHash amount;
    private MoneyHash subtotal;

    /**
     * Status of the trade operation.
     *
     * @return status of the trade operation.
     * @see #STATUS_CREATED
     * @see #STATUS_COMPLETED
     * @see #STATUS_CANCELED
     */
    public String getStatus() {
        return status;
    }

    /**
     * Associated {@link Transaction transaction}.
     *
     * @return associated transaction.
     * @see Transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Associated {@link PaymentMethod payment method}.
     *
     * @return associated payment method.
     * @see PaymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getUserReference() {
        return userReference;
    }

    /**
     * Resource creation timestamp.
     *
     * @return resource creation timestamp.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Resource update timestamp.
     *
     * @return resource update timestamp.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Has this trade been committed.
     *
     * @return {@code true} if this trade has been committed.
     */
    public Boolean getCommitted() {
        return committed;
    }

    /**
     * When a trade (buy/sell/withdrawal/deposit) isn’t executed instantly,
     * it will receive a payout date for the time it will be executed.
     *
     * @return payout date.
     */
    public Date getPayoutAt() {
        return payoutAt;
    }

    /**
     * Fee associated to this trade operation.
     *
     * @return fee associated to this trade operation.
     */
    public MoneyHash getFee() {
        return fee;
    }

    /**
     * Amount in bitcoin, bitcoin cash, litecoin or ethereum.
     *
     * @return amount in bitcoin, bitcoin cash, litecoin or ethereum.
     */
    public MoneyHash getAmount() {
        return amount;
    }

    /**
     * Fiat amount without fees.
     *
     * @return fiat amount without fees.
     */
    public MoneyHash getSubtotal() {
        return subtotal;
    }

    /**
     * Expand field options for trade operations.
     */
    public enum ExpandField implements com.coinbase.resources.ExpandField {

        /**
         * Expand all fields.
         */
        ALL(com.coinbase.resources.ExpandField.ALL_FIELDS),

        /**
         * Expand {@link Trade#getTransaction() transaction} field.
         */
        TRANSACTION("transaction"),

        /**
         * Expand {@link Trade#getPaymentMethod() payment method} field.
         */
        PAYMENT_METHOD("payment_method");

        private final String code;

        ExpandField(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }
    }
}
