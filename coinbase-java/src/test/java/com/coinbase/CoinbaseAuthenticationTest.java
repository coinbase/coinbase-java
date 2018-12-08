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

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.coinbase.resources.auth.AuthResource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class CoinbaseAuthenticationTest {

    private Application application;
    private Coinbase coinbase;
    private AuthorizationRequest authorizationRequest;

    private final String clientId = "123";
    private final String scope = "user:read";
    private final String clientSecret = "456";
    private final String redirectUriBase = "coinbase://some-app";
    private Application applicationSpy;

    @Before
    public void setUp() throws Exception {
        application = RuntimeEnvironment.application;
        applicationSpy = Mockito.spy(application);
        coinbase = new Coinbase(applicationSpy);
        coinbase.setClientIdAndSecret(clientId, clientSecret);
        authorizationRequest = new AuthorizationRequest(Uri.parse(redirectUriBase), scope);
    }

    @Test
    public void shouldHandleBeginAuthorizationWithRequest() {
        // Given
        final Context mockContext = mock(Context.class);

        // When
        final AuthorizationRequest authorizationRequest = new AuthorizationRequest(Uri.parse(redirectUriBase), scope);
        coinbase.beginAuthorization(mockContext, authorizationRequest);

        // Then
        final ArgumentCaptor<Intent> captor = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext).startActivity(captor.capture());

        final Intent intent = captor.getValue();
        assertThat(intent).isNotNull();
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);

        assertThat(intent.getData()).isNotNull();
        final Uri wantedUri = this.authorizationRequest.getAuthorizationUri(coinbase.baseOAuthUrl, clientId)
                .buildUpon()
                .appendQueryParameter("state", coinbase.getCsrfToken())
                .build();

        assertThat(intent.getData()).isEqualTo(wantedUri);
    }

    @Test
    public void shouldHandleBeginAuthorizationWithUri() {
        // Given
        final AuthorizationRequest authorizationRequest = new AuthorizationRequest(Uri.parse(redirectUriBase), scope);

        // When
        final Uri authUri = coinbase.buildAuthorizationUri(authorizationRequest);

        // Then
        assertThat(authUri).isNotNull();

        final Uri wantedUri = this.authorizationRequest.getAuthorizationUri(coinbase.baseOAuthUrl, clientId)
                .buildUpon()
                .appendQueryParameter("state", coinbase.getCsrfToken())
                .build();

        assertThat(authUri).isEqualTo(wantedUri);
    }

    @Test
    public void shouldHandleBeginAuthorization() throws Exception {
        // Given
        final Context mockContext = mock(Context.class);

        // When
        coinbase.beginAuthorization(mockContext, Uri.parse(redirectUriBase), scope);

        // Then
        final ArgumentCaptor<Intent> captor = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext).startActivity(captor.capture());

        final Intent intent = captor.getValue();
        assertThat(intent).isNotNull();
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);

        assertThat(intent.getData()).isNotNull();
        final Uri wantedUri = authorizationRequest.getAuthorizationUri(coinbase.baseOAuthUrl, clientId)
                .buildUpon()
                .appendQueryParameter("state", coinbase.getCsrfToken())
                .build();

        assertThat(intent.getData()).isEqualTo(wantedUri);
    }

    @Test
    public void shouldResetTokenWhenBeginningAuthorization() throws Exception {
        // Given
        final SharedPreferences mockPreferences = mock(SharedPreferences.class);
        final SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);

        when(applicationSpy.getSharedPreferences(Coinbase.PREFS_NAME, Context.MODE_PRIVATE))
                .thenReturn(mockPreferences);
        when(mockPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(any(), any())).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);

        // When
        coinbase.beginAuthorization(mock(Context.class), Uri.parse(redirectUriBase), scope);

        // Then
        final InOrder callSequence = inOrder(mockPreferences, mockEditor);
        // Should reset token with null.
        callSequence.verify(mockPreferences).edit();
        callSequence.verify(mockEditor).putString(eq(Coinbase.KEY_LOGIN_CSRF_TOKEN), isNull(String.class));
        callSequence.verify(mockEditor).commit();

        // Should generate and save new token.
        callSequence.verify(mockPreferences).edit();
        callSequence.verify(mockEditor).putString(eq(Coinbase.KEY_LOGIN_CSRF_TOKEN), isNotNull(String.class));
        callSequence.verify(mockEditor).commit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutData() throws Exception {
        // Given
        final Intent intent = new Intent().setData(null);

        // When
        coinbase.completeAuthorization(intent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutAuthCode() throws Exception {
        // Given
        final Uri resultUri = Uri.parse(redirectUriBase + "?q=nothing");
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorization(intent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutCsrfToken() throws Exception {
        // Given
        final Uri resultUri = Uri.parse(redirectUriBase + "?code=the_code");
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorization(intent);
    }

    @Test
    public void shouldCallGetTokens() throws Exception {
        // Given
        final AuthResource mockAuthResource = mock(AuthResource.class);
        coinbase = new Coinbase(RuntimeEnvironment.application) {
            @Override
            public AuthResource getAuthResource() {
                return mockAuthResource;
            }
        };
        coinbase.setClientIdAndSecret(clientId, clientSecret);

        final String authCode = "the_code";
        final Uri resultUri = Uri.parse(redirectUriBase + "?code=" + authCode + "&state=" + coinbase.getCsrfToken());
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorization(intent);

        // Then
        ArgumentCaptor<String> clientIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> clientSecretCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redirectUrlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockAuthResource).getTokens(clientIdCaptor.capture(),
                clientSecretCaptor.capture(),
                codeCaptor.capture(),
                redirectUrlCaptor.capture());

        assertThat(clientIdCaptor.getValue()).isEqualTo(clientId);
        assertThat(clientSecretCaptor.getValue()).isEqualTo(clientSecret);
        assertThat(codeCaptor.getValue()).isEqualTo(authCode);
        assertThat(redirectUrlCaptor.getValue()).isEqualTo(redirectUriBase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutDataRx() throws Exception {
        // Given
        final Intent intent = new Intent().setData(null);

        // When
        coinbase.completeAuthorizationRx(intent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutAuthCodeRx() throws Exception {
        // Given
        final Uri resultUri = Uri.parse(redirectUriBase + "?q=nothing");
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorizationRx(intent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowCompleteAuthorizationWithoutCsrfTokenRx() throws Exception {
        // Given
        final Uri resultUri = Uri.parse(redirectUriBase + "?code=the_code");
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorizationRx(intent);
    }

    @Test
    public void shouldCallGetTokensRx() throws Exception {
        // Given
        final AuthResource mockAuthResource = mock(AuthResource.class);
        coinbase = new Coinbase(RuntimeEnvironment.application) {
            @Override
            public AuthResource getAuthResource() {
                return mockAuthResource;
            }
        };
        coinbase.setClientIdAndSecret(clientId, clientSecret);

        final String authCode = "the_code";
        final Uri resultUri = Uri.parse(redirectUriBase + "?code=" + authCode + "&state=" + coinbase.getCsrfToken());
        final Intent intent = new Intent().setData(resultUri);

        // When
        coinbase.completeAuthorizationRx(intent);

        // Then
        ArgumentCaptor<String> clientIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> clientSecretCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redirectUrlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockAuthResource).getTokensRx(clientIdCaptor.capture(),
                clientSecretCaptor.capture(),
                codeCaptor.capture(),
                redirectUrlCaptor.capture());

        assertThat(clientIdCaptor.getValue()).isEqualTo(clientId);
        assertThat(clientSecretCaptor.getValue()).isEqualTo(clientSecret);
        assertThat(codeCaptor.getValue()).isEqualTo(authCode);
        assertThat(redirectUrlCaptor.getValue()).isEqualTo(redirectUriBase);
    }

    @Test
    public void shouldCleanTokensOnLogoutCall() {
        // Given
        coinbase.accessToken = "access_token";
        coinbase.refreshToken = "refresh_token";

        // When
        coinbase.logout();

        // Then
        assertThat(coinbase.accessToken).isNull();
        assertThat(coinbase.refreshToken).isNull();
    }

}