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
import com.coinbase.CoinbaseBuilder;
import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;
import com.coinbase.network.Callback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URL;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Base class for testing Resource classes. Effectively used to test
 * one method of resource for both Rx and Call approach.
 */
@Config(sdk = 26, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public abstract class ResourceMethodTest<R, T> {

    private static final String JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private final String resourceName;
    private final Function<Coinbase, R> getResource;
    private final Function<R, ApiCall<? extends CoinbaseResponse<T>>> callFactory;
    private final Function<R, Single<? extends CoinbaseResponse<T>>> observableFactory;

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();
    protected Coinbase coinbase;

    public ResourceMethodTest(String resourceName,
                              Function<Coinbase, R> getResource,
                              Function<R, ApiCall<? extends CoinbaseResponse<T>>> callFactory,
                              Function<R, Single<? extends CoinbaseResponse<T>>> observableFactory) {
        this.resourceName = resourceName;
        this.getResource = getResource;
        this.callFactory = callFactory;
        this.observableFactory = observableFactory;
    }

    @Before
    public void setUp() throws IOException {
        final URL url = mockWebServer.url("/").url();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(Resource.readResource(resourceName, this))
                .setHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8));

        coinbase = CoinbaseBuilder.withPublicDataAccess(RuntimeEnvironment.application)
                .withBaseApiURL(url)
                .withBaseApiURL(url)
                .withBaseOAuthURL(url)
                .withHttpExecutorService(new CurrentThreadExecutorService())
                .build();
    }

    @Test
    public void shouldHandleGetDataWithCallback() throws Exception {
        final R resource = getResource.apply(coinbase);

        final Callback callback = mock(Callback.class);
        final ArgumentCaptor captor = ArgumentCaptor.forClass(Object.class);

        //noinspection unchecked
        callFactory.apply(resource).enqueue(callback);

        //noinspection unchecked
        verify(callback).onSuccess(captor.capture());

        requestCheck();

        responseCheck(captor);
    }

    @Test
    public void shouldHandleGetDataWithRx() throws Exception {
        final R resource = getResource.apply(coinbase);

        //noinspection unchecked
        final SingleObserver observer = mock(SingleObserver.class);
        final ArgumentCaptor captor = ArgumentCaptor.forClass(Object.class);

        //noinspection unchecked
        observableFactory.apply(resource)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(observer);

        //noinspection unchecked
        verify(observer).onSuccess(captor.capture());

        requestCheck();

        responseCheck(captor);
    }

    protected abstract void requestCheck(RecordedRequest request);

    protected abstract void responseCheck(T data);

    void responseCheck(ArgumentCaptor captor) {
        //noinspection unchecked
        final CoinbaseResponse<T> response = (CoinbaseResponse<T>) captor.getValue();
        responseCheck(response.getData());
    }

    void requestCheck() throws InterruptedException {
        requestCheck(mockWebServer.takeRequest());
    }
}
