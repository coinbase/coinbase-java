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

import com.coinbase.resources.base.DynamicResource;

/**
 * Generic user information. By default, only public information is shared without any scopes.
 * More detailed information or email can be requested with additional scopes.
 * @see AuthUser
 */
public class User extends DynamicResource {

    private String name;
    private String username;
    private String profileLocation;
    private String profileBio;
    private String profileUrl;
    private String avatarUrl;

    /**
     * User’s public name.
     *
     * @return user’s public name.
     */
    public String getName() {
        return name;
    }

    /**
     * User's name on Coinbase.
     *
     * @return user's name on Coinbase.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Location for user’s public profile.
     *
     * @return location for user’s public profile.
     */
    public String getProfileLocation() {
        return profileLocation;
    }

    /**
     * Bio for user’s public profile.
     *
     * @return bio for user’s public profile.
     */
    public String getProfileBio() {
        return profileBio;
    }

    /**
     * Public profile location if user has one.
     *
     * @return public profile location if user has one.
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * User’s avatar url.
     *
     * @return user’s avatar url.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

}