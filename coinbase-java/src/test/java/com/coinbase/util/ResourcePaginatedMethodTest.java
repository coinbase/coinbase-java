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

package com.coinbase.util;

import com.coinbase.Coinbase;
import com.coinbase.CoinbaseResponse;
import com.coinbase.PagedResponse;
import com.coinbase.PaginationParams;
import com.coinbase.network.ApiCall;

import org.mockito.ArgumentCaptor;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ResourcePaginatedMethodTest<R, T> extends ResourceMethodTest<R, T> {

    private PaginationParams paginationParams;

    public ResourcePaginatedMethodTest(String resourceName,
                                       Function<Coinbase, R> getResource,
                                       Function<R, ApiCall<? extends CoinbaseResponse<T>>> callFactory,
                                       Function<R, Single<? extends CoinbaseResponse<T>>> observableFactory,
                                       PaginationParams paginationParams) {
        super(resourceName, getResource, callFactory, observableFactory);
        this.paginationParams = paginationParams;
    }

    protected abstract void responsePaginationCheck(PagedResponse.Pagination pagination);

    @Override
    void responseCheck(ArgumentCaptor captor) {
        //noinspection unchecked
        checkResponsePaginationParams((CoinbaseResponse<T>) captor.getValue());
        super.responseCheck(captor);
    }

    @Override
    void requestCheck() throws InterruptedException {
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        checkRequestPaginationParams(recordedRequest);
        requestCheck(recordedRequest);
    }

    private void checkResponsePaginationParams(CoinbaseResponse<T> response) {
        if (response instanceof PagedResponse) {
            final PagedResponse pagedResponse = (PagedResponse) response;
            responsePaginationCheck(pagedResponse.getPagination());
        }
    }

    private void checkRequestPaginationParams(RecordedRequest recordedRequest) {
        if (paginationParams != null) {
            String requestPath = recordedRequest.getPath();
            Map<String, String> params = paginationParams.toQueryMap();
            for (String key : params.keySet()) {
                assertThat(requestPath).contains(key + '=' + params.get(key));
            }
        }
    }

}
