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

/**
 * Country object model.
 */
public class Country {

    private String code;
    private String name;
    private Boolean isInEurope;

    /**
     * Country code abbreviation.
     *
     * @return country code abbreviation.
     */
    public String getCode() {
        return code;
    }

    /**
     * Country name.
     *
     * @return country name
     */
    public String getName() {
        return name;
    }

    /**
     * True if user is in Europe.
     *
     * @return true if user is in Europe.
     */
    public Boolean getInEurope() {
        return isInEurope;
    }
}
