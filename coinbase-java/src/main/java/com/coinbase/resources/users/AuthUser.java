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

import java.util.Date;

public class AuthUser extends User {

    private String email;
    private String timeZone;
    private String nativeCurrency;
    private String bitcoinUnit;
    private Country country;
    private Date createdAt;

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * User's current timezone.
     *
     * @return user's timezone.
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * User's native currency abbreviation.
     *
     * @return user’s native currency
     */
    public String getNativeCurrency() {
        return nativeCurrency;
    }

    /**
     * Bitcoin unit.
     *
     * @return bitcoin unit.
     */
    public String getBitcoinUnit() {
        return bitcoinUnit;
    }

    /**
     * User's country.
     *
     * @return user's country.
     */
    public Country getCountry() {
        return country;
    }

    /**
     * When user's account was created.
     *
     * @return when user's account was created.
     */
    public Date getCreatedAt() {
        return createdAt;
    }
}
