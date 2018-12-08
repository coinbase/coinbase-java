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

import com.coinbase.network.ApiCall;
import com.coinbase.resources.auth.AccessToken;
import com.coinbase.resources.auth.AuthResource;
import com.coinbase.util.CurrentThreadExecutorService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.coinbase.util.Resource.parseJsonAsHashMap;
import static com.coinbase.util.Resource.readResource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for AuthMethods and saving / updating tokens in interceptors.
 */
@Config(sdk = 26, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CoinbaseAuthMethodsTest {

    private static final String TOKENS_RESOURCE = "auth/get_tokens.json";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String TEST_CLIENT = "test client";
    private static final String CLIENT_SECRET = "12345";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String REDIRECT_URI_KEY = "redirect_uri";
    private static final String CODE_KEY = "code";

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();

    private Coinbase coinbase;
    private AuthResource authResource;

    @Before
    public void setUp() {
        final URL url = mockWebServer.url("/").url();
        coinbase = CoinbaseBuilder.withPublicDataAccess(RuntimeEnvironment.application)
                .withBaseApiURL(url)
                .withBaseOAuthURL(url)
                .withHttpExecutorService(new CurrentThreadExecutorService())
                .build();

        authResource = coinbase.getAuthResource();
    }

    //region Call method tests.

    @Test
    public void shouldUpdateTokenOnGetToken() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(readResource(TOKENS_RESOURCE, this))
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final HashMap<String, String> responseBody = parseJsonAsHashMap(readResource(TOKENS_RESOURCE, this));
        final String accessToken = responseBody.get(ACCESS_TOKEN);
        final String refreshToken = responseBody.get("refresh_token");

        final String clientId = TEST_CLIENT;
        final String clientSecret = CLIENT_SECRET;
        final String authCode = "abcd";
        final String redirectUri = "coinbase-test://some_address";

        final ApiCall<AccessToken> tokenCall = authResource.getTokens(clientId, clientSecret, authCode, redirectUri);
        final AccessToken accessTokenResponse = tokenCall.execute();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final String json = recordedRequest.getBody().readUtf8();
        final HashMap<String, String> map = parseJsonAsHashMap(json);

        // Assert response.
        assertThat(accessTokenResponse.getAccessToken()).isEqualTo(accessToken);
        assertThat(accessTokenResponse.getRefreshToken()).isEqualTo(refreshToken);
        // Make sure we saved new access token to Coinbase.
        assertThat(coinbase.getAccessToken()).isEqualTo(accessToken);

        // Assert right arguments sent to server.
        assertThat(map.get(CLIENT_ID_KEY)).isEqualTo(clientId);
        assertThat(map.get(CLIENT_SECRET_KEY)).isEqualTo(clientSecret);
        assertThat(map.get(CODE_KEY)).isEqualTo(authCode);
        assertThat(map.get(REDIRECT_URI_KEY)).isEqualTo(redirectUri);
        assertThat(map.get(GRANT_TYPE_KEY)).isEqualTo(ApiConstants.AUTHORIZATION_CODE);

    }

    @Test
    public void shouldUpdateTokenOnRefreshToken() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(readResource(TOKENS_RESOURCE, this))
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final HashMap<String, String> responseBody = parseJsonAsHashMap(readResource(TOKENS_RESOURCE, this));
        final String accessToken = responseBody.get(ACCESS_TOKEN);

        final String clientId = TEST_CLIENT;
        final String clientSecret = CLIENT_SECRET;
        final String refreshToken = "refresh123";

        final ApiCall<AccessToken> tokenCall = authResource.refreshTokens(clientId, clientSecret, refreshToken);
        final AccessToken accessTokenResponse = tokenCall.execute();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final String json = recordedRequest.getBody().readUtf8();
        final HashMap<String, String> map = parseJsonAsHashMap(json);

        // Assert response.
        assertThat(accessTokenResponse.getAccessToken()).isEqualTo(accessToken);
        // Make sure we saved new access token to Coinbase.
        assertThat(coinbase.getAccessToken()).isEqualTo(accessToken);

        // Assert right arguments sent to server.
        assertThat(map.get(CLIENT_ID_KEY)).isEqualTo(clientId);
        assertThat(map.get(CLIENT_SECRET_KEY)).isEqualTo(clientSecret);
        assertThat(map.get(GRANT_TYPE_KEY)).isEqualTo(ApiConstants.REFRESH_TOKEN);
    }

    @Test
    public void shouldClearTokenOnRevoke() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{}")
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final String accessToken = "the_token";

        authResource.revokeToken(accessToken).execute();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();

        // Token is sent to server.
        final String tokenSent = parseJsonAsHashMap(recordedRequest.getBody()).get(ApiConstants.TOKEN);
        assertThat(tokenSent).isEqualTo(accessToken);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAllowRevokeTokenIfNotLoggedIn() {
        authResource.revokeToken(null);
    }

    //endregion

    //region Rx method tests.

    @Test
    public void shouldUpdateTokenOnGetTokenRx() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(readResource(TOKENS_RESOURCE, this))
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final HashMap<String, String> responseBody = parseJsonAsHashMap(readResource(TOKENS_RESOURCE, this));
        final String accessToken = responseBody.get(ACCESS_TOKEN);
        final String refreshToken = responseBody.get("refresh_token");

        final String clientId = TEST_CLIENT;
        final String clientSecret = CLIENT_SECRET;
        final String authCode = "abcd";
        final String redirectUri = "coinbase-test://some_address";

        final AccessToken response = authResource.getTokensRx(clientId, clientSecret, authCode, redirectUri).blockingGet();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final String json = recordedRequest.getBody().readUtf8();
        final HashMap<String, String> map = parseJsonAsHashMap(json);

        // Assert response.
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
        // Make sure we saved new access token to Coinbase.
        assertThat(coinbase.getAccessToken()).isEqualTo(accessToken);

        // Assert right arguments sent to server.
        assertThat(map.get(CLIENT_ID_KEY)).isEqualTo(clientId);
        assertThat(map.get(CLIENT_SECRET_KEY)).isEqualTo(clientSecret);
        assertThat(map.get("code")).isEqualTo(authCode);
        assertThat(map.get(REDIRECT_URI_KEY)).isEqualTo(redirectUri);
        assertThat(map.get(GRANT_TYPE_KEY)).isEqualTo(ApiConstants.AUTHORIZATION_CODE);
    }

    @Test
    public void shouldUpdateTokenOnRefreshTokenRx() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(readResource(TOKENS_RESOURCE, this))
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final HashMap<String, String> responseBody = parseJsonAsHashMap(readResource(TOKENS_RESOURCE, this));
        final String accessToken = responseBody.get(ACCESS_TOKEN);

        final String clientId = TEST_CLIENT;
        final String clientSecret = CLIENT_SECRET;
        final String refreshToken = "refresh123";

        final AccessToken response = authResource.refreshTokensRx(clientId, clientSecret, refreshToken).blockingGet();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final String json = recordedRequest.getBody().readUtf8();
        final HashMap<String, String> map = parseJsonAsHashMap(json);

        // Assert response.
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
        // Make sure we saved new access token to Coinbase.
        assertThat(coinbase.getAccessToken()).isEqualTo(accessToken);

        // Assert right arguments sent to server.
        assertThat(map.get(CLIENT_ID_KEY)).isEqualTo(clientId);
        assertThat(map.get(CLIENT_SECRET_KEY)).isEqualTo(clientSecret);
        assertThat(map.get(GRANT_TYPE_KEY)).isEqualTo(ApiConstants.REFRESH_TOKEN);
    }

    @Test
    public void shouldClearTokenOnRevokeRx() throws IOException, InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{}")
                .setResponseCode(200)
                .addHeader(CONTENT_TYPE, JSON_CHARSET_UTF_8)
        );

        final String accessToken = "the_token";

        authResource.revokeTokenRx(accessToken).blockingGet();

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();

        // Token is sent to server.
        final String tokenSent = parseJsonAsHashMap(recordedRequest.getBody()).get(ApiConstants.TOKEN);
        assertThat(tokenSent).isEqualTo(accessToken);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAllowRevokeTokenIfNotLoggedInRx() {
        authResource.revokeTokenRx(null);
    }

    //endregion
}