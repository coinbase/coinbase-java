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

/**
 * Constants for error codes used as values of {@link Error#id} and {@link OAuthError#error}.
 */
public interface ErrorCodes {

    /**
     * 402 When sending money over 2fa limit.
     */
    String TWO_FACTOR_REQUIRED = "two_factor_required";

    /**
     * 400 Missing parameter.
     */
    String PARAM_REQUIRED = "param_required";

    /**
     * 400 Unable to validate POST/PUT.
     */
    String VALIDATION_ERROR = "validation_error";

    /**
     * 400 Invalid request.
     */
    String INVALID_REQUEST = "invalid_request";

    /**
     * 400 User’s personal detail required to complete this request.
     */
    String PERSONAL_DETAILS_REQUIRED = "personal_details_required";

    /**
     * 400 User has not verified their email.
     */
    String UNVERIFIED_EMAIL = "unverified_email";

    /**
     * 401 Invalid auth (generic).
     */
    String AUTHENTICATION_ERROR = "authentication_error";

    /**
     * 401 Invalid Oauth token.
     */
    String INVALID_TOKEN = "invalid_token";

    /**
     * 401 Revoked Oauth token.
     */
    String REVOKED_TOKEN = "revoked_token";

    /**
     * 401 Expired Oauth token.
     */
    String EXPIRED_TOKEN = "expired_token";

    /**
     * 403 User hasn’t authenticated necessary scope.
     */
    String INVALID_SCOPE = "invalid_scope";

    /**
     * 404 Resource not found.
     */
    String NOT_FOUND = "not_found";

    /**
     * 429 Rate limit exceeded.
     */
    String RATE_LIMIT_EXCEEDED = "rate_limit_exceeded";

    /**
     * 500 Internal server error.
     */
    String INTERNAL_SERVER_ERROR = "internal_server_error";
}
