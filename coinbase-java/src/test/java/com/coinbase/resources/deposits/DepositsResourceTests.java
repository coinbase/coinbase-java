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

package com.coinbase.resources.deposits;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.resources.trades.TradesTests;
import com.coinbase.util.Resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import okio.Buffer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        DepositsResourceTests.ListDepositsTest.class,
        DepositsResourceTests.DepositsPaginationTest.class,
        DepositsResourceTests.ShowDepositTest.class,
        DepositsResourceTests.PlaceDepositTest.class,
        DepositsResourceTests.CommitDepositTest.class
})
@RunWith(Suite.class)
public interface DepositsResourceTests extends TradesTests {

    class ListDepositsTest extends TradesTests.ListTradesTest<DepositsResource, Deposit> {

        public ListDepositsTest() {
            super(
                    ApiConstants.DEPOSITS, "deposits/list_deposits.json",
                    Coinbase::getDepositsResource,
                    DepositsResource::listDeposits,
                    DepositsResource::listDepositsRx
            );
        }
    }

    class DepositsPaginationTest extends TradesTests.ListTradesPagedTest<DepositsResource, Deposit> {

        public DepositsPaginationTest() {
            super(
                    ApiConstants.DEPOSITS, "deposits/list_deposits.json",
                    Coinbase::getDepositsResource,
                    DepositsResource::listDeposits,
                    DepositsResource::listDepositsRx
            );
        }
    }

    class ShowDepositTest extends ShowTradeTest<DepositsResource, Deposit> {

        public ShowDepositTest() {
            super(
                    ApiConstants.DEPOSITS,
                    "deposits/show_deposit.json",
                    Coinbase::getDepositsResource,
                    DepositsResource::showDeposit,
                    DepositsResource::showDepositRx
            );
        }

        @Override
        protected void responseCheck(Deposit data) {
            super.responseCheck(data);

            assertThat(data.getInstant()).isNotNull();
        }
    }

    class PlaceDepositTest extends PlaceTradeOrderTest<DepositsResource, PlaceDepositBody, Deposit> {

        public PlaceDepositTest() {
            super(
                    "deposits/place_deposit.json",
                    ApiConstants.DEPOSITS,
                    Coinbase::getDepositsResource,
                    new PlaceDepositBody("BTC", "payment-id", "100"),
                    (depositsResource, body) -> depositsResource.placeDeposit(ACCOUNT_ID, body),
                    (depositsResource, body) -> depositsResource.placeDepositRx(ACCOUNT_ID, body)
            );
        }

        @Override
        protected void requestBodyCheck(Buffer body) {
            final PlaceDepositBody bodySent = Resource.parseJsonAsObject(body, PlaceDepositBody.class);

            assertThat(bodySent.getAmount()).isEqualTo(orderBody.getAmount());
            assertThat(bodySent.getCurrency()).isEqualTo(orderBody.getCurrency());
            assertThat(bodySent.getPaymentMethod()).isEqualTo(orderBody.getPaymentMethod());

            assertThat(bodySent.getCommit()).isEqualTo(orderBody.getCommit());
        }
    }

    class CommitDepositTest extends CommitTradeTest<DepositsResource, Deposit> {

        public CommitDepositTest() {
            super(
                    ApiConstants.DEPOSITS,
                    "deposits/commit_deposit.json",
                    Coinbase::getDepositsResource,
                    DepositsResource::commitDeposit,
                    DepositsResource::commitDepositRx
            );
        }
    }

}