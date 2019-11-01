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

import androidx.annotation.NonNull;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;

import io.reactivex.rxjava3.core.Single;

/**
 * Represents resource for getting user information.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#users">online docs: Users</a> for more info.
 */
public class UserResource {

    private UsersApi usersApi;
    private UsersApiRx usersApiRx;

    public UserResource(UsersApi usersApi, UsersApiRx usersApiRx) {
        this.usersApi = usersApi;
        this.usersApiRx = usersApiRx;
    }

    //region ApiCall methods.

    /**
     * Get any user’s public information with their ID.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li><i>No permission required</i></li>
     * </ul>
     *
     * @param userId ID of the user.
     * @return {@link ApiCall call} for getting user info.
     * @see <a href="https://developers.coinbase.com/api/v2#show-a-user">online docs: Show a user</a>.
     */
    public ApiCall<CoinbaseResponse<User>> getUser(@NonNull String userId) {
        return usersApi.getUser(userId);
    }

    /**
     * <p>
     * Get current user’s public information. To get user’s email or private information,
     * use permissions {@code wallet:user:email} and {@code wallet:user:read}.
     * </p>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li><i>No scope required for public data</i></li>
     * <li>{@link com.coinbase.Scope.Wallet.User#EMAIL wallet:user:email}</li>
     * <li>{@link com.coinbase.Scope.Wallet.User#READ wallet:user:read}</li>
     * </ul>
     *
     * @return {@link ApiCall call} for getting authorized user info.
     * @see <a href="https://developers.coinbase.com/api/v2#show-current-user">online docs: Show current user</a>.
     */
    public ApiCall<CoinbaseResponse<AuthUser>> getCurrentUser() {
        return usersApi.getCurrentUser();
    }

    /**
     * Get current user’s authorization information including granted scopes
     * and send limits when using OAuth2 authentication.
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li><i>No permission required</i></li>
     * </ul>
     *
     * @return {@link ApiCall call} for getting authorization info.
     * @see <a href="https://developers.coinbase.com/api/v2#show-authorization-information">online docs: Show authorization information</a>.
     */
    public ApiCall<CoinbaseResponse<AuthInfo>> getAuthInfo() {
        return usersApi.getAuthInfo();
    }

    /**
     * Modify current user and their preferences.
     * <p/>
     * SCOPES:<br/>
     * <ul>
     * <li>{@link com.coinbase.Scope.Wallet.User#UPDATE wallet:user:update}</li>
     * </ul>
     *
     * @param request (required) user update request.
     * @return {@link ApiCall call} for updating current user information.
     * @see UpdateUserRequest
     * @see UpdateUserRequest.RequestBuilder
     * @see <a href="https://developers.coinbase.com/api/v2#update-current-user">online docs: Update current user</a>.
     */
    public ApiCall<CoinbaseResponse<AuthUser>> updateUser(@NonNull UpdateUserRequest request) {
        return usersApi.updateUser(request);
    }

    //endregion

    //region Rx Methods

    /**
     * Rx version of {@link #getUser(String) getUser(id)}.
     *
     * @param userId ID of the user.
     * @return {@link Single rxSingle} for getting authorized user info.
     */
    public Single<CoinbaseResponse<User>> getUserRx(@NonNull String userId) {
        return usersApiRx.getUser(userId);
    }

    /**
     * Rx version of {@link #getCurrentUser() getCurrentUser()}.
     *
     * @return {@link Single rxSingle} for getting user info.
     */
    public Single<CoinbaseResponse<AuthUser>> getCurrentUserRx() {
        return usersApiRx.getCurrentUser();
    }

    /**
     * Rx version of {@link #getAuthInfo() getAuthInfo()}.
     *
     * @return {@link Single rxSingle} for updating current user information.
     */
    public Single<CoinbaseResponse<AuthInfo>> getAuthInfoRx() {
        return usersApiRx.getAuthInfo();
    }

    /**
     * Rx version of {@link #updateUser(UpdateUserRequest) updateUser(request)}.
     *
     * @param request (required) user update request.
     * @return {@link Single rxSingle} for getting authorization info.
     */
    public Single<CoinbaseResponse<AuthUser>> updateUserRx(@NonNull UpdateUserRequest request) {
        return usersApiRx.updateUser(request);
    }

    //endregion
}
