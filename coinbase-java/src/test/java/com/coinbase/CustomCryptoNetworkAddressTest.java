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

package com.coinbase;

import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.transactions.Transaction;
import com.coinbase.resources.transactions.entities.CryptoNetwork;
import com.coinbase.util.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import okio.Buffer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CustomCryptoNetworkAddressTest {

    @Test
    public void shouldParseCustomCurrencyTransaction() throws Exception {
        // Given:
        final Coinbase coinbase = CoinbaseBuilder.withPublicDataAccess(RuntimeEnvironment.application)
                .withCryptCurrencyAddressNames("super_coin_address")
                .withCryptoNetworkNames("super_coin_network")
                .build();

        final Buffer buffer = Resource.readResource("transactions/super_coin_network_transaction.json", this);

        // When:
        final Transaction transaction = coinbase.gson.fromJson(
                buffer.readUtf8(),
                Transaction.class
        );

        // Then:
        assertThat(transaction).isNotNull();

        final DynamicResource from = transaction.getFrom();
        assertThat(from)
                .isNotNull()
                .isInstanceOf(CryptoNetwork.class);

        final CryptoNetwork network = (CryptoNetwork) from;
        assertThat(network.getCurrency())
                .isNotNull()
                .isEqualTo("SuperCoin");
    }
}