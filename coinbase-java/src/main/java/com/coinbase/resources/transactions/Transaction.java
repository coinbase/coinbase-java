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

import com.coinbase.resources.addresses.Address;
import com.coinbase.resources.base.BaseResource;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.buys.Buy;
import com.coinbase.resources.sells.Sell;

import java.util.Date;

/**
 * Transaction resource represents an event on the account. It can be either negative or positive
 * on {@link #getAmount() amount} depending if it credited or debited funds on the account.
 * If there’s another party, the transaction will have either {@link #getTo() to} or
 * {@link #getFrom() from} field. For certain types of transactions, also linked
 * resources with type value as field will be included in the payload
 * (example {@link Buy buy} and {@link Sell sell}).
 * All these fields are {@link ExpandField expandable}.
 * <p>
 * <b>Important:</b> As transactions represent multiple objects, resources with new type values
 * can and will be added over time. Also new status values might be added.
 *
 * @see Buy
 * @see Sell
 * @see <a href="https://developers.coinbase.com/api/v2#transaction-resource">online docs: Transaction resource</a>.
 */
public class Transaction extends BaseResource {

    //region Status

    /**
     * Pending transactions (e.g. a send or a buy)
     */
    public static final String STATUS_PENDING = "pending";

    /**
     * Completed transactions (e.g. a send or a buy)
     */
    public static final String STATUS_COMPLETED = "completed";

    /**
     * Failed transactions (e.g. failed buy)
     */
    public static final String STATUS_FAILED = "failed";

    /**
     * Conditional transaction expired due to external factors
     */
    public static final String STATUS_EXPIRED = "expired";

    /**
     * Transaction was canceled
     */
    public static final String STATUS_CANCELED = "canceled";

    /**
     * Vault withdrawal is waiting for approval
     */
    public static final String STATUS_WAITING_FOR_SIGNATURE = "waiting_for_signature";

    /**
     * Vault withdrawal is waiting to be cleared
     */
    public static final String STATUS_WAITING_FOR_CLEARING = "waiting_for_clearing";

    //endregion

    //region Type

    /**
     * Sent bitcoin/bitcoin cash/litecoin/ethereum to a bitcoin/bitcoin cash/litecoin/ethereum address or email (documentation)
     */
    public static final String TYPE_SEND = "send";

    /**
     * Requested bitcoin/bitcoin cash/litecoin/ethereum from a user or email (documentation)
     */
    public static final String TYPE_REQUEST = "request";

    /**
     * Transfered funds between two of a user’s accounts (documentation)
     */
    public static final String TYPE_TRANSFER = "transfer";

    /**
     * Bought bitcoin, bitcoin cash, litecoin or ethereum (documentation)
     */
    public static final String TYPE_BUY = "buy";

    /**
     * Sold bitcoin, bitcoin cash, litecoin or ethereum (documentation)
     */
    public static final String TYPE_SELL = "sell";

    /**
     * Deposited funds into a fiat account from a financial institution (documentation)
     */
    public static final String TYPE_FIAT_DEPOSIT = "fiat_deposit";

    /**
     * Withdrew funds from a fiat account (documentation)
     */
    public static final String TYPE_FIAT_WITHDRAWAL = "fiat_withdrawal";

    /**
     * Deposited money into GDAX
     */
    public static final String TYPE_EXCHANGE_DEPOSIT = "exchange_deposit";

    /**
     * Withdrew money from GDAX
     */
    public static final String TYPE_EXCHANGE_WITHDRAWAL = "exchange_withdrawal";

    /**
     * Withdrew funds from a vault account
     */
    public static final String TYPE_VAULT_WITHDRAWAL = "vault_withdrawal";

    //endregion

    private String type;
    private String status;
    private MoneyHash amount;
    private MoneyHash nativeAmount;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Network network;
    private String idem;
    private Boolean instantExchange;
    private DynamicResource to;
    private DynamicResource from;
    private Buy buy;
    private Sell sell;
    private Details details;
    private Address address;
    private Application application;

