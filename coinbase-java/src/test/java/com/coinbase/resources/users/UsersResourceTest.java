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

import com.coinbase.ApiConstants;
import com.coinbase.Coinbase;
import com.coinbase.util.Resource;
import com.coinbase.util.ResourceMethodTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import okhttp3.mockwebserver.RecordedRequest;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for User resource methods.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UsersResourceTest.GetUserTest.class,
        UsersResourceTest.GetCurrentUserTest.class,
        UsersResourceTest.GetAuthInfoTest.class,
        UsersResourceTest.UpdateUserTest.class
})
public interface UsersResourceTest {

    String USER_ID = "user_id";
    UpdateUserRequest UPDATE_USER_REQUEST = UpdateUserRequest.builder()
            .setName("name")
            .setTimeZone("utc")
            .setNativeCurrency("usd")
            .build();

    class GetCurrentUserTest extends ResourceMethodTest<UserResource, AuthUser> {

        public GetCurrentUserTest() {
            super(
                    "auth/auth_user.json",
                    Coinbase::getUserResource,
                    UserResource::getCurrentUser,
                    UserResource::getCurrentUserRx
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).endsWith(ApiConstants.USER);
        }

        @Override
        protected void responseCheck(AuthUser data) {
            assertThat(data).isNotNull();

            // Check country.
            assertThat(data.getCountry()).isNotNull();
            assertThat(data.getCountry().getInEurope()).isNotNull();
        }
    }

    class GetUserTest extends ResourceMethodTest<UserResource, User> {

        public GetUserTest() {
            super(
                    "users/user_by_id.json",
                    Coinbase::getUserResource,
                    userResource -> userResource.getUser(USER_ID),
                    userResource -> userResource.getUserRx(USER_ID)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).endsWith(ApiConstants.USERS + "/" + USER_ID);
        }

        @Override
        protected void responseCheck(User data) {
            assertThat(data).isNotNull();
        }
    }

    class GetAuthInfoTest extends ResourceMethodTest<UserResource, AuthInfo> {

        public GetAuthInfoTest() {
            super(
                    "auth/auth_info.json",
                    Coinbase::getUserResource,
                    UserResource::getAuthInfo,
                    UserResource::getAuthInfoRx
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).endsWith(ApiConstants.USER_AUTH_INFO);
        }

        @Override
        protected void responseCheck(AuthInfo data) {
            assertThat(data).isNotNull();
        }
    }

    class UpdateUserTest extends ResourceMethodTest<UserResource, AuthUser> {

        public UpdateUserTest() {
            super(
                    "auth/auth_user.json",
                    Coinbase::getUserResource,
                    userResource -> userResource.updateUser(UPDATE_USER_REQUEST),
                    userResource -> userResource.updateUserRx(UPDATE_USER_REQUEST)
            );
        }

        @Override
        protected void requestCheck(RecordedRequest request) {
            assertThat(request.getPath()).endsWith(ApiConstants.USER);
            UpdateUserRequest recordedRequest = Resource.parseJsonAsObject(request.getBody(), UpdateUserRequest.class);
            assertEquals(UPDATE_USER_REQUEST.name, recordedRequest.name);
            assertEquals(UPDATE_USER_REQUEST.timeZone, recordedRequest.timeZone);
            assertEquals(UPDATE_USER_REQUEST.nativeCurrency, recordedRequest.nativeCurrency);
        }

        @Override
        protected void responseCheck(AuthUser data) {
            assertThat(data).isNotNull();
        }
    }

}
