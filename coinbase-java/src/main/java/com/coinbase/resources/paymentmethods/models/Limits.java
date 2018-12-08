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

import java.util.List;

/**
 * Information about buy, instant buy, sell and deposit limits.
 */
public class Limits {

    //region Type

    /**
     * Limits type.
     */
    public static final String TYPE_BANK = "bank";

    /**
     * Limits type.
     */
    public static final String TYPE_PAYPAL = "paypal";

    /**
     * Limits type.
     */
    public static final String TYPE_CARD = "card";

    /**
     * Limits type.
     */
    public static final String TYPE_FIAT_ACCOUNT = "fiat_account";

    /**
     * Limits type.
     */
    public static final String TYPE_XFERS = "xfers";

    /**
     * Limits type.
     */
    public static final String TYPE_WIRE = "wire";

    /**
     * Limits type.
     */
    public static final String TYPE_INTRA_BANK = "intra_bank";

    //endregion

    private String type;
    private String name;
    private List<Limit> buy;
    private List<Limit> instantBuy;
    private List<Limit> sell;
    private List<Limit> deposit;

    /**
     * Gets limits type.
     *
     * @return on of the following:
     * <ul>
     * <li>{@link #TYPE_BANK}</li>
     * <li>{@link #TYPE_PAYPAL}</li>
     * <li>{@link #TYPE_CARD}</li>
     * <li>{@link #TYPE_FIAT_ACCOUNT}</li>
     * <li>{@link #TYPE_XFERS}</li>
     * <li>{@link #TYPE_WIRE}</li>
     * <li>{@link #TYPE_INTRA_BANK}</li>
     * </ul>
     */
    public String getType() {
        return type;
    }

    /**
     * Gets limits name.
     *
     * @return limits name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets buy limits.
     *
     * @return buy limits.
     */
    public List<Limit> getBuy() {
        return buy;
    }

    /**
     * Gets instant buy limits.
     *
     * @return instant buy limits.
     */
    public List<Limit> getInstantBuy() {
        return instantBuy;
    }

    /**
     * Gets sell limits.
     *
     * @return sell limits.
     */
    public List<Limit> getSell() {
        return sell;
    }

    /**
     * Gets deposit limits.
     *
     * @return deposit limits.
     */
    public List<Limit> getDeposit() {
        return deposit;
    }
}
