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

import com.coinbase.Coinbase;
import com.coinbase.resources.accounts.Account;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.transactions.entities.CryptoAddress;
import com.coinbase.resources.transactions.entities.CryptoNetwork;
import com.coinbase.resources.transactions.entities.EmailResource;
import com.coinbase.resources.users.User;
import com.coinbase.util.ResourceMethodTest;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.resources.transactions.DynamicResourceTransactionsTest.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for different kinds of transaction entities (from/to) deserialization.
 */
@Suite.SuiteClasses({
        FromUserTest.class,
        ToUserTest.class,
        FromAccountTest.class,
        ToAccountTest.class,
        FromEmailTest.class,
        ToEmailTest.class,
        FromBtcAddressTest.class,
        FromBtcNetworkTest.class,
        ToBtcAddress.class,
        WithApplicationTest.class
})
@RunWith(Suite.class)
public interface DynamicResourceTransactionsTest {

    String ACCOUNT_ID = "12345";
    String TRANSACTION_ID = "12345";

    class ToUserTest extends TransactionTest {

        public ToUserTest() {
            super("transactions/transaction_to_user.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource to = data.getTo();
            assertThat(to)
                    .isNotNull()
                    .isInstanceOf(User.class);

            assertThat(to.getId()).isNotNull();
            assertThat(to.getResource())
                    .isNotNull()
                    .isEqualTo("user");

            assertThat(to.getResourcePath())
                    .isNotNull()
                    .startsWith("/v2/users");
        }
    }

    class FromUserTest extends TransactionTest {

        public FromUserTest() {
            super("transactions/transaction_from_user.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource from = data.getFrom();
            assertThat(from)
                    .isNotNull()
                    .isInstanceOf(User.class);

            assertThat(from.getId()).isNotNull();
            assertThat(from.getResource())
                    .isNotNull()
                    .isEqualTo("user");

            assertThat(from.getResourcePath())
                    .isNotNull()
                    .startsWith("/v2/users");
        }
    }

    class FromAccountTest extends TransactionTest {
        public FromAccountTest() {
            super("transactions/transaction_from_account.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource from = data.getFrom();
            assertThat(from)
                    .isNotNull()
                    .isInstanceOf(Account.class);

            assertThat(from.getId()).isNotNull();
            assertThat(from.getResource())
                    .isNotNull()
                    .isEqualTo("account");

            assertThat(from.getResourcePath())
                    .isNotNull()
                    .startsWith("/v2/accounts");
        }
    }

    class ToAccountTest extends TransactionTest {
        public ToAccountTest() {
            super("transactions/transaction_to_account.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource to = data.getTo();
            assertThat(to)
                    .isNotNull()
                    .isInstanceOf(Account.class);

            assertThat(to.getId()).isNotNull();
            assertThat(to.getResource())
                    .isNotNull()
                    .isEqualTo("account");

            assertThat(to.getResourcePath())
                    .isNotNull()
                    .startsWith("/v2/accounts");
        }
    }

    class FromEmailTest extends TransactionTest {
        public FromEmailTest() {
            super("transactions/transaction_from_email.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource from = data.getFrom();
            assertThat(from)
                    .isNotNull()
                    .isInstanceOf(EmailResource.class);

            assertThat(from.getResource())
                    .isNotNull()
                    .isEqualTo("email");

            final EmailResource emailResource = (EmailResource) from;
            assertThat(emailResource.getEmail()).isNotNull();
        }
    }

    class ToEmailTest extends TransactionTest {
        public ToEmailTest() {
            super("transactions/transaction_to_email.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource to = data.getTo();
            assertThat(to)
                    .isNotNull()
                    .isInstanceOf(EmailResource.class);

            assertThat(to.getResource())
                    .isNotNull()
                    .isEqualTo("email");

            final EmailResource emailResource = (EmailResource) to;
            assertThat(emailResource.getEmail()).isNotNull();
        }
    }

    class FromBtcAddressTest extends TransactionTest {
        public FromBtcAddressTest() {
            super("transactions/transaction_from_btc_address.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource from = data.getFrom();
            assertThat(from)
                    .isNotNull()
                    .isInstanceOf(CryptoAddress.class);

            assertThat(from.getResource())
                    .isNotNull()
                    .isEqualTo("bitcoin_address");

            assertThat(((CryptoAddress) from).getAddress()).isNotNull();
        }
    }

    class FromBtcNetworkTest extends TransactionTest {

        public FromBtcNetworkTest() {
            super("transactions/transaction_from_btc_network.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource from = data.getFrom();

            assertThat(from)
                    .isNotNull()
                    .isInstanceOf(CryptoNetwork.class);

            assertThat(from.getResource()).isEqualTo("bitcoin_network");

            final CryptoNetwork network = (CryptoNetwork) from;
            assertThat(network.getCurrency()).isNotNull();

            // Check address field.
            assertThat(data.getAddress()).isNotNull();
        }
    }

    class ToBtcAddress extends TransactionTest {
        public ToBtcAddress() {
            super("transactions/transaction_to_btc_address.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            assertThat(data).isNotNull();

            final DynamicResource to = data.getTo();
            assertThat(to)
                    .isNotNull()
                    .isInstanceOf(CryptoAddress.class);

            assertThat(to.getResource())
                    .isNotNull()
                    .isEqualTo("bitcoin_address");

            final CryptoAddress emailResource = (CryptoAddress) to;
            assertThat(emailResource.getAddress()).isNotNull();
        }
    }

    class WithApplicationTest extends TransactionTest {

        public WithApplicationTest() {
            super("transactions/transaction_with_application.json");
        }

        @Override
        protected void responseCheck(Transaction data) {
            final Application application = data.getApplication();

            assertThat(application).isNotNull();
            assertThat(application.getId()).isNotNull();
            assertThat(application.getName()).isNotNull();
            assertThat(application.getDescription()).isNotNull();
            assertThat(application.getImageUrl()).isNotNull();
            assertThat(application.getResource()).isNotNull();
            assertThat(application.getResourcePath()).isNotNull();

        }
    }

    @Ignore
    abstract class TransactionTest extends ResourceMethodTest<TransactionsResource, Transaction> {

        TransactionTest(String resourceName) {
            super(
                    resourceName,
                    Coinbase::getTransactionsResource,
                    resource -> resource.showTransaction(ACCOUNT_ID, TRANSACTION_ID),
                    resource -> resource.showTransactionRx(ACCOUNT_ID, TRANSACTION_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            // Skip.
        }

    }

}
