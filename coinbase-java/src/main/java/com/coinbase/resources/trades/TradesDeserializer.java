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

import com.coinbase.resources.base.ResourceTypeDeserializer;
import com.coinbase.resources.buys.Buy;
import com.coinbase.resources.deposits.Deposit;
import com.coinbase.resources.sells.Sell;
import com.coinbase.resources.withdrawals.Withdrawal;

/**
 * Custom deserializer for dynamic parsing of {@link Trade} resource models.
 */
public final class TradesDeserializer extends ResourceTypeDeserializer<Trade> {

    public TradesDeserializer() {
        typeMapping.put("buy", Buy.class);
        typeMapping.put("sell", Sell.class);
        typeMapping.put("deposit", Deposit.class);
        typeMapping.put("withdrawal", Withdrawal.class);
    }

}
