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

import androidx.annotation.Nullable;

/**
 * Request model for updating user profile data.
 * <b>All {@code null} parameters will not be sent to server</b>.
 * User {@link RequestBuilder builder} to set corresponding values.
 */
public final class UpdateUserRequest {

    public final String name;
    public final String timeZone;
    public final String nativeCurrency;

    private UpdateUserRequest(String name, String timeZone, String nativeCurrency) {
        this.name = name;
        this.timeZone = timeZone;
        this.nativeCurrency = nativeCurrency;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    /**
     * Builder for updating user data request.
     * Note that parameters set to {@code null} will not be sent
     * to the server.
     */
    public static class RequestBuilder {

        private String name;
        private String timeZone;
        private String nativeCurrency;

        /**
         * Sets user's public name.
         *
         * @param name (optional) user's public name.
         * @return this builder instance.
         */
        public RequestBuilder setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets user's timezone.
         *
         * @param timeZone (optional) timezone.
         * @return this builder instance.
         */
        public RequestBuilder setTimeZone(@Nullable String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * Sets local currency used to display amounts converted from BTC.
         *
         * @param nativeCurrency (optional) local currency used to display amounts converted from BTC.
         * @return this builder instance.
         */
        public RequestBuilder setNativeCurrency(@Nullable String nativeCurrency) {
            this.nativeCurrency = nativeCurrency;
            return this;
        }

        /**
         * Builds the update request.
         *
         * @return update user {@link UpdateUserRequest request}.
         */
        public UpdateUserRequest build() {
            return new UpdateUserRequest(name, timeZone, nativeCurrency);
        }
    }
}