    //region Getters

    /**
     * Returns type of this transaction.
     *
     * @return type of this transaction.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns status of this transaction.
     *
     * @return status of this transaction.
     */
    public String getStatus() {
        return status;
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
     * Amount in user’s native currency
     *
     * @return amount in user’s native currency.
     */
    public MoneyHash getNativeAmount() {
        return nativeAmount;
    }

    /**
     * User defined description.
     *
     * @return user defined description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Transaction creation timestamp.
     *
     * @return transaction creation timestamp
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Transaction update timestamp.
     *
     * @return transaction update timestamp
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Information about bitcoin, bitcoin cash, litecoin or ethereum network including network
     * transaction hash if transaction was on-blockchain. Only available for certain types of transactions.
     *
     * @return blockchain network information if available, or {@code null} otherwise.
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Optional idempotence token.
     *
     * @return idempotence token or {@code null} if not present.
     * @see SendMoneyRequest#setIdem(String)
     */
    public String getIdem() {
        return idem;
    }

    /**
     * Indicator if the transaction was instant exchanged (received into a bitcoin address for a fiat account).
     *
     * @return {@code true} if transaction was <i>instant exchange</i>.
     */
    public Boolean getInstantExchange() {
        return instantExchange;
    }

    /**
     * The receiving party of a debit transaction.
     * Usually another resource but can also be another type like email.
     * Only available for certain types of transactions
     *
     * @return receiving party of a debit transaction or {@code null} if not present.
     * @see DynamicResource
     */
    public DynamicResource getTo() {
        return to;
    }

    /**
     * The originating party of a credit transaction.
     * Usually another resource but can also be another type like bitcoin network.
     * Only available for certain types of transactions.
     *
     * @return originating party of a credit transaction or {@code null} if not present.
     */
    public DynamicResource getFrom() {
        return from;
    }

    /**
     * Associated {@link Buy buy} object if transaction is of a {@link #TYPE_BUY type buy}.
     *
     * @return associated buy object or {@code null} if not present.
     */
    public Buy getBuy() {
        return buy;
    }

    /**
     * Associated {@link Sell sell} object if transaction is of a {@link #TYPE_SELL type sell}.
     *
     * @return associated sell object or {@code null} if not present.
     */
    public Sell getSell() {
        return sell;
    }

    /**
     * Optional transaction details, like title or payment method name.
     *
     * @return transaction details.
     */
    public Details getDetails() {
        return details;
    }

    /**
     * Associated OAuth2 application.
     *
     * @return associated OAuth2 application.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Associated bitcoin, bitcoin cash, litecoin or ethereum address for received payment.
     *
     * @return associated bitcoin, bitcoin cash, litecoin or ethereum address for received payment
     */
    public Address getAddress() {
        return address;
    }

    //endregion

    //region Inner classes.

    /**
     * Optional details of a transaction.
     */
    public static class Details {

        private String title;
        private String subtitle;
        private String paymentMethodName;

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getPaymentMethodName() {
            return paymentMethodName;
        }
    }

    /**
     * Expand options for Transaction object.
     */
    public enum ExpandField implements com.coinbase.resources.ExpandField {

        /**
         * Show all fields of {@link Transaction#getFrom()} object.
         */
        FROM("from"),

        /**
         * Show all fields of {@link Transaction#getTo()} ()} object.
         */
        TO("to"),

        /**
         * Show all fields of {@link Transaction#getBuy()} object.
         */
        BUY(TYPE_BUY),

        /**
         * Show all fields of {@link Transaction#getSell()} object.
         */
        SELL(TYPE_SELL),
        /**
         * Show all fields of {@link Transaction#getApplication()} object.
         */
        APPLICATION("application"),

        /**
         * Expand all available fields.
         */
        ALL(com.coinbase.resources.ExpandField.ALL_FIELDS);

        private final String value;

        ExpandField(String value) {
            this.value = value;
        }

        @Override
        public String getCode() {
            return value;
        }
    }

    //endregion

}
