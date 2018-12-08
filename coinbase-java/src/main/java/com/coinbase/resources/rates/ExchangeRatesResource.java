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

package com.coinbase.resources.rates;

import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.currencies.CurrenciesResource;

import io.reactivex.Single;

/**
 * Resource for getting exchange rates for specified currency.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#exchange-rates">online docs: Exchange Rates</a> for more info.
 */
public class ExchangeRatesResource {

    private final ExchangeRatesApi exchangeRatesApi;
    private final ExchangeRatesApiRx exchangeRatesApiRx;

    public ExchangeRatesResource(ExchangeRatesApi exchangeRatesApi,
                                 ExchangeRatesApiRx exchangeRatesApiRx) {
        this.exchangeRatesApi = exchangeRatesApi;
        this.exchangeRatesApiRx = exchangeRatesApiRx;
    }

    /**
     * Get current exchange rates.
     * Default base currency is USD but it can be defined as any supported currency.
     * Returned rates will define the exchange rate for one unit of the base currency.
     * <p>
     * <p/>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @param currency (optional) Base currency (default: USD).
     * @return {@link ApiCall} for exchange rates.
     * @see CurrenciesResource
     */
    public ApiCall<CoinbaseResponse<ExchangeRates>> getExchangeRates(@Nullable String currency) {
        return exchangeRatesApi.getExchangeRates(currency);
    }

    /**
     * Rx version of {@link #getExchangeRates(String) getExchangeRates(currency)}.
     *
     * @param currency (optional) Base currency (default: USD).
     * @return Rx {@link Single} for exchange rates.
     * @see CurrenciesResource
     */
    public Single<CoinbaseResponse<ExchangeRates>> getExchangeRatesRx(@Nullable String currency) {
        return exchangeRatesApiRx.getExchangeRates(currency);
    }
}
