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

package com.coinbase.resources.trades;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.util.ResourceMethodTest;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public interface TradesTests {

    String ACCOUNT_ID = "account-1234";
    String TRADE_ID = "trade-1234";

    abstract class ListTradesTest<R extends TradesResource, T extends Trade> extends ResourceMethodTest<R, List<T>> {

        private final String tradeType;

        public ListTradesTest(
                String tradeType,
                String resourceName,
                Function<Coinbase, R> getResource,
                BiFunction<R, String, ApiCall<? extends CoinbaseResponse<List<T>>>> callFactory,
                BiFunction<R, String, Single<? extends CoinbaseResponse<List<T>>>> observableFactory
        ) {
            super(
                    resourceName,
                    getResource,
                    r -> callFactory.apply(r, ACCOUNT_ID),
                    r -> observableFactory.apply(r, ACCOUNT_ID)
            );
            this.tradeType = tradeType;
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/" + tradeType;
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<T> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    abstract class ListTradesPagedTest<R extends TradesResource, T extends Trade> extends ResourceMethodTest<R, List<T>> {

        private static final PaginationParams PAGINATION_PARAMS = PaginationParams.fromStartingAfter("next-page-id");

        static {
            PAGINATION_PARAMS.setLimit(25);
        }

        private final String tradeType;

        public ListTradesPagedTest(
                String tradeType,
                String resourceName,
                Function<Coinbase, R> getResource,
                Function3<R, String, PaginationParams, ApiCall<? extends CoinbaseResponse<List<T>>>> callFactory,
                Function3<R, String, PaginationParams, Single<? extends CoinbaseResponse<List<T>>>> observableFactory
        ) {
            super(
                    resourceName,
                    getResource,
                    r -> callFactory.apply(r, ACCOUNT_ID, PAGINATION_PARAMS),
                    r -> observableFactory.apply(r, ACCOUNT_ID, PAGINATION_PARAMS)
            );
            this.tradeType = tradeType;
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/" + tradeType;
            String pagination = "?limit=25&starting_after=next-page-id";
            assertThat(request.getPath()).endsWith(path + pagination);
        }

        @Override
        protected void responseCheck(List<T> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    abstract class ShowTradeTest<R extends TradesResource, T extends Trade> extends ResourceMethodTest<R, T> {

        private final String tradeType;

        public ShowTradeTest(
                String tradeType,
                String resourceName,
                Function<Coinbase, R> getResource,
                Function3<R, String, String, ApiCall<? extends CoinbaseResponse<T>>> callFactory,
                Function3<R, String, String, Single<? extends CoinbaseResponse<T>>> observableFactory
        ) {
            super(
                    resourceName,
                    getResource,
                    r -> callFactory.apply(r, ACCOUNT_ID, TRADE_ID),
                    r -> observableFactory.apply(r, ACCOUNT_ID, TRADE_ID)
            );
            this.tradeType = tradeType;
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + tradeType + "/" + TRADE_ID;
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(T data) {
            checkTradeFields(data);

        }

        private void checkTradeFields(Trade data) {
            assertThat(data).isNotNull();
            // Resource base fields.
            assertThat(data.getId()).isNotNull();
            assertThat(data.getResource()).isNotNull();
            assertThat(data.getResourcePath()).isNotNull();

            // Nested resources.
            assertThat(data.getPaymentMethod()).isNotNull();
            assertThat(data.getTransaction()).isNotNull();

            // MoneyHash fields.
            assertThat(data.getAmount()).isNotNull();
            assertThat(data.getFee()).isNotNull();
            assertThat(data.getSubtotal()).isNotNull();

            // Other fields.
            assertThat(data.getStatus()).isNotNull();
            assertThat(data.getPayoutAt()).isNotNull();
            assertThat(data.getCommitted()).isNotNull();
        }
    }

    abstract class CommitTradeTest<R extends TradesResource, T extends Trade> extends ResourceMethodTest<R, T> {

        private final String tradeType;

        public CommitTradeTest(
                String tradeType,
                String resourceName,
                Function<Coinbase, R> getResource,
                Function3<R, String, String, ApiCall<? extends CoinbaseResponse<T>>> callFactory,
                Function3<R, String, String, Single<? extends CoinbaseResponse<T>>> observableFactory
        ) {
            super(
                    resourceName,
                    getResource,
                    r -> callFactory.apply(r, ACCOUNT_ID, TRADE_ID),
                    r -> observableFactory.apply(r, ACCOUNT_ID, TRADE_ID)
            );
            this.tradeType = tradeType;
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID
                    + "/" + tradeType
                    + "/" + TRADE_ID + "/" + ApiConstants.COMMIT;
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
        }

        @Override
        protected void responseCheck(T data) {
            assertThat(data).isNotNull();
        }
    }

    abstract class PlaceTradeOrderTest<
            Resource extends TradesResource,
            Body extends PlaceTradeOrderBody,
            Model extends Trade>
            extends ResourceMethodTest<Resource, Model> {

        protected final Body orderBody;
        protected final String tradeType;

        public PlaceTradeOrderTest(
                String resourceName,
                String tradeType,
                Function<Coinbase, Resource> getResource,
                Body request,
                BiFunction<Resource, Body, ApiCall<? extends CoinbaseResponse<Model>>> callFactory,
                BiFunction<Resource, Body, Single<? extends CoinbaseResponse<Model>>> observableFactory) {
            super(
                    resourceName,
                    getResource,
                    r -> callFactory.apply(r, request),
                    r -> observableFactory.apply(r, request)
            );
            this.orderBody = request;
            this.tradeType = tradeType;
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/" + tradeType;
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
            requestBodyCheck(request.getBody());
        }

        @Override
        protected void responseCheck(Model data) {
            assertThat(data).isNotNull();
        }

        protected abstract void requestBodyCheck(Buffer body);
    }
}
