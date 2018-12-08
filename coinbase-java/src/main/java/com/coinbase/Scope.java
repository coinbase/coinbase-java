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

/**
 * Scope constants.
 */
public interface Scope {

    @SuppressWarnings({"unused"})
    interface Wallet {

        interface Accounts {
            /**
             * List user’s accounts and their balances.
             */
            String READ = "wallet:accounts:read";
            /**
             * Update account (e.g. change name).
             */
            String UPDATE = "wallet:accounts:update";
            /**
             * Create a new account (e.g. BTC wallet).
             */
            String CREATE = "wallet:accounts:create";
            /**
             * Delete existing account.
             */
            String DELETE = "wallet:accounts:delete";
        }

        interface Addresses {
            /**
             * List account’s bitcoin or ethereum addresses.
             */
            String READ = "wallet:addresses:read";
            /**
             * Create new bitcoin or ethereum addresses for wallets.
             */
            String CREATE = "wallet:addresses:create";
        }

        interface Buys {
            /**
             * List account’s buys.
             */
            String READ = "wallet:buys:read";
            /**
             * Buy bitcoin or ethereum.
             */
            String CREATE = "wallet:buys:create";
        }

        interface Checkouts {
            /**
             * List user’s merchant checkouts.
             */
            String READ = "wallet:checkouts:read";
            /**
             * Create a new merchant checkout.
             */
            String CREATE = "wallet:checkouts:create";
        }

        interface Deposits {
            /**
             * List account’s deposits.
             */
            String READ = "wallet:deposits:read";
            /**
             * Create a new deposit.
             */
            String CREATE = "wallet:deposits:create";
        }

        interface Orders {
            /**
             * List user’s merchant order.
             */
            String READ = "wallet:orders:read";
            /**
             * Create a new merchant order.
             */
            String CREATE = "wallet:orders:create";
            /**
             * Refund a merchant order.
             */
            String REFUND = "wallet:orders:refund";
        }

        interface PaymentMethods {
            /**
             * List user’s payment methods (e.g. bank accounts).
             */
            String READ = "wallet:payment-methods:read";
            /**
             * Remove existing payment methods.
             */
            String DELETE = "wallet:payment-methods:delete";
            /**
             * Get detailed limits for payment methods (useful for performing buys and sells).
             * This permission is to be used together with wallet:payment-methods:read.
             */
            String LIMITS = "wallet:payment-methods:limits";
        }

        interface Sells {
            /**
             * List account’s sells.
             */
            String READ = "wallet:sells:read";
            /**
             * Sell bitcoin or ethereum.
             */
            String CREATE = "wallet:sells:create";
        }

        interface Transactions {

            /**
             * List account’s transactions.
             */
            String READ = "wallet:transactions:read";

            /**
             * Send bitcoin or ethereum.
             */
            String SEND = "wallet:transactions:send";

            /**
             * Special permission to be able to send money without requiring 2FA from user.
             * Because of the security sensitivity of this option, this scope is available by whitelist only.
             * You may request that your app be added to the whitelist via your application settings.
             * When using this scope you must make it abundantly clear to the user
             * that the application is moving funds in the background, without user interaction or per-transaction approval.
             * It is strongly advised to use this option only with low specified send limits.
             *
             * @see com.coinbase.resources.transactions.TransactionsResource#sendMoney(String, String,
             * com.coinbase.resources.transactions.SendMoneyRequest, com.coinbase.resources.transactions.Transaction.ExpandField...)
             * @see com.coinbase.resources.transactions.SendMoneyRequest
             * @see <a href="https://developers.coinbase.com/docs/wallet/coinbase-connect/two-factor-authentication">online docs: Two Factor Authentication</a>.
             */
            String SEND_BYPASS_2FA = "wallet:transactions:send:bypass-2fa";

            /**
             * Request bitcoin or ethereum from a Coinbase user.
             */
            String REQUEST = "wallet:transactions:request";
            /**
             * Transfer funds between user’s two bitcoin or ethereum accounts.
             */
            String TRANSFER = "wallet:transactions:transfer";
        }

        interface User {
            /**
             * List detailed user information (information is available without this permission).
             */
            String READ = "wallet:user:read";
            /**
             * Update current user.
             */
            String UPDATE = "wallet:user:update";
            /**
             * Read current user’s email address.
             */
            String EMAIL = "wallet:user:email";
        }

        interface Withdrawals {
            /**
             * List account’s withdrawals.
             */
            String READ = "wallet:withdrawals:read";
            /**
             * Create a new withdrawal.
             */
            String CREATE = "wallet:withdrawals:create";
        }
    }
}
