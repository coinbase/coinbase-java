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

package com.coinbase.resources.transactions;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.PageOrder;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.resources.transactions.Transaction.ExpandField;
import com.coinbase.util.Resource;
import com.coinbase.util.ResourceMethodTest;
import com.coinbase.util.ResourcePaginatedMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.resources.transactions.TransactionsResourceTests.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link TransactionsResource}.
 */
@Suite.SuiteClasses({
        ListTransactionsTest.class,
        ShowTransactionTest.class,
        SendMoneyTest.class,
        RequestMoneyTest.class,
        CompleteRequestMoneyTest.class,
        ResendMoneyRequestTest.class,
        CancelMoneyRequestTest.class,
        ListTransactionsPaginationTest.class
})
@RunWith(Suite.class)
public interface TransactionsResourceTests {

    String ACCOUNT_ID = "account1234";

    class ListTransactionsTest extends ResourceMethodTest<TransactionsResource, List<Transaction>> {

        public ListTransactionsTest() {
            super(
                    "transactions/list_transactions.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.listTransactions(ACCOUNT_ID, ExpandField.ALL, ExpandField.BUY),
                    transactionsResource -> transactionsResource.listTransactionsRx(ACCOUNT_ID, ExpandField.ALL, ExpandField.BUY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/"
                    + ApiConstants.TRANSACTIONS + "?expand[]=all";
            assertThat(request.getPath()).endsWith(path);

            assertThat(request.getMethod()).isEqualTo("GET");
        }

        @Override
        protected void responseCheck(List<Transaction> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class ListTransactionsPaginationTest extends ResourcePaginatedMethodTest<TransactionsResource, List<Transaction>> {

        private static PaginationParams paginationParams;

        static {
            paginationParams = PaginationParams.fromStartingAfter("id");
            paginationParams.setLimit(20);
            paginationParams.setOrder(PageOrder.ASC);
        }

        public ListTransactionsPaginationTest() {
            super(
                    "transactions/list_transactions.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.listTransactions(ACCOUNT_ID, paginationParams, ExpandField.ALL, ExpandField.BUY),
                    transactionsResource -> transactionsResource.listTransactionsRx(ACCOUNT_ID, paginationParams, ExpandField.ALL, ExpandField.BUY),
                    paginationParams
            );
        }

        @Override
        protected void responsePaginationCheck(PagedResponse.Pagination pagination) {
            assertThat(pagination).isNotNull();
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/"
                    + ApiConstants.TRANSACTIONS;
            assertThat(request.getPath().split("\\?")[0]).endsWith(path);
            assertThat(request.getPath()).contains("expand[]=all");

            assertThat(request.getMethod()).isEqualTo("GET");
        }

        @Override
        protected void responseCheck(List<Transaction> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class ShowTransactionTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        private static final String TRANSACTION_ID = "12345";

        public ShowTransactionTest() {
            super(
                    "transactions/show_transaction.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.showTransaction(ACCOUNT_ID, TRANSACTION_ID, ExpandField.APPLICATION),
                    resource -> resource.showTransactionRx(ACCOUNT_ID, TRANSACTION_ID, ExpandField.APPLICATION)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "/" + TRANSACTION_ID
                    + "?expand[]=" + ExpandField.APPLICATION.getCode();
            assertThat(request.getPath()).endsWith(path);

            assertThat(request.getMethod()).isEqualTo("GET");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();
        }
    }

    class SendMoneyTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        private static final SendMoneyRequest sendMoneyRequest = new SendMoneyRequest("test@coinbase.com", "1.0", "BTC");
        private static final String TWO_FACTOR_AUTH_TOKEN = "1234";

        public SendMoneyTest() {
            super(
                    "transactions/send_money_confirmed.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.sendMoney(ACCOUNT_ID, TWO_FACTOR_AUTH_TOKEN, sendMoneyRequest, ExpandField.FROM),
                    resource -> resource.sendMoneyRx(ACCOUNT_ID, TWO_FACTOR_AUTH_TOKEN, sendMoneyRequest, ExpandField.FROM)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "?expand[]=" + ExpandField.FROM.getCode();
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
            assertThat(request.getHeader(ApiConstants.Headers.CB_2_FA_TOKEN)).isEqualTo(TWO_FACTOR_AUTH_TOKEN);

            final HashMap<String, String> requestMap = Resource.parseJsonAsHashMap(request.getBody());

            assertThat(requestMap.get("type")).isEqualTo("send");
            assertThat(requestMap.get("to")).isEqualTo(sendMoneyRequest.getTo());
            assertThat(requestMap.get("amount")).isEqualTo(sendMoneyRequest.getAmount());
            assertThat(requestMap.get("currency")).isEqualTo(sendMoneyRequest.getCurrency());
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();
        }
    }

    class RequestMoneyTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        private static final MoneyRequest moneyRequest = new MoneyRequest("user@coinbase.com", "1.0", "BTC");

        public RequestMoneyTest() {
            super(
                    "transactions/request_money.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.requestMoney(ACCOUNT_ID, moneyRequest, ExpandField.TO),
                    resource -> resource.requestMoneyRx(ACCOUNT_ID, moneyRequest, ExpandField.TO)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "?expand[]=" + ExpandField.TO.getCode();
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");

            final HashMap<String, String> requestMap = Resource.parseJsonAsHashMap(request.getBody());

            assertThat(requestMap.get("type")).isEqualTo("request");
            assertThat(requestMap.get("to")).isEqualTo(moneyRequest.getTo());
            assertThat(requestMap.get("amount")).isEqualTo(moneyRequest.getAmount());
            assertThat(requestMap.get("currency")).isEqualTo(moneyRequest.getCurrency());
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();
        }
    }

    class CompleteRequestMoneyTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        private static final String TRANSACTION_ID = "12345";

        public CompleteRequestMoneyTest() {
            super(
                    "transactions/send_money_confirmed.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.completeMoneyRequest(ACCOUNT_ID, TRANSACTION_ID, ExpandField.BUY),
                    resource -> resource.completeMoneyRequestRx(ACCOUNT_ID, TRANSACTION_ID, ExpandField.BUY)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "/" + TRANSACTION_ID
                    + "/" + ApiConstants.COMPLETE + "?expand[]=" + ExpandField.BUY.getCode();
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();
        }
    }

    class ResendMoneyRequestTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        private static final String TRANSACTION_ID = "12345";

        public ResendMoneyRequestTest() {
            super(
                    "transactions/request_money.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.resendMoneyRequest(ACCOUNT_ID, TRANSACTION_ID, ExpandField.SELL),
                    resource -> resource.resendMoneyRequestRx(ACCOUNT_ID, TRANSACTION_ID, ExpandField.SELL)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "/" + TRANSACTION_ID
                    + "/" + ApiConstants.RESEND
                    + "?expand[]=" + ExpandField.SELL.getCode();
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();
        }
    }

    class CancelMoneyRequestTest extends ResourceMethodTest<TransactionsResource, Void> {

        private static final String TRANSACTION_ID = "12345";

        public CancelMoneyRequestTest() {
            super(
                    "empty.json",
                    Coinbase::getTransactionsResource,
                    resource -> resource.cancelMoneyRequest(ACCOUNT_ID, TRANSACTION_ID),
                    resource -> resource.cancelMoneyRequestRx(ACCOUNT_ID, TRANSACTION_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + ApiConstants.TRANSACTIONS + "/" + TRANSACTION_ID;

            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("DELETE");
        }

        @Override
        protected void responseCheck(Void data) {
            // Skip.
        }
    }

}