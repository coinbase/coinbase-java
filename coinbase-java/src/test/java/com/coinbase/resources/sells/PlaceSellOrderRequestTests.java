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

package com.coinbase.resources.sells;

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

import static com.coinbase.resources.sells.PlaceSellOrderRequestTests.PlaceSellOrderWithAmountTest;
import static com.coinbase.resources.sells.PlaceSellOrderRequestTests.PlaceSellOrderWithOptions;
import static com.coinbase.resources.sells.PlaceSellOrderRequestTests.PlaceSellOrderWithTotalTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        PlaceSellOrderWithAmountTest.class,
        PlaceSellOrderWithTotalTest.class,
        PlaceSellOrderWithOptions.class
})
@RunWith(Suite.class)
public interface PlaceSellOrderRequestTests extends TradesTests {

    String PAYMENT_METHOD_ID = "payment-method-1234";

    String AMOUNT = "100.0";
    String CURRENCY = "CURRENCY";

    class PlaceSellOrderWithAmountTest extends PlaceSellOrderTest {

        public PlaceSellOrderWithAmountTest() {
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

    class PlaceSellOrderWithTotalTest extends PlaceSellOrderTest {

        private static final String TOTAL = "1.0";

        public PlaceSellOrderWithTotalTest() {
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

    class PlaceSellOrderWithOptions extends PlaceSellOrderTest {

        public PlaceSellOrderWithOptions() {
            super(TransferOrderBody.withAmount(AMOUNT, CURRENCY, PAYMENT_METHOD_ID)
                    .setCommit(true)
                    .setAgreeBtcAmountVaries(true)
                    .setQuote(true)
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
    abstract class PlaceSellOrderTest extends TradesTests.PlaceTradeOrderTest<SellsResource, TransferOrderBody, Sell> {

        PlaceSellOrderTest(TransferOrderBody body) {
            super(
                    "sells/place_sell.json",
                    ApiConstants.SELLS,
                    Coinbase::getSellsResource,
                    body,
                    (sellsResource, orderBody) -> sellsResource.placeSellOrder(ACCOUNT_ID, orderBody),
                    (sellsResource, orderBody) -> sellsResource.placeSellOrderRx(ACCOUNT_ID, orderBody)
            );
        }
    }
}
