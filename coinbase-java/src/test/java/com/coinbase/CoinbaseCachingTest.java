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

import com.coinbase.resources.rates.ExchangeRates;
import com.coinbase.util.CurrentThreadExecutorService;
import com.coinbase.util.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.URL;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Config(sdk = 26, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CoinbaseCachingTest {

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();
    private Coinbase coinbase;

    @Before
    public void setUp() {
        final URL url = mockWebServer.url("/").url();
        final CoinbaseBuilder coinbaseBuilder = CoinbaseBuilder.withPublicDataAccess(RuntimeEnvironment.application)
                .withBaseApiURL(url)
                .withBaseOAuthURL(url)
                .withHttpExecutorService(new CurrentThreadExecutorService());

        coinbase = new Coinbase(coinbaseBuilder);
    }

    //region OkHttp Cache tests.

    @Test
    public void shouldServeCachedResponseOnLastModified() throws Exception {
        // Given:
        // Original response, modified in past.
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Last-Modified", "Wed, 21 Oct 2015 07:28:00 GMT")
                        .setHeader("Content-Type", "application/json")
                        .setBody(Resource.readResource("data/exchange_rates.json", this))
        );

        // When:
        // Coinbase requests first response.
        final ExchangeRates originalRates = coinbase.getExchangeRatesResource()
                .getExchangeRates("BTC")
                .execute()
                .getData();

        // Then:
        // It's stored in cache.
        assertThat(coinbase.diskCache.networkCount()).isEqualTo(1);

        // When:
        // Coinbase requests resource again.
        // And server responds with Not Modified 304.
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(304)
                        .setHeader("Last-Modified", "Wed, 21 Oct 2015 07:28:00 GMT")
        );

        final ExchangeRates cachedRates = coinbase.getExchangeRatesResource()
                .getExchangeRates("BTC")
                .execute()
                .getData();

        // Then:
        // Coinbase serves cached response.
        assertThat(cachedRates.getCurrency()).isEqualTo(originalRates.getCurrency());
        assertThat(cachedRates.getRates().size()).isEqualTo(originalRates.getRates().size());

        assertThat(coinbase.diskCache.hitCount()).isEqualTo(1);
    }

    @Test
    public void shouldServeCachedResponseOnETag() throws Exception {
        // Given:
        // Original response, modified in past.
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("ETag", "\"1234\"")
                        .setHeader("Content-Type", "application/json")
                        .setBody(Resource.readResource("data/exchange_rates.json", this))
        );

        // When:
        // Coinbase requests first response.
        final ExchangeRates originalRates = coinbase.getExchangeRatesResource()
                .getExchangeRates("BTC")
                .execute()
                .getData();

        // Then:
        // It's stored in cache.
        assertThat(coinbase.diskCache.networkCount()).isEqualTo(1);

        // When:
        // Coinbase requests resource again.
        // And server responds with Not Modified 304.
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(304)
                        .setHeader("ETag", "\"1234\"")
        );

        final ExchangeRates cachedRates = coinbase.getExchangeRatesResource()
                .getExchangeRates("BTC")
                .execute()
                .getData();

        // Then:
        // Coinbase serves cached response.
        assertThat(cachedRates.getCurrency()).isEqualTo(originalRates.getCurrency());
        assertThat(cachedRates.getRates().size()).isEqualTo(originalRates.getRates().size());

        assertThat(coinbase.diskCache.hitCount()).isEqualTo(1);
    }
    //endregion

}