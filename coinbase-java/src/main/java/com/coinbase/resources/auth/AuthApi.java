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

package com.coinbase.resources.auth;

import com.coinbase.ApiConstants;
import com.coinbase.network.ApiCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Authentication API interface for {@link AuthResource}.
 */
public interface AuthApi {

    @POST(ApiConstants.OAuth.TOKEN)
    ApiCall<AccessToken> getTokens(@Body GetTokensRequest getTokensRequest);

    @POST(ApiConstants.OAuth.TOKEN)
    ApiCall<AccessToken> refreshTokens(@Body RefreshTokenRequest refreshTokenRequest);

    @POST(ApiConstants.OAuth.REVOKE)
    ApiCall<RevokeTokenResponse> revokeToken(@Body RevokeTokenRequest revokeTokenRequest);
}
