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
 * Information about bitcoin, bitcoin cash, litecoin or ethereum network including network
 * transaction hash if transaction was on-blockchain. Only available for certain types of transactions.
 */
public class Network {

    //region Status

    public static final String STATUS_OFF_BLOCKCHAIN = "off_blockchain";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_UNCONFIRMED = "unconfirmed";

    //endregion

    private String status;
    private Integer confirmations;
    private MoneyHash transactionFee;
    private MoneyHash transactionAmount;
    private String hash;

    public String getStatus() {
        return status;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public MoneyHash getTransactionFee() {
        return transactionFee;
    }

    public MoneyHash getTransactionAmount() {
        return transactionAmount;
    }

    public String getHash() {
        return hash;
    }

}
