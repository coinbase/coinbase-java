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

package com.coinbase.resources.currencies;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.util.ResourceMethodTest;

import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link CurrenciesResource} methods.
 */
public class CurrenciesResourceTest extends ResourceMethodTest<CurrenciesResource, List<Currency>> {

    public CurrenciesResourceTest() {
        super(
                "data/currencies.json",
                Coinbase::getCurrenciesResource,
                CurrenciesResource::getSupportedCurrencies,
                CurrenciesResource::getSupportedCurrenciesRx
        );
    }

    @Override
    protected void requestCheck(RecordedRequest request) {
        assertThat(request.getPath()).isEqualTo("/" + ApiConstants.CURRENCIES);
    }

    @Override
    protected void responseCheck(List<Currency> data) {
        assertThat(data).isNotNull().isInstanceOf(List.class);
        assertThat(data).isNotNull();
        assertThat(data.size()).isGreaterThan(0);
    }

}
