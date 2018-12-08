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

import com.coinbase.resources.transactions.MoneyHash;

/**
 * Next requirement.
 */
public class NextRequirement {

    /**
     * Next requirement type.
     */
    public static final String BUY_HISTORY = "buy_history";
    /**
     * Next requirement type.
     */
    public static final String IDENTITY_VERIFICATION = "identity_verification";
    /**
     * Next requirement type.
     */
    public static final String JUMIO = "jumio";
    /**
     * Next requirement type.
     */
    public static final String VERIFIED_PHONE = "verified_phone";

    private String type;
    private MoneyHash volume;
    private MoneyHash amountRemaining;
    private Long timeAfterStarting;

    /**
     * Gets requirement type.
     *
     * @return on of the following:
     * <ul>
     * <li>{@link #BUY_HISTORY}</li>
     * <li>{@link #IDENTITY_VERIFICATION}</li>
     * <li>{@link #JUMIO}</li>
     * <li>{@link #VERIFIED_PHONE}</li>
     * </ul>
     */
    public String getType() {
        return type;
    }

    /**
     * Gets volume.
     *
     * @return volume.
     */
    public MoneyHash getVolume() {
        return volume;
    }

    /**
     * Gets amount remaining.
     *
     * @return amount remaining.
     */
    public MoneyHash getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * Gets time after starting.
     *
     * @return time after starting.
     */
    public Long getTimeAfterStarting() {
        return timeAfterStarting;
    }

}
