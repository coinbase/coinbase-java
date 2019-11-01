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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.paymentmethods.models.PaymentMethod;
import com.coinbase.resources.paymentmethods.models.PaymentMethod.ExpandField;

import io.reactivex.rxjava3.core.Single;

import static com.coinbase.resources.ExpandUtils.toValueSet;

/**
 * Payment methods API.
 *
 * @see PaymentMethod
 * @see <a href="https://developers.coinbase.com/api/v2#payment-methods">online docs:
 * Payment methods</a> for more info.
 */
public class PaymentMethodResource {

    private PaymentMethodsApi paymentMethodsApi;

    private PaymentMethodsApiRx paymentMethodsApiRx;

    public PaymentMethodResource(PaymentMethodsApi paymentMethodsApi,
                                 PaymentMethodsApiRx paymentMethodsApiRx) {
        this.paymentMethodsApi = paymentMethodsApi;
        this.paymentMethodsApiRx = paymentMethodsApiRx;
    }

    //region ApiCall methods.

    /**
     * Same as {@link #getPaymentMethods(PaginationParams, ExpandField...) getPaymentMethods( pagination, expandOptions)}
     * with default {@link PaginationParams pagination params}.
     *
     * @param expandOptions {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link ApiCall call} for getting list of payment methods.
     */
    public ApiCall<PagedResponse<PaymentMethod>> getPaymentMethods(@Nullable ExpandField... expandOptions) {
        return paymentMethodsApi.getPaymentMethods(toValueSet(expandOptions));
    }

    /**
     * Lists current user’s payment methods.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.PaymentMethods#READ wallet:payment-methods:read}</li>
     * </ul>
     *
     * @param paginationParams parameters for pagination.
     * @param expandOptions    {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link ApiCall call} for getting list of payment methods.
     * @see <a href="https://developers.coinbase.com/api/v2#list-payment-methods">
     * online docs: List Payment methods</a>.
     */
    public ApiCall<PagedResponse<PaymentMethod>> getPaymentMethods(@Nullable PaginationParams paginationParams,
                                                                   @Nullable ExpandField... expandOptions) {
        if (paginationParams == null) {
            return getPaymentMethods(expandOptions);
        }
        return paymentMethodsApi.getPaymentMethods(toValueSet(expandOptions), paginationParams.toQueryMap());
    }

    /**
     * Shows current user’s payment method.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.PaymentMethods#READ wallet:payment-methods:read}</li>
     * </ul>
     *
     * @param paymentMethodId payment method id.
     * @param expandOptions   {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link ApiCall call} for getting payment method.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-payment-method">
     * online docs: Show a payment method</a>.
     */
    public ApiCall<CoinbaseResponse<PaymentMethod>> getPaymentMethod(@NonNull String paymentMethodId,
                                                                     @Nullable ExpandField... expandOptions) {
        return paymentMethodsApi.getPaymentMethod(paymentMethodId, toValueSet(expandOptions));
    }

    /**
     * Deletes user’s payment method.
     * <p>
     * SCOPES<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.PaymentMethods#DELETE wallet:payment-methods:delete}</li>
     * </ul>
     *
     * @param paymentMethodId id of payment method to delete.
     * @return {@link ApiCall call} to notify call went through.
     */
    public ApiCall<CoinbaseResponse<Void>> deletePaymentMethod(@NonNull String paymentMethodId) {
        return paymentMethodsApi.deletePaymentMethod(paymentMethodId);
    }
    //endregion

    //region Rx Methods

    /**
     * Rx version of {@link #getPaymentMethods(ExpandField...) getPaymentMethods(ExpandField...)}.
     *
     * @param expandOptions {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link Single source} for getting list of payment methods.
     */
    public Single<PagedResponse<PaymentMethod>> getPaymentMethodsRx(@Nullable ExpandField... expandOptions) {
        return paymentMethodsApiRx.getPaymentMethods(toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #getPaymentMethods(PaginationParams, ExpandField...)
     * getPaymentMethods(PaginationParams, ExpandField...)}.
     *
     * @param paginationParams parameters for pagination.
     * @param expandOptions    {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link Single source} for getting list of payment methods.
     */
    public Single<PagedResponse<PaymentMethod>> getPaymentMethodsRx(@Nullable PaginationParams paginationParams,
                                                                    @Nullable ExpandField... expandOptions) {
        if (paginationParams == null) {
            return getPaymentMethodsRx(expandOptions);
        }
        return paymentMethodsApiRx.getPaymentMethods(toValueSet(expandOptions), paginationParams.toQueryMap());
    }

    /**
     * Rx version of {@link #getPaymentMethod(String, ExpandField...)
     * getPaymentMethod(String, ExpandField...)}.
     *
     * @param paymentMethodId payment method id.
     * @param expandOptions   {@link PaymentMethod.ExpandField expand fields} list.
     * @return {@link Single source} for getting payment method.
     */
    public Single<CoinbaseResponse<PaymentMethod>> getPaymentMethodRx(@NonNull String paymentMethodId,
                                                                      @Nullable ExpandField... expandOptions) {
        return paymentMethodsApiRx.getPaymentMethod(paymentMethodId, toValueSet(expandOptions));
    }

    /**
     * Rx version of {@link #deletePaymentMethod(String)}
     * deletePaymentMethod(String)}.
     *
     * @param paymentMethodId id of payment method to delete.
     * @return {@link Single source} to notify call went through.
     */
    public Single<CoinbaseResponse<Void>> deletePaymentMethodRx(@NonNull String paymentMethodId) {
        return paymentMethodsApiRx.deletePaymentMethod(paymentMethodId);
    }
    //endregion
}
