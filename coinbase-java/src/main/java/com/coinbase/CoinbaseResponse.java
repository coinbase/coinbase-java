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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.errors.Error;

import java.util.ArrayList;
import java.util.List;

/**
 * The response object returned by Coinbaser server on every successful (status code is 200) request.
 * It contains request {@link #data} and optional {@link #warnings}.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#warnings">
 * https://developers.coinbase.com/api/v2#warnings</a>
 */
public class CoinbaseResponse<T> {

    private T data;
    private List<Error> warnings = new ArrayList<>();

    /**
     * Gets response data of type {@link T}.
     *
     * @return data of type {@link T}.
     */
    @NonNull
    public T getData() {
        return data;
    }

    /**
     * Gets optional warnings that notify the developer of best practices, implementation suggestions
     * or deprecation warnings. While you don’t need to show warnings to the user, they are usually
     * something you need to act on.
     *
     * @return optional warnings.
     * @see <a href="https://developers.coinbase.com/api/v2#warnings">
     * https://developers.coinbase.com/api/v2#warnings</a>
     */
    @Nullable
    public List<Error> getWarnings() {
        return warnings;
    }

}
