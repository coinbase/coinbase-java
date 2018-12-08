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

package com.coinbase.resources.addresses;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.PageOrder;
import com.coinbase.PagedResponse.Pagination;
import com.coinbase.PaginationParams;
import com.coinbase.resources.transactions.Transaction;
import com.coinbase.util.ResourceMethodTest;
import com.coinbase.util.ResourcePaginatedMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.resources.addresses.AddressResourceTests.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 *
 */
@Suite.SuiteClasses({
        ListAddressesTest.class,
        ShowAddressTest.class,
        GetAddressTransactionsTest.class,
        GenerateAddressTest.class,
        ListAddressesPaginationTest.class,
        GetAddressTransactionsPaginationTest.class

})
@RunWith(Suite.class)
public interface AddressResourceTests {

    String ACCOUNT_ID = "1234";
    String ADDRESS_ID = "5678";

    class ListAddressesTest extends ResourceMethodTest<AddressResource, List<Address>> {

        public ListAddressesTest() {
            super(
                    "address/addresses_list.json",
                    Coinbase::getAddressResource,
                    resource -> resource.listAddresses(ACCOUNT_ID),
                    resource -> resource.listAddressesRx(ACCOUNT_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Address> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class ListAddressesPaginationTest extends ResourcePaginatedMethodTest<AddressResource, List<Address>> {

        private static PaginationParams paginationParams;

        static {
            paginationParams = PaginationParams.fromStartingAfter("id");
            paginationParams.setLimit(20);
            paginationParams.setOrder(PageOrder.ASC);
        }

        public ListAddressesPaginationTest() {
            super(
                    "address/addresses_list.json",
                    Coinbase::getAddressResource,
                    resource -> resource.listAddresses(ACCOUNT_ID, paginationParams),
                    resource -> resource.listAddressesRx(ACCOUNT_ID, paginationParams),
                    paginationParams
            );
        }

        @Override
        protected void responsePaginationCheck(Pagination pagination) {
            assertThat(pagination).isNotNull();
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES);
            assertThat(request.getPath().split("\\?")[0]).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Address> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class ShowAddressTest extends ResourceMethodTest<AddressResource, Address> {

        public ShowAddressTest() {
            super(
                    "address/address.json",
                    Coinbase::getAddressResource,
                    resource -> resource.showAddress(ACCOUNT_ID, ADDRESS_ID),
                    resource -> resource.showAddressRx(ACCOUNT_ID, ADDRESS_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES, ADDRESS_ID);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Address data) {
            assertThat(data).isNotNull();
            assertThat(data.getName()).isNotNull();
            assertThat(data.getNetwork()).isEqualToIgnoringCase("bitcoin");
        }
    }

    class GetAddressTransactionsTest extends ResourceMethodTest<AddressResource, List<Transaction>> {

        public GetAddressTransactionsTest() {
            super(
                    "address/address_transactions.json",
                    Coinbase::getAddressResource,
                    resource -> resource.getAddressTransactions(ACCOUNT_ID, ADDRESS_ID),
                    resource -> resource.getAddressTransactionsRx(ACCOUNT_ID, ADDRESS_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES, ADDRESS_ID, ApiConstants.TRANSACTIONS);
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Transaction> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class GetAddressTransactionsPaginationTest extends ResourceMethodTest<AddressResource, List<Transaction>> {

        private static PaginationParams paginationParams;

        static {
            paginationParams = PaginationParams.fromStartingAfter("id");
            paginationParams.setLimit(20);
            paginationParams.setOrder(PageOrder.ASC);
        }

        public GetAddressTransactionsPaginationTest() {
            super(
                    "address/address_transactions.json",
                    Coinbase::getAddressResource,
                    resource -> resource.getAddressTransactions(ACCOUNT_ID, ADDRESS_ID, paginationParams),
                    resource -> resource.getAddressTransactionsRx(ACCOUNT_ID, ADDRESS_ID, paginationParams)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES, ADDRESS_ID, ApiConstants.TRANSACTIONS);
            assertThat(request.getPath().split("\\?")[0]).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Transaction> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class GenerateAddressTest extends ResourceMethodTest<AddressResource, Address> {

        public GenerateAddressTest() {
            super(
                    "address/address.json",
                    Coinbase::getAddressResource,
                    resource -> resource.generateAddress(ACCOUNT_ID, "new address"),
                    resource -> resource.generateAddressRx(ACCOUNT_ID, "new address")
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = String.format("%s/%s/%s", ApiConstants.ACCOUNTS, ACCOUNT_ID, ApiConstants.ADDRESSES);
            assertThat(request.getPath()).endsWith(path);
            assertThat(request.getMethod()).isEqualTo("POST");
        }

        @Override
        protected void responseCheck(Address data) {
            assertThat(data).isNotNull();
            assertThat(data.getName()).isNotNull();
            assertThat(data.getNetwork()).isEqualToIgnoringCase("bitcoin");
        }
    }
}