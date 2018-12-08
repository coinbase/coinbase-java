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

public class Time {

    private String iso;
    private long epoch;

    /**
     * Returns ISO8601 formatted time.
     *
     * @return timestamp in ISO8601 format.
     */
    public String getIso() {
        return iso;
    }

    /**
     * Returns time in Unix Epoch format: number of seconds elapsed from UTC 00:00:00, January 1 1970
     *
     * @return time in Unix Epoch format.
     */
    public long getEpoch() {
        return epoch;
    }

}
