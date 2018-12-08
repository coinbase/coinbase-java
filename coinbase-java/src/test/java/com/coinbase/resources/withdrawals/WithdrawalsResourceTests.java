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

package com.coinbase.resources.withdrawals;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.resources.trades.TradesTests;
import com.coinbase.util.Resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import okio.Buffer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Suite.SuiteClasses({
        WithdrawalsResourceTests.ListWithdrawalsTest.class,
        WithdrawalsResourceTests.WithdrawalsPaginationTest.class,
        WithdrawalsResourceTests.ShowWithdrawalTest.class,
        WithdrawalsResourceTests.PlaceWithdrawalTest.class,
        WithdrawalsResourceTests.CommitWithdrawalTest.class
})
@RunWith(Suite.class)
public interface WithdrawalsResourceTests extends TradesTests {

    class ListWithdrawalsTest extends TradesTests.ListTradesTest<WithdrawalsResource, Withdrawal> {

        public ListWithdrawalsTest() {
            super(
                    ApiConstants.WITHDRAWALS,
                    "withdrawals/list_withdrawals.json",
                    Coinbase::getWithdrawalsResource,
                    WithdrawalsResource::listWithdrawals,
                    WithdrawalsResource::listWithdrawalsRx
            );
        }
    }

    class WithdrawalsPaginationTest extends TradesTests.ListTradesPagedTest<WithdrawalsResource, Withdrawal> {

        public WithdrawalsPaginationTest() {
            super(
                    ApiConstants.WITHDRAWALS,
                    "withdrawals/list_withdrawals.json",
                    Coinbase::getWithdrawalsResource,
                    WithdrawalsResource::listWithdrawals,
                    WithdrawalsResource::listWithdrawalsRx
            );
        }
    }

    class ShowWithdrawalTest extends ShowTradeTest<WithdrawalsResource, Withdrawal> {

        public ShowWithdrawalTest() {
            super(
                    ApiConstants.WITHDRAWALS,
                    "withdrawals/show_withdrawal.json",
                    Coinbase::getWithdrawalsResource,
                    WithdrawalsResource::showWithdrawal,
                    WithdrawalsResource::showWithdrawalRx
            );
        }
    }

    class PlaceWithdrawalTest extends PlaceTradeOrderTest<WithdrawalsResource, PlaceWithdrawalBody, Withdrawal> {

        public PlaceWithdrawalTest() {
            super(
                    "withdrawals/place_withdrawal.json",
                    ApiConstants.WITHDRAWALS,
                    Coinbase::getWithdrawalsResource,
                    new PlaceWithdrawalBody("100", "BTC", "payment-id"),
                    (withdrawalsResource, body) -> withdrawalsResource.placeWithdrawal(ACCOUNT_ID, body),
                    (withdrawalsResource, body) -> withdrawalsResource.placeWithdrawalRx(ACCOUNT_ID, body)
            );
        }

        @Override
        protected void requestBodyCheck(Buffer body) {
            final PlaceWithdrawalBody bodySent = Resource.parseJsonAsObject(body, PlaceWithdrawalBody.class);

            assertThat(bodySent.getAmount()).isEqualTo(orderBody.getAmount());
            assertThat(bodySent.getCurrency()).isEqualTo(orderBody.getCurrency());
            assertThat(bodySent.getPaymentMethod()).isEqualTo(orderBody.getPaymentMethod());

            assertThat(bodySent.getCommit()).isEqualTo(orderBody.getCommit());
        }
    }

    class CommitWithdrawalTest extends CommitTradeTest<WithdrawalsResource, Withdrawal> {

        public CommitWithdrawalTest() {
            super(
                    ApiConstants.WITHDRAWALS,
                    "withdrawals/commit_withdrawal.json",
                    Coinbase::getWithdrawalsResource,
                    WithdrawalsResource::commitWithdrawal,
                    WithdrawalsResource::commitWithdrawalRx
            );
        }
    }
}