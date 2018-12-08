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

import androidx.annotation.Nullable;

/**
 * An error that may be returned by server. The {@link #message} field
 * will be localized, according to language headers.
 */
public class Error {

    private String id;
    private String message;
    private String url;

    /**
     * Returns error code.
     *
     * @return error code.
     * @see ErrorCodes
     */
    public String getId() {
        return id;
    }

    /**
     * Returns human readable localized message.
     *
     * @return human readable localized message.
     */
    @Nullable
    public String getMessage() {
        return message;
    }

    /**
     * Returns optional link to the documentation.
     *
     * @return link to the documentation.
     */
    @Nullable
    public String getUrl() {
        return url;
    }
}
