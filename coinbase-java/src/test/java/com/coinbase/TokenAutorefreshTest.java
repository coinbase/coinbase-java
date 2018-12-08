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

package com.coinbase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.coinbase.errors.CoinbaseException;
import com.coinbase.resources.accounts.Account;
import com.coinbase.resources.auth.AccessToken;
import com.coinbase.resources.auth.RefreshTokenRequest;
import com.coinbase.util.CurrentThreadExecutorService;
import com.coinbase.util.Resource;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.ApiConstants.ACCESS_TOKEN;
import static com.coinbase.ApiConstants.ACCOUNTS;
import static com.coinbase.ApiConstants.Headers.AUTHORIZATION;
import static com.coinbase.Coinbase.KEY_LOGIN_CSRF_TOKEN;
import static com.coinbase.Coinbase.PREFS_NAME;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(sdk = 26, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class TokenAutorefreshTest {

    private final static String UPDATED_ACCESS_TOKEN = "access_token_value";
    private final static String UPDATED_REFRESH_TOKEN = "refresh_token_value";
    private final static String TOKENS_FILE = "auth/get_tokens.json";
    private final static String EXPIRED_ACCESS_TOKEN = "token";
    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String CLIENT_ID = "clientId";
    private final static String CLIENT_SECRET = "clientSecret";

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();

    @Mock
    private Coinbase.TokenListener tokenListener;

    private Coinbase coinbase;
    private Gson gson;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final URL url = mockWebServer.url("/").url();
        coinbase = CoinbaseBuilder.withTokenAutoRefresh(RuntimeEnvironment.application,
                CLIENT_ID, CLIENT_SECRET, EXPIRED_ACCESS_TOKEN, REFRESH_TOKEN, tokenListener)
                .withBaseApiURL(url)
                .withBaseOAuthURL(url)
                .withHttpExecutorService(new CurrentThreadExecutorService())
                .build();

        // Gson for parsing.
        gson = Coinbase.createGsonBuilder().create();
    }

    @Test
    public void testTokenRefreshedOnUnauthorizedResponse() throws IOException, InterruptedException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(request -> new MockResponse()
                .setResponseCode(200)
                .setBody(Resource.readResource(TOKENS_FILE, this))));

        // When
        PagedResponse<Account> accounts = coinbase.getAccountResource().getAccounts().execute();

        // Then
        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
        assertThat(accounts).isNotNull();

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);

        checkRequestIsRefreshToken(mockWebServer.takeRequest());

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
    }

    @Test
    public void testListenerNotifiedOnTokenRefresh() throws IOException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(request -> new MockResponse()
                .setResponseCode(200)
                .setBody(Resource.readResource(TOKENS_FILE, this))));

        // When
        PagedResponse<Account> accounts = coinbase.getAccountResource().getAccounts().execute();

        // Then
        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
        assertThat(accounts).isNotNull();
        verify(tokenListener).onNewTokensAvailable(any(AccessToken.class));
    }

    @Test(expected = CoinbaseException.class)
    public void testRefreshTokenFailedRequestNotRepeated() throws IOException, InterruptedException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(request -> new MockResponse()
                .setResponseCode(401)
                .setBody(Resource.readResource("auth/oauth_error.json", this))));

        // When
        try {
            coinbase.getAccountResource().getAccounts().execute();
        } catch (CoinbaseException e) {
            // Then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(2);

            checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);
            checkRequestIsRefreshToken(mockWebServer.takeRequest());
            throw e;
        }
        Assert.fail("Should throw CoinbaseOAuthException exception");
    }

    @Test(expected = ConnectException.class)
    public void testRefreshNoNetworkIoExceptionThrown() throws IOException {
        // Given
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                try {
                    //shut down after first request
                    mockWebServer.shutdown();
                    return new MockResponse().setResponseCode(401)
                            .setBody(Resource.readResource("auth/oauth_error.json", this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new MockResponse().setResponseCode(404);
            }

        });

        // When
        try {
            coinbase.getAccountResource().getAccounts().execute();
        } catch (IOException e) {

            // Then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
            assertTrue(e.getClass() == ConnectException.class);
            verify(tokenListener, never()).onRefreshFailed(any());
            throw e;
        }
        Assert.fail("Should throw CoinbaseOAuthException exception");
    }

    @Test(expected = CoinbaseException.class)
    public void testRefreshWithInvalidTokenFailedListenerNotified() throws IOException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(request -> new MockResponse()
                .setResponseCode(401)
                .setBody(Resource.readResource("auth/oauth_error.json", this))));

        // When
        try {
            coinbase.getAccountResource().getAccounts().execute();
        } catch (CoinbaseException e) {
            // Then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(2);

            verify(tokenListener).onRefreshFailed(any());
            throw e;
        }
        Assert.fail("Should throw CoinbaseOAuthException exception");
    }

    @Test
    public void testTokenRefreshedOnceOnMultipleUnauthorizedResponses() throws InterruptedException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(request -> {
            Thread.sleep(100);
            return new MockResponse()
                    .setResponseCode(200)
                    .setBody(Resource.readResource(TOKENS_FILE, this));
        }));
        CountDownLatch countDownLatch = new CountDownLatch(2);

        // When
        performGetAccountsInNewThread(countDownLatch::countDown);
        performGetAccountsInNewThread(countDownLatch::countDown);
        countDownLatch.await();

        // Then
        assertThat(mockWebServer.getRequestCount()).isEqualTo(5);

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);
        checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);

        checkRequestIsRefreshToken(mockWebServer.takeRequest());

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
    }

    @Test
    public void testTokenRefreshedOnceOnUnauthorizedAndRevokedResponses() throws InterruptedException, IOException {
        // Given
        mockWebServer.enqueue(new MockResponse().setResponseCode(401).setBody(Resource.readResource("auth/expired_token_error.json", this)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(401).setBody(Resource.readResource("auth/token_revoked_error.json", this)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(Resource.readResource(TOKENS_FILE, this)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(Resource.readResource("accounts/accounts_list.json", this)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(Resource.readResource("accounts/accounts_list.json", this)));

        // When
        CountDownLatch countDownLatch = new CountDownLatch(2);
        performGetAccountsInNewThread(countDownLatch::countDown);
        performGetAccountsInNewThread(countDownLatch::countDown);
        countDownLatch.await();

        // Then
        assertThat(mockWebServer.getRequestCount()).isEqualTo(5);

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);
        checkRequestIsGetAccounts(mockWebServer.takeRequest(), EXPIRED_ACCESS_TOKEN);

        checkRequestIsRefreshToken(mockWebServer.takeRequest());

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
    }

    @Test
    public void testTokenValidNoRefreshHappened() throws InterruptedException, IOException {
        // Given
        mockWebServer.setDispatcher(new MockDispatcher(null));
        coinbase.setAccessToken(UPDATED_ACCESS_TOKEN);

        // When
        PagedResponse<Account> accounts = coinbase.getAccountResource().getAccounts().execute();

        // Then
        assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        assertThat(accounts).isNotNull();

        checkRequestIsGetAccounts(mockWebServer.takeRequest(), UPDATED_ACCESS_TOKEN);
    }

    @Test
    public void testTokenRevokedListenerNotified() throws IOException {
        // Given
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        // When
        coinbase.getAuthResource().revokeToken(ACCESS_TOKEN).execute();

        // Then
        verify(tokenListener).onTokenRevoked();
    }

    @Test
    public void testTokenReceivedOnLoginListenerNotified() throws IOException {
        // Given
        String csfrToken = "token";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(Resource.readResource("auth/get_tokens.json", this)));
        Intent resultIntent = mock(Intent.class);
        RuntimeEnvironment.application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_LOGIN_CSRF_TOKEN, csfrToken).commit();
        when(resultIntent.getData()).thenReturn(Uri.parse("https://example.com?state=token&code=auth_code"));

        // When
        coinbase.completeAuthorization(resultIntent).execute();

        // Then
        ArgumentCaptor<AccessToken> tokenCaptor = ArgumentCaptor.forClass(AccessToken.class);
        verify(tokenListener).onNewTokensAvailable(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getAccessToken()).isEqualTo("access_token_value");
        assertThat(tokenCaptor.getValue().getRefreshToken()).isEqualTo("refresh_token_value");
    }

    private void performGetAccountsInNewThread(Runnable postAction) {
        new Thread(() -> {
            try {
                coinbase.getAccountResource().getAccounts().execute();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                postAction.run();
            }
        }).start();
    }

    private void checkRequestIsRefreshToken(RecordedRequest recordedRequest) {
        RefreshTokenRequest requestBody = gson.fromJson(recordedRequest.getBody().readUtf8(), RefreshTokenRequest.class);
        assertThat(recordedRequest.getHeader(AUTHORIZATION)).isEqualToIgnoringCase("Bearer " + EXPIRED_ACCESS_TOKEN);
        assertThat(requestBody.clientId).isEqualTo(CLIENT_ID);
        assertThat(requestBody.clientSecret).isEqualTo(CLIENT_SECRET);
        assertThat(requestBody.grantType).isEqualTo(ApiConstants.REFRESH_TOKEN);
        assertThat(requestBody.refreshToken).isEqualTo(REFRESH_TOKEN);
    }

    private void checkRequestIsGetAccounts(RecordedRequest recordedRequest, String accessToken) {
        assertThat(recordedRequest.getHeader(AUTHORIZATION)).isEqualToIgnoringCase("Bearer " + accessToken);
        assertThat(recordedRequest.getPath()).endsWith(ACCOUNTS);
    }

    @Ignore
    private static class MockDispatcher extends okhttp3.mockwebserver.Dispatcher {

        private final OauthRequestDispatcher oauthRequestDispatcher;

        private MockDispatcher(OauthRequestDispatcher oauthRequestDispatcher) {

            this.oauthRequestDispatcher = oauthRequestDispatcher;
        }

        @Override
        public MockResponse dispatch(RecordedRequest request) {
            try {
                if (request.getPath().contains(ACCOUNTS)) {
                    if (request.getHeader(AUTHORIZATION).equals("Bearer " + UPDATED_ACCESS_TOKEN)) {
                        return new MockResponse().setResponseCode(200)
                                .setBody(Resource.readResource("accounts/accounts_list.json", this));
                    } else {
                        return new MockResponse().setResponseCode(401)
                                .setBody(Resource.readResource("auth/expired_token_error.json", this));
                    }
                } else if (request.getPath().contains(ApiConstants.OAuth.OAUTH)) {
                    return oauthRequestDispatcher.dispatch(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new MockResponse().setResponseCode(404);
        }

        @Ignore
        interface OauthRequestDispatcher {
            MockResponse dispatch(RecordedRequest request) throws Exception;
        }
    }
}
