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

package com.coinbase.resources.prices;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.util.ResourceMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.resources.prices.PricesResourceTest.*;
import static com.coinbase.resources.prices.PricesResourceTest.BuyPriceTest.PATH_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link PricesResource} methods.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BuyPriceTest.class,
        SellPriceTest.class,
        SpotPriceTest.class,
        SpotPriceWithDateTest.class,
        SpotPricesTest.class,
        SpotPricesWithDateTest.class
})
public interface PricesResourceTest {

    class BuyPriceTest extends ResourceMethodTest<PricesResource, Price> {

        static final String PATH_PATTERN = "%s/%s-%s/%s";
        private static final String BASE_CURRENCY = "BTC";
        private static final String FIAT_CURRENCY = "USD";

        public BuyPriceTest() {
            super(
                    "data/buy_price.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getBuyPrice(BASE_CURRENCY, FIAT_CURRENCY),
                    resource -> resource.getBuyPriceRx(BASE_CURRENCY, FIAT_CURRENCY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format(PATH_PATTERN, ApiConstants.PRICES, BASE_CURRENCY, FIAT_CURRENCY, ApiConstants.BUY);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Price data) {
            assertThat(data).isNotNull();
        }
    }

    class SellPriceTest extends ResourceMethodTest<PricesResource, Price> {

        private static final String BASE_CURRENCY = "BTC";
        private static final String FIAT_CURRENCY = "USD";

        public SellPriceTest() {
            super(
                    "data/sell_price.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getSellPrice(BASE_CURRENCY, FIAT_CURRENCY),
                    resource -> resource.getSellPriceRx(BASE_CURRENCY, FIAT_CURRENCY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format(PATH_PATTERN, ApiConstants.PRICES, BASE_CURRENCY, FIAT_CURRENCY, ApiConstants.SELL);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Price data) {
            assertThat(data).isNotNull();
        }
    }

    class SpotPriceTest extends ResourceMethodTest<PricesResource, Price> {

        private static final String BASE_CURRENCY = "BTC";
        private static final String FIAT_CURRENCY = "USD";

        public SpotPriceTest() {
            super(
                    "data/spot_price_btc_usd.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getSpotPrice(BASE_CURRENCY, FIAT_CURRENCY),
                    resource -> resource.getSpotPriceRx(BASE_CURRENCY, FIAT_CURRENCY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format(PATH_PATTERN, ApiConstants.PRICES, BASE_CURRENCY, FIAT_CURRENCY, ApiConstants.SPOT);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Price data) {
            assertThat(data).isNotNull();
        }
    }

    class SpotPriceWithDateTest extends ResourceMethodTest<PricesResource, Price> {

        private static final String BASE_CURRENCY = "BTC";
        private static final String FIAT_CURRENCY = "USD";
        private static final Date date;

        static {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2017, 11, 24);
            date = calendar.getTime();
        }

        public SpotPriceWithDateTest() {
            super(
                    "data/spot_price_btc_usd.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getSpotPrice(
                            BASE_CURRENCY,
                            FIAT_CURRENCY,
                            date),
                    resource -> resource.getSpotPriceRx(
                            BASE_CURRENCY,
                            FIAT_CURRENCY,
                            date)
            );

        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String dateQuery = "?date=2017-12-24";

            final String path = String.format("%s/%s-%s/%s%s", ApiConstants.PRICES, BASE_CURRENCY, FIAT_CURRENCY, ApiConstants.SPOT, dateQuery);

            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Price data) {
            assertThat(data).isNotNull();
        }
    }

    class SpotPricesTest extends ResourceMethodTest<PricesResource, List<Price>> {

        private static final String FIAT_CURRENCY = "USD";

        public SpotPricesTest() {
            super(
                    "data/spot_prices_usd.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getSpotPrices(FIAT_CURRENCY),
                    resource -> resource.getSpotPricesRx(FIAT_CURRENCY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s", ApiConstants.PRICES, FIAT_CURRENCY, ApiConstants.SPOT);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Price> data) {
            assertThat(data).isNotNull();
            assertThat(data.size()).isGreaterThan(0);
        }
    }

    class SpotPricesWithDateTest extends ResourceMethodTest<PricesResource, List<Price>> {

        private static final String FIAT_CURRENCY = "USD";
        private static final Date date;

        static {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2017, 11, 24);
            date = calendar.getTime();
        }

        public SpotPricesWithDateTest() {
            super(
                    "data/spot_prices_usd.json",
                    Coinbase::getPricesResource,
                    resource -> resource.getSpotPrices(FIAT_CURRENCY, date),
                    resource -> resource.getSpotPricesRx(FIAT_CURRENCY, date)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s?date=2017-12-24", ApiConstants.PRICES, FIAT_CURRENCY, ApiConstants.SPOT);

            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Price> data) {
            assertThat(data).isNotNull();
            assertThat(data.size()).isGreaterThan(0);
        }
    }
}
