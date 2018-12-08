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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static com.coinbase.resources.sells.SellsResourceTests.CommitSellTest;
import static com.coinbase.resources.sells.SellsResourceTests.ListSellsTest;
import static com.coinbase.resources.sells.SellsResourceTests.SellsPaginationTest;
import static com.coinbase.resources.sells.SellsResourceTests.ShowSellTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        ListSellsTest.class,
        SellsPaginationTest.class,
        ShowSellTest.class,
        CommitSellTest.class
})
@RunWith(Suite.class)
public interface SellsResourceTests extends TradesTests {

    class ListSellsTest extends TradesTests.ListTradesTest<SellsResource, Sell> {

        public ListSellsTest() {
            super(
                    ApiConstants.SELLS,
                    "sells/list_sells.json",
                    Coinbase::getSellsResource,
                    SellsResource::listSells,
                    SellsResource::listSellsRx
            );
        }
    }

    class SellsPaginationTest extends TradesTests.ListTradesPagedTest<SellsResource, Sell> {
        public SellsPaginationTest() {
            super(
                    ApiConstants.SELLS,
                    "sells/list_sells.json",
                    Coinbase::getSellsResource,
                    SellsResource::listSells,
                    SellsResource::listSellsRx
            );
        }
    }

    class ShowSellTest extends TradesTests.ShowTradeTest<SellsResource, Sell> {

        public ShowSellTest() {
            super(
                    ApiConstants.SELLS,
                    "sells/show_sell.json",
                    Coinbase::getSellsResource,
                    SellsResource::showSell,
                    SellsResource::showSellRx
            );
        }

        @Override
        protected void responseCheck(Sell data) {
            super.responseCheck(data);

            assertThat(data.getTotal()).isNotNull();
            assertThat(data.getInstant()).isNotNull();
        }
    }

    class CommitSellTest extends TradesTests.CommitTradeTest<SellsResource, Sell> {

        public CommitSellTest() {
            super(
                    ApiConstants.SELLS, "sells/commit_sell.json",
                    Coinbase::getSellsResource,
                    SellsResource::commitSellOrder,
                    SellsResource::commitSellOrderRx
            );
        }

        @Override
        protected void responseCheck(Sell data) {
            assertThat(data).isNotNull();
        }
    }
}