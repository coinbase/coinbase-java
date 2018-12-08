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

package com.coinbase.resources.auth;

import com.coinbase.ApiConstants;
import com.coinbase.network.ApiCall;

import io.reactivex.Single;

/**
 * Resource that provides methods for authorization.
 */
public class AuthResource {

    private final AuthApi authApi;
    private final AuthApiRx authApiRx;

    /**
     * Creates a new {@link AuthResource}
     *
     * @param authApi   api interface implementation
     * @param authApiRx rx api interface implementation
     */
    public AuthResource(AuthApi authApi, AuthApiRx authApiRx) {
        this.authApi = authApi;
        this.authApiRx = authApiRx;
    }

    /**
     * Exchanges the temporary code for valid {@link AccessToken#accessToken access token} and
     * {@link AccessToken#refreshToken refresh token}.
     * <p>
     * When you authenticate, your app will be given an {@link AccessToken#accessToken access token}
     * and a {@link AccessToken#refreshToken refresh token}.
     * The {@link AccessToken#accessToken access token} is used to authenticate all your requests,
     * but expires in two hours. If you try to make a call with an expired access token,
     * a {@link com.coinbase.errors.CoinbaseException CoinbaseException} will be thrown.
     * <p>
     * Once an {@link AccessToken#accessToken access token} has expired, you will need to use the
     * {@link AccessToken#refreshToken refresh token} to obtain a new
     * {@link AccessToken#accessToken access token} and a new {@link AccessToken#refreshToken refresh token}
     * via {@link #refreshTokens(String, String, String)} call.
     *
     * @param clientId     client ID, received after registering a coinbase application.
     * @param clientSecret client secret received after registering a coinbase application.
     * @param authCode     Value from auth redirect url.
     * @param redirectUri  application redirect URI.
     * @return request {@link ApiCall} for {@link AccessToken}.
     * @see AccessToken
     * @see ApiCall
     * @see #refreshTokens(String, String, String)
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/integrating">
     * https://developers.coinbase.com/docs/wallet/coinbase-connect/integrating</a>
     */
    public ApiCall<AccessToken> getTokens(String clientId,
                                          String clientSecret,
                                          String authCode,
                                          String redirectUri) {
        return authApi.getTokens(new GetTokensRequest(clientId,
                clientSecret,
                authCode,
                redirectUri != null ? redirectUri : ApiConstants.TWO_LEGGED));
    }

    /**
     * Refresh {@link AccessToken#accessToken access token} and {@link AccessToken#refreshToken refresh token}
     * <p>
     * The {@link AccessToken#refreshToken refresh token} never expires but it can only be
     * exchanged once for a new set of {@link AccessToken#accessToken access token} and
     * {@link AccessToken#refreshToken refresh token}.
     *
     * @param clientId     client ID, received after registering a coinbase application.
     * @param clientSecret client secret received after registering a coinbase application.
     * @param refreshToken the most recent {@link AccessToken#refreshToken refresh token} received
     *                     from Coinbase server.
     * @return request {@link ApiCall} to refresh tokens.
     * @see AccessToken
     * @see RefreshTokenRequest
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens">
     * https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    public ApiCall<AccessToken> refreshTokens(String clientId,
                                              String clientSecret,
                                              String refreshToken) {
        return authApi.refreshTokens(new RefreshTokenRequest(clientId, clientSecret, refreshToken));
    }

    /**
     * {@link AccessToken#accessToken Access token} can be revoked manually if you want to disconnect your application’s access
     * to the user’s account. Revoking can also be used to implement a log-out feature.
     * <p>
     * Once token is successfully revoked both {@link AccessToken#accessToken access token} and
     * {@link AccessToken#refreshToken refresh token} become invalid. To get new tokens user should
     * pass through OAuth flow.
     *
     * @param accessToken access token to revoke.
     * @return request {@link ApiCall} to revoke token.
     * @see RevokeTokenRequest
     * @see RevokeTokenResponse
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens">
     * https://developers.coinbase.com/docs/wallet/coinbase-connect/access-and-refresh-tokens</a>
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public ApiCall<RevokeTokenResponse> revokeToken(String accessToken) {
        if (accessToken == null) {
            throw new IllegalStateException("Client is not logged in!");
        }
        return authApi.revokeToken(new RevokeTokenRequest(accessToken));
    }

    /**
     * Rx version of {@link #getTokens(String, String, String, String) getTokens()}.
     *
     * @return call {@link Single} source with {@link AccessToken}.
     * @see #getTokens(String, String, String, String)
     */
    public Single<AccessToken> getTokensRx(String clientId,
                                           String clientSecret,
                                           String authCode,
                                           String redirectUri) {
        return authApiRx.getTokens(new GetTokensRequest(clientId,
                clientSecret,
                authCode,
                redirectUri != null ? redirectUri : ApiConstants.TWO_LEGGED));
    }

    /**
     * Rx version of {@link #refreshTokens(String, String, String) refreshTokens()}.
     *
     * @return call {@link Single} source to refresh tokens.
     * @see #refreshTokens(String, String, String)
     */
    public Single<AccessToken> refreshTokensRx(String clientId,
                                               String clientSecret,
                                               String refreshToken) {
        return authApiRx.refreshTokens(new RefreshTokenRequest(clientId, clientSecret, refreshToken));
    }

    /**
     * Rx version of {@link #revokeToken(String)} revokeToken()}.
     *
     * @return call {@link Single} source to revoke token.
     * @see #revokeToken(String)
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public Single<RevokeTokenResponse> revokeTokenRx(String accessToken) {
        if (accessToken == null) {
            throw new IllegalStateException("Client is not logged in!");
        }
        return authApiRx.revokeToken(new RevokeTokenRequest(accessToken));
    }
}
