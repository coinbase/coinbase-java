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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static com.coinbase.resources.buys.BuysResourceTests.BuysPaginationTest;
import static com.coinbase.resources.buys.BuysResourceTests.CommitBuyTest;
import static com.coinbase.resources.buys.BuysResourceTests.ListBuysTest;
import static com.coinbase.resources.buys.BuysResourceTests.ShowBuyTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        ListBuysTest.class,
        BuysPaginationTest.class,
        ShowBuyTest.class,
        CommitBuyTest.class
})
@RunWith(Suite.class)
public interface BuysResourceTests extends TradesTests {

    class ListBuysTest extends TradesTests.ListTradesTest<BuysResource, Buy> {

        public ListBuysTest() {
            super(
                    ApiConstants.BUYS,
                    "buys/list_buys.json",
                    Coinbase::getBuysResource,
                    BuysResource::listBuys,
                    BuysResource::listBuysRx
            );
        }

    }

    class BuysPaginationTest extends TradesTests.ListTradesPagedTest<BuysResource, Buy> {

        public BuysPaginationTest() {
            super(
                    ApiConstants.BUYS,
                    "buys/list_buys.json",
                    Coinbase::getBuysResource,
                    BuysResource::listBuys,
                    BuysResource::listBuysRx
            );
        }
    }

    class ShowBuyTest extends TradesTests.ShowTradeTest<BuysResource, Buy> {

        public ShowBuyTest() {
            super(
                    ApiConstants.BUYS, "buys/show_buy.json",
                    Coinbase::getBuysResource,
                    BuysResource::showBuy,
                    BuysResource::showBuyRx
            );
        }

        @Override
        protected void responseCheck(Buy data) {
            super.responseCheck(data);

            assertThat(data.getTotal()).isNotNull();
            assertThat(data.getInstant()).isNotNull();
            assertThat(data.getRequiresCompletionStep()).isNotNull();
            assertThat(data.getIsFirstBuy()).isNotNull();

        }
    }

    class CommitBuyTest extends TradesTests.CommitTradeTest<BuysResource, Buy> {

        public CommitBuyTest() {
            super(
                    ApiConstants.BUYS, "buys/commit_buy.json",
                    Coinbase::getBuysResource,
                    BuysResource::commitBuyOrder,
                    BuysResource::commitBuyOrderRx
            );
        }

        @Override
        protected void responseCheck(Buy data) {
            assertThat(data).isNotNull();
        }
    }
}