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

package com.coinbase.resources.transactions.entities;

import com.coinbase.resources.base.DynamicResource;
import com.google.gson.annotations.SerializedName;

/**
 * Sending or receiving party of a transaction which is direct address of Bitcoin, Bitcoin Cash, Litecoin
 * or Ethereum network.<br/>
 * Use {@link #getCurrency()} method to find out crypto network.
 */
public class CryptoNetwork extends DynamicResource {

    @SerializedName("currency")
    private String currency;

    public String getCurrency() {
        return currency;
    }

}
