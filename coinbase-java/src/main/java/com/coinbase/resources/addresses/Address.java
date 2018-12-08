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

package com.coinbase.resources.addresses;

import com.coinbase.resources.base.BaseResource;

import java.util.Date;

/**
 * Address model represents a bitcoin, bitcoin cash, litecoin or ethereum address for an account.
 * Account can have unlimited amount of addresses and they should be used only once.
 */
public class Address extends BaseResource {

    private String address;
    private String callbackUrl;
    private String legacyAddress;
    private String name;
    private String network;
    private String uriScheme;
    private String warningDetails;
    private String warningTitle;

    private Date createdAt;
    private Date updatedAt;

    /**
     * Bitcoin, Bitcoin Cash, Litecoin or Ethereum address.
     *
     * @return currency address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Callback url.
     *
     * @return callback url.
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * Resource creation date.
     *
     * @return creation date.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Legacy address.
     *
     * @return legacy address.
     */
    public String getLegacyAddress() {
        return legacyAddress;
    }

    /**
     * User defined label for the address.
     *
     * @return label for the address.
     */
    public String getName() {
        return name;
    }

    /**
     * Name of blockchain.
     *
     * @return name of blockchain.
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Resource update date.
     *
     * @return update date.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Crypto currency URI scheme.
     *
     * @return URI scheme.
     */
    public String getUriScheme() {
        return uriScheme;
    }

    /**
     * Warning details.
     *
     * @return Warning details.
     */
    public String getWarningDetails() {
        return warningDetails;
    }

    /**
     * Warning title.
     *
     * @return warning title.
     */
    public String getWarningTitle() {
        return warningTitle;
    }
}