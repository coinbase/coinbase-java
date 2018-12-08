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

package com.coinbase.errors;

import com.coinbase.resources.auth.AuthResource;

import java.io.IOException;

/**
 * Indicates a deviation from normal communication flow for authorization requests like
 * {@link AuthResource#getTokens(String, String, String, String) getTokens()},
 * {@link AuthResource#refreshTokens(String, String, String) refreshTokens(String, String, String)}
 * or {@link AuthResource#revokeToken(String) revokeToken(String)}.  Exception is thrown
 * when API call went through the network successfully, but the response code is different from 200.
 *
 * @see <a href="https://developers.coinbase.com/docs/wallet/error-codes">
 * https://developers.coinbase.com/docs/wallet/error-codes</a>
 */
public class CoinbaseOAuthException extends IOException {

    private final OAuthError oAuthError;

    /**
     * Creates an instance of {@link CoinbaseOAuthException} with specific {@link OAuthError}.
     *
     * @param oAuthError errors returned by server.
     */
    public CoinbaseOAuthException(OAuthError oAuthError) {
        this.oAuthError = oAuthError;
    }

    /**
     * Gets {@link OAuthError}.
     *
     * @return {@link OAuthError}.
     */
    public OAuthError getoAuthError() {
        return oAuthError;
    }
}
