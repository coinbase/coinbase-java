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

package com.coinbase.resources.users;

import java.util.List;
import java.util.Map;

/**
 * Authentication information for the current session.
 */
public class AuthInfo {

    private String method;
    private List<String> scopes;
    private Map<String, String> oauthMeta;

    /**
     * Authentication method. It will be {@code oauth} in most cases.
     *
     * @return authentication method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Authentication scopes for the current session.
     *
     * @return Authentication scopes for the current session.
     */
    public List<String> getScopes() {
        return scopes;
    }

    /**
     * OAuth meta data.
     *
     * @return OAuth meta data.
     * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/reference">onlide docs</a>.
     */
    public Map<String, String> getOauthMeta() {
        return oauthMeta;
    }
}
