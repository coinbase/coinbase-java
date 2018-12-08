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

/**
 * Access token response data.
 */
public class AccessToken {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String scope;

    /**
     * Gets an access token.
     *
     * @return the access token.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets an access token type.
     *
     * @return the access tokenType.
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Gets an access token expiration time in seconds.
     *
     * @return the expiration time.
     */
    public Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * Gets a refresh token.
     *
     * @return the refreshToken.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Gets the coma separated access token {@link com.coinbase.Scope scope constants}.
     *
     * @return the access token scopes.
     * @see com.coinbase.Scope Scope
     */
    public String getScope() {
        return scope;
    }

}