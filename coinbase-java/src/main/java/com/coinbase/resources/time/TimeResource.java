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

package com.coinbase.resources.time;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;

import io.reactivex.rxjava3.core.Single;

/**
 * Represents resource for getting current time from Coinbase.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#time">online docs: Time</a> for more info.
 */
public class TimeResource {

    private final TimeApi timeApi;
    private final TimeApiRx timeApiRx;

    public TimeResource(TimeApi timeApi, TimeApiRx timeApiRx) {
        this.timeApi = timeApi;
        this.timeApiRx = timeApiRx;
    }

    /**
     * Get the API server time.
     * <p/>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @return {@link ApiCall} for current server time.
     */
    public ApiCall<CoinbaseResponse<Time>> getCurrentTime() {
        return timeApi.getCurrentTime();
    }

    /**
     * Rx version of {@link #getCurrentTime() getCurrentTime()}.
     *
     * @return {@link Single} for current server time.
     */
    public Single<CoinbaseResponse<Time>> getCurrentTimeRx() {
        return timeApiRx.getCurrentTime();
    }
}
