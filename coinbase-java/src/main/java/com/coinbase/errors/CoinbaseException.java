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

import java.io.IOException;
import java.util.List;

/**
 * Indicates a deviation from normal communication flow. Specifically case when API call went through
 * the network successfully, but the response code is different from 200.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#errors">
 * https://developers.coinbase.com/api/v2#errors</a>
 */
public class CoinbaseException extends IOException {

    private List<Error> errors;

    /**
     * Creates an instance of {@link CoinbaseException} with specified errors.
     *
     * @param errors errors returned by server.
     */
    public CoinbaseException(List<Error> errors) {
        this.errors = errors;
    }

    /**
     * Returns the first error returned by server or <code>null</code> if there are no errors.
     *
     * @return the first server error.
     */
    public Error getServerError() {
        return !errors.isEmpty() ? errors.get(0) : null;
    }

    /**
     * Returns all errors returned by server.
     *
     * @return all errors returned by server.
     */
    public List<Error> getServerErrors() {
        return errors;
    }
}
