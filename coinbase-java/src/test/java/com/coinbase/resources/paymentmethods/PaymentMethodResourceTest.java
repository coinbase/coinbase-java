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

package com.coinbase.resources.paymentmethods;

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.PageOrder;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.resources.paymentmethods.models.PaymentMethod;
import com.coinbase.util.ResourceMethodTest;
import com.coinbase.util.ResourcePaginatedMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.List;

import okhttp3.mockwebserver.RecordedRequest;

import static junit.framework.Assert.assertFalse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link PaymentMethodResource} methods.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PaymentMethodResourceTest.GetPaymentMethodsTest.class,
        PaymentMethodResourceTest.GetPaymentMethodTest.class,
        PaymentMethodResourceTest.GetPaymentMethodsPaginationTest.class
})
public interface PaymentMethodResourceTest {

    String PAYMENT_ID = "id";

    class GetPaymentMethodsTest extends ResourceMethodTest<PaymentMethodResource, List<PaymentMethod>> {

        public GetPaymentMethodsTest() {
            super("payment_methods/payment_methods.json",
                    Coinbase::getPaymentMethodResource,
                    PaymentMethodResource::getPaymentMethods,
                    PaymentMethodResource::getPaymentMethodsRx);
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).contains(ApiConstants.PAYMENT_METHODS);
        }

        @Override
        protected void responseCheck(List<PaymentMethod> data) {
            assertFalse(data.isEmpty());
        }
    }

    class GetPaymentMethodsPaginationTest extends ResourcePaginatedMethodTest<PaymentMethodResource, List<PaymentMethod>> {

        private static PaginationParams paginationParams;

        static {
            paginationParams = PaginationParams.fromStartingAfter("id");
            paginationParams.setLimit(20);
            paginationParams.setOrder(PageOrder.ASC);
        }

        public GetPaymentMethodsPaginationTest() {
            super("payment_methods/payment_methods.json",
                    Coinbase::getPaymentMethodResource,
                    resource -> resource.getPaymentMethods(paginationParams),
                    resource -> resource.getPaymentMethodsRx(paginationParams),
                    paginationParams);
        }

        @Override
        protected void responsePaginationCheck(PagedResponse.Pagination pagination) {
            assertThat(pagination).isNotNull();
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).contains(ApiConstants.PAYMENT_METHODS);
        }

        @Override
        protected void responseCheck(List<PaymentMethod> data) {
            assertFalse(data.isEmpty());
        }
    }

    class GetPaymentMethodTest extends ResourceMethodTest<PaymentMethodResource, PaymentMethod> {

        public GetPaymentMethodTest() {
            super("payment_methods/payment_method_by_id.json",
                    Coinbase::getPaymentMethodResource,
                    coinbase -> coinbase.getPaymentMethod(PAYMENT_ID),
                    coinbase -> coinbase.getPaymentMethodRx(PAYMENT_ID));
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).endsWith(ApiConstants.PAYMENT_METHODS + "/" + PAYMENT_ID);
        }

        @Override
        protected void responseCheck(PaymentMethod data) {
            assertThat(data).isNotNull();
        }
    }

}
