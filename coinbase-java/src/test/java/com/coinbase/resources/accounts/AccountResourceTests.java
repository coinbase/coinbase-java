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

package com.coinbase.resources.accounts;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.PageOrder;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.util.Resource;
import com.coinbase.util.ResourceMethodTest;
import com.coinbase.util.ResourcePaginatedMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.resources.accounts.AccountResourceTests.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 *
 */
@Suite.SuiteClasses({
        ListAccountsTest.class,
        GetAccountTest.class,
        UpdateAccountTest.class,
        SetAccountPrimaryTest.class,
        DeleteAccountTest.class,
        ListAccountsPaginationTest.class
})
@RunWith(Suite.class)
public interface AccountResourceTests {

    String ACCOUNT_ID = "1234";
    String ACCOUNT_JSON = "accounts/account.json";

    class ListAccountsTest extends ResourceMethodTest<AccountResource, List<Account>> {

        public ListAccountsTest() {
            super(
                    "accounts/accounts_list.json",
                    Coinbase::getAccountResource,
                    AccountResource::getAccounts,
                    AccountResource::getAccountsRx
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS;
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Account> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class ListAccountsPaginationTest extends ResourcePaginatedMethodTest<AccountResource, List<Account>> {

        private static PaginationParams paginationParams;

        static {
            paginationParams = PaginationParams.fromStartingAfter("id");
            paginationParams.setLimit(20);
            paginationParams.setOrder(PageOrder.ASC);
        }

        public ListAccountsPaginationTest() {
            super(
                    "accounts/accounts_list.json",
                    Coinbase::getAccountResource,
                    accountResource -> accountResource.getAccounts(paginationParams),
                    accountResource -> accountResource.getAccountsRx(paginationParams),
                    paginationParams
            );
        }

        public void responsePaginationCheck(PagedResponse.Pagination pagination) {
            assertThat(pagination).isNotNull();
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            String path = ApiConstants.ACCOUNTS;
            assertThat(request.getPath().split("\\?")[0]).endsWith(path);
        }

        @Override
        protected void responseCheck(List<Account> data) {
            assertThat(data).isNotNull();
            assertThat(data.isEmpty()).isFalse();
        }
    }

    class GetAccountTest extends ResourceMethodTest<AccountResource, Account> {
        public GetAccountTest() {
            super(
                    ACCOUNT_JSON,
                    Coinbase::getAccountResource,
                    resource -> resource.showAccount(ACCOUNT_ID),
                    resource -> resource.showAccountRx(ACCOUNT_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID;
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Account data) {
            assertThat(data).isNotNull();
            assertThat(data.getCurrency()).isNotNull();
            assertThat(data.getCurrency().getCode()).isNotNull();
            assertThat(data.getCurrency().getName()).isNotNull();
            assertThat(data.getCurrency().getColor()).isNotNull();
            assertThat(data.getCurrency().getExponent()).isNotNull();
            assertThat(data.getCurrency().getType()).isNotNull();
            assertThat(data.getCurrency().getAddressRegex()).isNotNull();
        }
    }

    class UpdateAccountTest extends ResourceMethodTest<AccountResource, Account> {

        private static final String ACCOUNT_NAME = "updated account name";

        public UpdateAccountTest() {
            super(
                    ACCOUNT_JSON,
                    Coinbase::getAccountResource,
                    resource -> resource.updateAccount(ACCOUNT_ID, ACCOUNT_NAME),
                    resource -> resource.updateAccountRx(ACCOUNT_ID, ACCOUNT_NAME)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID;
            final String nameSent = Resource.parseJsonAsHashMap(request.getBody()).get("name");

            assertThat(request.getMethod()).isEqualTo("PUT");
            assertThat(request.getPath()).endsWith(path);

            assertThat(nameSent).isEqualTo(ACCOUNT_NAME);
        }

        @Override
        protected void responseCheck(Account data) {
            assertThat(data).isNotNull();
        }
    }

    class SetAccountPrimaryTest extends ResourceMethodTest<AccountResource, Account> {

        public SetAccountPrimaryTest() {
            super(
                    ACCOUNT_JSON,
                    Coinbase::getAccountResource,
                    resource -> resource.setAccountPrimary(ACCOUNT_ID),
                    resource -> resource.setAccountPrimaryRx(ACCOUNT_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID + "/" + ApiConstants.PRIMARY;

            assertThat(request.getMethod()).isEqualTo("POST");
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Account data) {
            assertThat(data).isNotNull();
        }
    }

    class DeleteAccountTest extends ResourceMethodTest<AccountResource, Void> {

        public DeleteAccountTest() {
            super(
                    "empty.json",
                    Coinbase::getAccountResource,
                    resource -> resource.deleteAccount(ACCOUNT_ID),
                    resource -> resource.deleteAccountRx(ACCOUNT_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            final String path = ApiConstants.ACCOUNTS + "/" + ACCOUNT_ID;

            assertThat(request.getMethod()).isEqualTo("DELETE");
            assertThat(request.getPath()).endsWith(path);
        }

        @Override
        protected void responseCheck(Void data) {
            // Nothing to check.
        }
    }

}