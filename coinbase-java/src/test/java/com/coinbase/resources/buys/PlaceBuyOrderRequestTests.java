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

package com.coinbase.resources.buys;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.resources.trades.TradesTests;
import com.coinbase.resources.trades.TransferOrderBody;
import com.coinbase.util.Resource;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;

import okio.Buffer;

import static com.coinbase.resources.buys.PlaceBuyOrderRequestTests.PlaceBuyOrderWithAmountTest;
import static com.coinbase.resources.buys.PlaceBuyOrderRequestTests.PlaceBuyOrderWithOptions;
import static com.coinbase.resources.buys.PlaceBuyOrderRequestTests.PlaceBuyOrderWithTotalTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        PlaceBuyOrderWithAmountTest.class,
        PlaceBuyOrderWithTotalTest.class,
        PlaceBuyOrderWithOptions.class
})
@RunWith(Suite.class)
public interface PlaceBuyOrderRequestTests extends TradesTests {

    String PAYMENT_METHOD_ID = "payment-method-1234";

    String AMOUNT = "100.0";
    String CURRENCY = "CURRENCY";

    class PlaceBuyOrderWithAmountTest extends PlaceBuyOrderTest {

        public PlaceBuyOrderWithAmountTest() {
            super(TransferOrderBody.withAmount(AMOUNT, CURRENCY, PAYMENT_METHOD_ID));
        }

        @Override
        protected void requestBodyCheck(Buffer body) {
            final HashMap<String, String> json = Resource.parseJsonAsHashMap(body);

            assertThat(json.get("amount")).isEqualTo(AMOUNT);
            assertThat(json.get("currency")).isEqualTo(CURRENCY);
            assertThat(json.get("payment_method")).isEqualTo(PAYMENT_METHOD_ID);
        }

    }

    class PlaceBuyOrderWithTotalTest extends PlaceBuyOrderTest {

        private static final String TOTAL = "1.0";

        public PlaceBuyOrderWithTotalTest() {
            super(TransferOrderBody.withTotal(TOTAL, CURRENCY, PAYMENT_METHOD_ID));
        }

        @Override
        protected void requestBodyCheck(Buffer body) {
            final HashMap<String, String> json = Resource.parseJsonAsHashMap(body);

            assertThat(json.get("total")).isEqualTo(TOTAL);
            assertThat(json.get("currency")).isEqualTo(CURRENCY);
            assertThat(json.get("payment_method")).isEqualTo(PAYMENT_METHOD_ID);
        }

    }

    class PlaceBuyOrderWithOptions extends PlaceBuyOrderTest {

        public PlaceBuyOrderWithOptions() {
            super(TransferOrderBody.withAmount(AMOUNT, CURRENCY, PAYMENT_METHOD_ID)
                    .setAgreeBtcAmountVaries(true)
                    .setQuote(true)
                    .setCommit(true)
            );
        }

        @Override
        protected void requestBodyCheck(Buffer body) {
            final TransferOrderBody bodyReceived = Resource.parseJsonAsObject(body, TransferOrderBody.class);

            assertThat(bodyReceived.getCommit()).isEqualTo(orderBody.getCommit());
            assertThat(bodyReceived.getAgreeBtcAmountVaries()).isEqualTo(orderBody.getAgreeBtcAmountVaries());
            assertThat(bodyReceived.getQuote()).isEqualTo(orderBody.getQuote());
        }

    }

    @Ignore
    abstract class PlaceBuyOrderTest extends TradesTests.PlaceTradeOrderTest<BuysResource, TransferOrderBody, Buy> {

        public PlaceBuyOrderTest(TransferOrderBody body) {
            super(
                    "buys/place_buy.json",
                    ApiConstants.BUYS,
                    Coinbase::getBuysResource,
                    body,
                    (resource, orderBody) -> resource.placeBuyOrder(ACCOUNT_ID, orderBody),
                    (resource, orderBody) -> resource.placeBuyOrderRx(ACCOUNT_ID, orderBody)
            );
        }
    }
}
