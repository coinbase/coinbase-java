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

package com.coinbase.resources.paymentmethods.models;

import com.coinbase.resources.accounts.Account;
import com.coinbase.resources.base.BaseResource;

import java.util.Date;

/**
 * Payment method resource represents the different kinds of payment methods that can be used when
 * buying and selling bitcoin, bitcoin cash, litecoin or ethereum.
 * <p>
 * As fiat accounts can be used for buying and selling, they have an associated payment method.
 * This type of a payment method will also have a {@code fiat_account} reference to the actual account.
 * <p>
 * Currently available {@code type} values:
 * <ul>
 * <li>{@link #TYPE_ACH_BANK_ACCOUNT ach_bank_account}</li>
 * <p>
 * <li>{@link #TYPE_SEPA_BANK_ACCOUNT sepa_bank_account}</li>
 * <p>
 * <li>{@link #TYPE_IDEAL_BANK ideal_bank_account}</li>
 * <p>
 * <li>{@link #TYPE_FIAT_ACCOUNT fiat_account}</li>
 * <p>
 * <li>{@link #TYPE_BANK_WIRE bank_wire}</li>
 * <p>
 * <li>{@link #TYPE_CREDIT_CARD credit_card}</li>
 * <p>
 * <li>{@link #TYPE_DEBIT_CARD debit_card}</li>
 * <p>
 * <li>{@link #TYPE_SECURE_3D secure3d_card}</li>
 * <p>
 * <li>{@link #TYPE_EFT_BANK_ACCOUNT eft_bank_account}</li>
 * <p>
 * <li>{@link #TYPE_INTERAC interac}</li>
 * <p>
 * </ul>
 * If the user has obtained optional {@link com.coinbase.Scope.Wallet.PaymentMethods#LIMITS
 * wallet:payment-methods:limits} permission, an additional field, {@code limits}, will be embedded
 * into payment method data. It will contain information about buy, instant buy, sell and deposit
 * limits (there’s no limits for withdrawals at this time). As each one of these can have several
 * limits you should always look for the lowest remaining value when performing the relevant action.
 */
public class PaymentMethod extends BaseResource {

    //region Type

    /**
     * Regular US bank account
     */
    public static final String TYPE_ACH_BANK_ACCOUNT = "ach_bank_account";

    /**
     * European SEPA bank account
     */
    public static final String TYPE_SEPA_BANK_ACCOUNT = "sepa_bank_account";

    /**
     * iDeal bank account (Europe)
     */
    public static final String TYPE_IDEAL_BANK = "ideal_bank_account";

    /**
     * Fiat nominated Coinbase account
     */
    public static final String TYPE_FIAT_ACCOUNT = "fiat_account";

    /**
     * Bank wire (US only)
     */
    public static final String TYPE_BANK_WIRE = "bank_wire";

    /**
     * Credit card (can’t be used for buying/selling)
     */
    public static final String TYPE_CREDIT_CARD = "credit_card";

    /**
     * Debit card
     */
    public static final String TYPE_DEBIT_CARD = "debit_card";

    /**
     * Secure3D verified payment card
     */
    public static final String TYPE_SECURE_3D = "secure3d_card";

    /**
     * Canadian EFT bank account
     */
    public static final String TYPE_EFT_BANK_ACCOUNT = "eft_bank_account";

    /**
     * Interac Online for Canadian bank accounts
     */
    public static final String TYPE_INTERAC = "interac";

    //endregion

    private String type;
    private String name;
    private String currency;
    private Boolean primaryBuy;
    private Boolean primarySell;
    private Boolean allowBuy;
    private Boolean allowSell;
    private Boolean allowDeposit;
    private Boolean allowWithdraw;
    private Boolean instantBuy;
    private Boolean instantSell;
    private Date createdAt;
    private Date updatedAt;
    private Account fiatAccount;
    private Limits limits;
    private Boolean verified;

