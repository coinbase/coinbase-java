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

import android.net.Uri;

import com.coinbase.AuthorizationRequest.AccountSetting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for {@link AuthorizationRequest} class.
 */
@Config(sdk = 26)
@RunWith(RobolectricTestRunner.class)
public class AuthorizationRequestTest {

    private Uri authorizationUri;

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullScope() throws Exception {
        new AuthorizationRequest(
                Uri.parse("h://1")
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAllowNullRedirectUri() throws Exception {
        new AuthorizationRequest(
                null,
                "scope"
        );
    }

    @Test
    public void shouldBuildCorrectUri() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "user:read";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";
        final URL url = new URL(baseOauthUrl);
        final Uri wantedUri = Uri.parse(baseOauthUrl)
                .buildUpon()
                .path(ApiConstants.AUTHORIZE)
                // Add from request
                .appendQueryParameter(AuthorizationRequest.ARG_RESPONSE_TYPE, "code")
                .appendQueryParameter(AuthorizationRequest.ARG_CLIENT_ID, clientId)
                .appendQueryParameter(AuthorizationRequest.ARG_REDIRECT_URI, redirectUri.toString())
                .appendQueryParameter(AuthorizationRequest.ARG_SCOPE, scope)
                .build();

        // When
        final Uri authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .getAuthorizationUri(url, clientId);

        // Then
        assertThat(authorizationUri).isEqualTo(wantedUri);
    }

    @Test
    public void shouldSetAccount() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "user:read";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";

        final AccountSetting account = AuthorizationRequest.AccountSetting.ALL;

        // When
        final Uri authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .setAccount(account)
                .getAuthorizationUri(new URL(baseOauthUrl), clientId);

        // Then
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.ARG_ACCOUNT))
                .isEqualTo(account.value);
    }

    @Test
    public void shouldAddLayoutParameter() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "user:read";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";

        // When
        final Uri authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .setShowSignUpPage(true)
                .getAuthorizationUri(new URL(baseOauthUrl), clientId);

        // Then
        assertThat(authorizationUri
                .getQueryParameter(AuthorizationRequest.ARG_LAYOUT))
                .isEqualTo(AuthorizationRequest.LAYOUT_SIGNUP);
    }

    @Test
    public void shouldAddReferralId() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "user:read";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";

        final String referralId = "referral";

        // When
        final Uri authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .setReferralId(referralId)
                .getAuthorizationUri(new URL(baseOauthUrl), clientId);

        // Then
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.ARG_REFERRAL))
                .isEqualTo(referralId);
    }

    @Test
    public void shouldSetAccountCurrency() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "user:read";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";

        final AccountSetting account = AuthorizationRequest.AccountSetting.SELECT;
        final String currency = "BTC";

        // When
        final Uri authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .setAccount(account)
                .setAccountCurrency(currency)
                .getAuthorizationUri(new URL(baseOauthUrl), clientId);

        // Then
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.ARG_ACCOUNT_CURRENCY)).isEqualTo(currency);
    }

    @Test
    public void shouldAddMeta() throws Exception {
        // Given
        final String clientId = "clientId";
        final Uri redirectUri = Uri.parse("coinbase://some-app");
        final String scope = "wallet:transactions:send";
        final String baseOauthUrl = "https://www.coinbase.com/authorize";

        // When
        final String name = "Name";
        final String limitAmount = "100";
        final String limitCurrency = "100";
        final AuthorizationRequest.LimitPeriod limitPeriod = AuthorizationRequest.LimitPeriod.YEAR;

        authorizationUri = new AuthorizationRequest(redirectUri, scope)
                .setMetaName(name)
                .setMetaSendLimitAmount(limitAmount)
                .setMetaSendLimitCurrency(limitCurrency)
                .setMetaSendLimitPeriod(limitPeriod)
                .getAuthorizationUri(new URL(baseOauthUrl), clientId);

        // Then
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.META_NAME)).isEqualTo(name);
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.META_SEND_LIMIT_AMOUNT)).isEqualTo(limitAmount);
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.META_SEND_LIMIT_CURRENCY)).isEqualTo(limitCurrency);
        assertThat(authorizationUri.getQueryParameter(AuthorizationRequest.META_SEND_LIMIT_PERIOD)).isEqualTo(limitPeriod.value);
    }

}