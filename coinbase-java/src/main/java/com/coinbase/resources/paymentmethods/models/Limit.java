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
 * Payment method limit.
 */
public class Limit {

    private Integer periodInDays;
    private MoneyHash total;
    private MoneyHash remaining;
    private String description;
    private String label;
    private NextRequirement nextRequirement;

    /**
     * Gets period in days.
     *
     * @return period in days.
     */
    public Integer getPeriodInDays() {
        return periodInDays;
    }

    /**
     * Gets total money value.
     *
     * @return total money value.
     */
    public MoneyHash getTotal() {
        return total;
    }

    /**
     * Gets remaining money value.
     *
     * @return remaining money value.
     */
    public MoneyHash getRemaining() {
        return remaining;
    }

    /**
     * Gets limit description.
     *
     * @return limit description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets label.
     *
     * @return label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets next requirement.
     *
     * @return next requirement.
     */
    public NextRequirement getNextRequirement() {
        return nextRequirement;
    }
}
