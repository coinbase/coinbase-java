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

import com.coinbase.Coinbase;
import com.coinbase.util.Resource;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Config(sdk = 26, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class AccountCurrencyParsingTest {

    @Test
    public void shouldParseAccountWithCurrencyString() throws Exception {
        // Given:
        final Gson gson = Coinbase.createGsonBuilder()
                .create();

        // When:
        final String json = Resource.readResource("accounts/account_currency_string.json", this).readUtf8();
        final Account account = gson.fromJson(json, Account.class);

        // Then:
        assertThat(account.getCurrency()).isNotNull();
        assertThat(account.getCurrency().getCode()).isNotNull();
    }

    @Test
    public void shouldParseAccountWithCurrencyObject() throws Exception {
        // Given:
        final Gson gson = Coinbase.createGsonBuilder()
                .create();

        // When:
        final String json = Resource.readResource("accounts/account_currency_obj.json", this).readUtf8();
        final Account account = gson.fromJson(json, Account.class);

        // Then:
        final Currency currency = account.getCurrency();
        assertThat(currency).isNotNull();
        assertThat(currency.getCode()).isNotNull();
        assertThat(currency.getName()).isNotNull();
        assertThat(currency.getAddressRegex()).isNotNull();
        assertThat(currency.getExponent()).isNotNull();
        assertThat(currency.getColor()).isNotNull();
        assertThat(currency.getType()).isNotNull();
    }
}
