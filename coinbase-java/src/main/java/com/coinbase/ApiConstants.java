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

public interface ApiConstants {

    String PATH_SEPARATOR = "/";
    String BASE_URL_PRODUCTION = "https://api.coinbase.com";
    String SERVER_VERSION = "v2";
    String CLIENT_PLATFORM = "mobile";
    String VERSION = "2018-02-08";

    // Endpoints
    String AUTH_CODE = "oauth/authorize/with-credentials";
    String ACCESS_TOKEN = "oauth/token";
    String USER = "user";
    String USERS = "users";
    String USER_AUTH_INFO = USER + "/auth";
    String TRANSACTIONS = "transactions";
    String ACCOUNTS = "accounts";
    String COMPLETE = "complete";
    String RESEND = "resend";
    String PRICES = "prices";
    String SPOT = "spot";
    String ADDRESSES = "addresses";
    String PRIMARY = "primary";
    String DEPOSITS = "deposits";
    String WITHDRAWALS = "withdrawals";
    String PAYMENT_METHODS = "payment-methods";
    String EXCHANGE_RATES = "exchange-rates";
    String CURRENCIES = "currencies";
    String TIME = "time";

    // Params
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String TOKEN = "token";

    String AUTHORIZE = "authorize";
    String REFRESH_TOKEN = "refresh_token";
    String AUTHORIZATION_CODE = "authorization_code";
    String TWO_LEGGED = "2_legged";

    String GRANT_TYPE = "grant_type";
    String STARTING_AFTER = "starting_after";
    String LOCALE = "locale";
    String TZ_OFFSET = "tz_offset";
    String ABOVE = "above";
    String CURRENCY_CODE = "currency_code";
    String MESSAGE = "message";
    String PRICE = "price";
    String COMPLETED = "completed";
    String PENDING = "pending";
    String TYPE = "type";
    String SEND = "send";
    String REQUEST = "request";
    String TRANSFER = "transfer";
    String BUY = "buy";
    String SELL = "sell";
    String BUYS = "buys";
    String SELLS = "sells";
    String TO = "to";
    String FROM = "from";
    String AMOUNT = "amount";
    String CURRENCY = "currency";
    String COMMIT = "commit";
    String IDEM = "idem";
    String FEE = "fee";
    String PAYMENT_METHOD = "payment_method";
    String DESCRIPTION = "description";
    String INSTANT_EXCHANGE_QUOTE = "instant_exchange_quote";
    String NAME = "name";
    String TIME_ZONE = "time_zone";
    String NATIVE_CURRENCY = "native_currency";

    String TOTAL = "total";
    String QUOTE = "quote";
    String STATE = "state";

    interface Headers {
        String CB_VERSION = "CB-VERSION";
        String CB_CLIENT = "CB-CLIENT";
        String APP_VERSION = "X-App-Version";
        String APP_BUILD_NUMBER = "X-App-Build-Number";
        String AUTHORIZATION = "Authorization";
        String ACCEPT_LANGUAGE = "Accept-Language";
        String CB_2_FA_TOKEN = "CB-2FA-Token";
    }

    interface OAuth {
        String OAUTH = "oauth";
        String ROOT = PATH_SEPARATOR + OAUTH + PATH_SEPARATOR;

        String TOKEN = ROOT + ApiConstants.TOKEN;
        String REVOKE = ROOT + "revoke";
    }
}
