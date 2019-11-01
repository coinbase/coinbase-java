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

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Represents resource for getting supported currencies.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#currencies">online docs: Currencies</a> for more info.
 */
public class CurrenciesResource {

    private final CurrenciesApi currenciesApi;
    private final CurrenciesApiRx currenciesApiRx;

    public CurrenciesResource(CurrenciesApi currenciesApi,
                              CurrenciesApiRx currenciesApiRx) {
        this.currenciesApi = currenciesApi;
        this.currenciesApiRx = currenciesApiRx;
    }

    /**
     * Lists known currencies. Currency codes will conform to the ISO 4217 standard where possible.
     * Currencies which have or had no representation in ISO 4217 may use a custom code (e.g. BTC).
     * <p>
     * <p/>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @return {@link ApiCall call} to get currency list
     * @see <a href="https://developers.coinbase.com/api/v2#currencies">online docs: Currencies</a> for more info
     */
    public ApiCall<CoinbaseResponse<List<Currency>>> getSupportedCurrencies() {
        return currenciesApi.getSupportedCurrencies();
    }

    /**
     * Rx version of {@link #getSupportedCurrencies()}.
     *
     * @see <a href="https://developers.coinbase.com/api/v2#currencies">online docs: Currencies</a> for more info
     * @return Rx {@link Single} for currency list
     */
    public Single<CoinbaseResponse<List<Currency>>> getSupportedCurrenciesRx() {
        return currenciesApiRx.getSupportedCurrencies();
    }
}