    /**
     * Payment method type.
     *
     * @return One ofe the following:
     * <ul>
     * <li>{@link #TYPE_ACH_BANK_ACCOUNT ach_bank_account} - Regular US bank account</li>
     * <li>{@link #TYPE_SEPA_BANK_ACCOUNT sepa_bank_account} - European SEPA bank account</li>
     * <li>{@link #TYPE_IDEAL_BANK ideal_bank_account} - iDeal bank account (Europe)</li>
     * <li>{@link #TYPE_FIAT_ACCOUNT fiat_account} - Fiat nominated Coinbase account</li>
     * <li>{@link #TYPE_BANK_WIRE bank_wire} - Bank wire (US only)</li>
     * <li>{@link #TYPE_CREDIT_CARD credit_card} - Credit card (can’t be used for buying/selling)</li>
     * <li>{@link #TYPE_DEBIT_CARD debit_card} - Debit card </li>
     * <li>{@link #TYPE_SECURE_3D secure3d_card} - Secure3D verified payment card</li>
     * <li>{@link #TYPE_EFT_BANK_ACCOUNT eft_bank_account} - Canadian EFT bank account</li>
     * <li>{@link #TYPE_INTERAC interac} - Interac Online for Canadian bank accounts</li>
     * </ul>
     */
    public String getType() {
        return type;
    }

    /**
     * Payment method name.
     *
     * @return method name.
     */
    public String getName() {
        return name;
    }

    /**
     * Payment method’s native currency.
     *
     * @return method’s native currency.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Is primary buying method?
     *
     * @return {@code true} if primary buying method.
     */
    public Boolean getPrimaryBuy() {
        return primaryBuy;
    }

    /**
     * Is primary selling method?
     *
     * @return {@code true} if primary selling method.
     */
    public Boolean getPrimarySell() {
        return primarySell;
    }

    /**
     * Is buying allowed with this method?
     *
     * @return {@code true} if buying is allowed.
     */
    public Boolean getAllowBuy() {
        return allowBuy;
    }

    /**
     * Is selling allowed with this method?
     *
     * @return {@code true} if selling is allowed.
     */
    public Boolean getAllowSell() {
        return allowSell;
    }

    /**
     * Is deposit allowed with this method?
     *
     * @return {@code true} if deposit is allowed.
     */
    public Boolean getAllowDeposit() {
        return allowDeposit;
    }

    /**
     * Is withdraw allowed with this method?
     *
     * @return {@code true} if withdraw is allowed.
     */
    public Boolean getAllowWithdraw() {
        return allowWithdraw;
    }

    /**
     * Is instant buy allowed with this method?
     *
     * @return {@code true} if instant buy is allowed.
     */
    public Boolean getInstantBuy() {
        return instantBuy;
    }

    /**
     * Is instant sell allowed with this method?
     *
     * @return {@code true} if instant sell is allowed.
     */
    public Boolean getInstantSell() {
        return instantSell;
    }

    /**
     * Gets creation date.
     *
     * @return creation date.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets update date.
     *
     * @return update date.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Gets fiat account.
     *
     * @return fiat account.
     */
    public Account getFiatAccount() {
        return fiatAccount;
    }

    /**
     * Gets payment method limits.
     *
     * @return limits.
     */
    public Limits getLimits() {
        return limits;
    }

    /**
     * Is payment method verified?
     *
     * @return {@code true} if payment method is verified.
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * Expand options for PaymentMethod object.
     */
    public enum ExpandField implements com.coinbase.resources.ExpandField {

        /**
         * Show all fields of {@link PaymentMethod#getFiatAccount()} object.
         */
        FIAT_ACCOUNT(TYPE_FIAT_ACCOUNT),

        /**
         * Expand all available fields.
         */
        ALL(com.coinbase.resources.ExpandField.ALL_FIELDS);

        private String value;

        ExpandField(String value) {
            this.value = value;
        }

        @Override
        public String getCode() {
            return value;
        }
    }
}
