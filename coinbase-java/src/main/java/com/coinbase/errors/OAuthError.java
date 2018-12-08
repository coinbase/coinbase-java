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

/**
 * An error returned by server as response on authorization requests like
 * {@link AuthResource#getTokens(String, String, String, String) getTokens()},
 * {@link AuthResource#refreshTokens(String, String, String) refreshTokens(String, String, String)}
 * or {@link AuthResource#revokeToken(String) revokeToken(String)}.
 */
public class OAuthError {

    private String error;
    private String errorDescription;

    /**
     * Returns error code.
     *
     * @return error code.
     * @see ErrorCodes
     */
    public String getError() {
        return error;
    }

    /**
     * Returns human readable localized message.
     *
     * @return human readable localized message.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

}
