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

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Encapsulates arguments to make a request for authorization.
 *
 * @see Coinbase#beginAuthorization(android.content.Context, AuthorizationRequest)
 */
public final class AuthorizationRequest {

    static final String ARG_RESPONSE_TYPE = "response_type";
    static final String ARG_CLIENT_ID = "client_id";
    static final String ARG_REDIRECT_URI = "redirect_uri";

    static final String ARG_LAYOUT = "layout";
    static final String ARG_REFERRAL = "referral";
    static final String ARG_ACCOUNT = "account";
    static final String ARG_SCOPE = "scope";
    static final String ARG_ACCOUNT_CURRENCY = "account_currency";

    // Meta arguments.
    static final String META_NAME = "meta[name]";
    static final String META_SEND_LIMIT_AMOUNT = "meta[send_limit_amount]";
    static final String META_SEND_LIMIT_CURRENCY = "meta[send_limit_currency]";
    static final String META_SEND_LIMIT_PERIOD = "meta[send_limit_period]";

    static final String LAYOUT_SIGNUP = "signup";

    private final String scope;
    private final Uri redirectUri;

    private AccountSetting account;
    private String accountCurrency;

    private boolean showSignUp = false;
    private String referralId;

    // Meta info.
    private String metaName;
    private String sendLimitAmount;
    private String sendLimitCurrency;
    private LimitPeriod sendLimitPeriod;

    /**
     * Creates a new object storing authorization parameters.
     *
     * @param redirectUri redirect url
     * @param scopes      constants from {@link Scope}. Can not be empty
     * @see Scope
     */
    public AuthorizationRequest(@NonNull Uri redirectUri,
                                @NonNull String... scopes) {
        if (throwIfNull(scopes, "scopes array").length == 0) {
            throw new IllegalArgumentException("Scopes can not be empty");
        }
        this.redirectUri = throwIfNull(redirectUri, "redirectUri");
        this.scope = throwIfNull(TextUtils.join(",", scopes), "scopes");
    }

    /**
     * Change the account access the application will receive.
     * Default value is {@link AccountSetting#SELECT SELECT}.
     *
     * @param account account setting.
     * @return this authorization request object.
     */
    public AuthorizationRequest setAccount(AccountSetting account) {
        this.account = account;
        return this;
    }

    /**
     * For logged out users, login view is shown by default. You can show the sign up page instead.
     *
     * @param showSignUp set to {@code true} if you want to present sign up page.
     * @return this authorization request object.
     */
    public AuthorizationRequest setShowSignUpPage(boolean showSignUp) {
        this.showSignUp = showSignUp;
        return this;
    }

    /**
     * When account setting is {@link AccountSetting#SELECT select}, specify the currency to
     * filter user accounts.
     *
     * @param currency currency code, e.g. "BTC"
     * @return this authorization request object.
     * @see #setAccount(AccountSetting)
     */
    public AuthorizationRequest setAccountCurrency(String currency) {
        this.accountCurrency = currency;
        return this;
    }

    /**
     * Earn a referral bonus from new users who sign up via OAuth.
     * Value needs to be set to developer’s referral ID (username).
     *
     * @param referralId referral ID
     * @return this authorization request object.
     */
    public AuthorizationRequest setReferralId(String referralId) {
        this.referralId = referralId;
        return this;
    }

    /**
     * Name for this session (not a name for your application.) This will appear in the user’s account
     * settings underneath your application’s name. Use it to provide identifying information
     * if your app is often authorized multiple times.
     *
     * @param name name of the session.
     * @return this authorization request object.
     */
    public AuthorizationRequest setMetaName(String name) {
        this.metaName = name;
        return this;
    }

    /**
     * Limit for the amount of money your application can send from the user’s account.
     * This will be displayed on the authorize screen
     *
     * @param limitAmount limit amount
     * @return this authorization request object.
     * @see #setMetaSendLimitPeriod(LimitPeriod)
     * @see #setMetaSendLimitCurrency(String)
     */
    public AuthorizationRequest setMetaSendLimitAmount(String limitAmount) {
        this.sendLimitAmount = limitAmount;
        return this;
    }

    /**
     * Currency of {@link #setMetaSendLimitAmount(String) send money limit} in ISO format, ex. BTC, USD.
     *
     * @param limitCurrency limit currency, e.g. "BTC", "USD"
     * @return this authorization request object.
     * @see #setMetaSendLimitPeriod(LimitPeriod)
     */
    public AuthorizationRequest setMetaSendLimitCurrency(String limitCurrency) {
        this.sendLimitCurrency = limitCurrency;
        return this;
    }

    /**
     * How often the {@link #setMetaSendLimitAmount(String) send money limit} expires. Default is {@link LimitPeriod#MONTH month} -
     * allowed values are {@link LimitPeriod#DAY day}, {@link LimitPeriod#MONTH month} and {@link LimitPeriod#YEAR year}.
     *
     * @param limitPeriod how often send money limit expires
     * @return this authorization request object.
     */
    public AuthorizationRequest setMetaSendLimitPeriod(LimitPeriod limitPeriod) {
        this.sendLimitPeriod = limitPeriod;
        return this;
    }

    Uri getAuthorizationUri(URL baseOAuthUrl, String clientId) {
        try {
            final Uri.Builder builder = Uri.parse(new URL(baseOAuthUrl, ApiConstants.AUTHORIZE).toURI().toString())
                    .buildUpon()
                    .appendQueryParameter(ARG_RESPONSE_TYPE, "code")
                    .appendQueryParameter(ARG_CLIENT_ID, clientId)
                    .appendQueryParameter(ARG_REDIRECT_URI, redirectUri.toString())
                    .appendQueryParameter(ARG_SCOPE, scope);

            if (showSignUp) {
                builder.appendQueryParameter(ARG_LAYOUT, LAYOUT_SIGNUP);
            }

            appendIfNotNull(builder, ARG_ACCOUNT, account);
            appendIfNotNull(builder, ARG_ACCOUNT_CURRENCY, accountCurrency);
            appendIfNotNull(builder, ARG_REFERRAL, referralId);

            // Append meta data.
            appendIfNotNull(builder, META_NAME, metaName);
            appendIfNotNull(builder, META_SEND_LIMIT_AMOUNT, sendLimitAmount);
            appendIfNotNull(builder, META_SEND_LIMIT_CURRENCY, sendLimitCurrency);
            appendIfNotNull(builder, META_SEND_LIMIT_PERIOD, sendLimitPeriod);

            return builder.build();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <T> void appendIfNotNull(Uri.Builder builder, String queryParam, T value) {
        if (value != null) {
            builder.appendQueryParameter(queryParam, value.toString());
        }
    }

    private <T> T throwIfNull(T t, String name) {
        if (t == null) {
            throw new NullPointerException(name + " == null");
        }
        return t;
    }

    /**
     * Access to user accounts setting.
     */
    public enum AccountSetting {
        /**
         * <i>(default)</i> Allow user to pick the wallet associated with the application.
         */
        SELECT("select"),

        /**
         * Application will get access to all of user’s wallets.
         */
        ALL("all");

        String value;

        AccountSetting(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum LimitPeriod {
        DAY("day"),
        MONTH("month"),
        YEAR("year");

        String value;

        LimitPeriod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
