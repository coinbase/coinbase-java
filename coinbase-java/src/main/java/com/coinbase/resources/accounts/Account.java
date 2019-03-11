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

package com.coinbase.resources.accounts;

import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.transactions.MoneyHash;

import java.util.Date;

/**
 * Account resource represents all of a user’s accounts, including bitcoin,
 * bitcoin cash, litecoin and ethereum wallets, fiat currency accounts, and vaults.
 * This is represented in the type field.
 * It’s important to note that new types can be added over time
 * so you want to make sure this won’t break your implementation.
 * <p>
 * User can only have one primary account and its type can only be {@code wallet}.
 */
public class Account extends DynamicResource {

    //region Type

    /**
     * Wallet account. Only this account type can be set as primary.
     */
    public static final String TYPE_WALLET = "wallet";

    /**
     * Vault account.
     */
    public static final String TYPE_VAULT = "vault";

    /**
     * Fiat account.
     */
    public static final String TYPE_FIAT = "fiat";

    public static final String TYPE_MULTISIG_VAULT = "multisig_vault";
    public static final String TYPE_MULTISIG = "multisig";

    //endregion

    private String name;
    private Boolean primary;
    private String type;
    private Currency currency;
    private MoneyHash balance;
    private Date createdAt;
    private Date updatedAt;

    /**
     * User or system defined name.
     *
     * @return name of this account.
     */
    public String getName() {
        return name;
    }

    /**
     * Whether this account is primary.
     *
     * @return {@code true} if this account is <b>primary</b> and {@code false} otherwise.
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * Type of this account.
     * <p>
     * It can be <pre>wallet, vault, fiat, multisig or multisig_vault.</pre>
     * <p>
     * <b>NOTE:</b><br/>
     * Use constants to determine the type of this account.
     *
     * @return type of this account.
     * @see #TYPE_FIAT
     * @see #TYPE_VAULT
     * @see #TYPE_WALLET
     * @see #TYPE_MULTISIG
     * @see #TYPE_MULTISIG_VAULT
     */
    public String getType() {
        return type;
    }

    /**
     * Account currency.
     *
     * @return account currency.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Balance of this account in it's currency.
     *
     * @return balance of this account
     * @see MoneyHash
     */
    public MoneyHash getBalance() {
        return balance;
    }

    /**
     * When account was created.
     *
     * @return timestamp when account was created.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * When account was updated.
     *
     * @return timestamp when account was updated.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

}
