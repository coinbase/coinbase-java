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

package com.coinbase.sample

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.coinbase.Coinbase
import com.coinbase.CoinbaseBuilder
import com.coinbase.errors.CoinbaseOAuthException
import com.coinbase.resources.auth.AccessToken
import com.coinbase.resources.auth.RevokeTokenResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

class MainApplication : Application() {

    lateinit var coinbase: Coinbase
        private set

    val savedExpireDate: Date? by lazy {
        val time = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getLong(KEY_EXPIRE_DATE, 0)
        if (time != 0L) {
            Date(time)
        } else {
            null
        }
    }

    var accessToken: AccessToken
        get() {
            val json = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_ACCESS_TOKEN, null)
            return if (json == null) AccessToken() else gson.fromJson(json, AccessToken::class.java)
        }
        set(value) {
            val json = gson.toJson(value)

            val expiresIn = Calendar.getInstance()
            expiresIn.add(Calendar.SECOND, value.expiresIn)

            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_ACCESS_TOKEN, json)
                    .putLong(KEY_EXPIRE_DATE, expiresIn.time.time)
                    .apply()
        }

    private val gson = Gson()

    private val tokenUpdateListener = object : Coinbase.TokenListener {
        override fun onNewTokensAvailable(accessToken: AccessToken) {
            this@MainApplication.accessToken = accessToken
        }

        override fun onRefreshFailed(cause: CoinbaseOAuthException) {
            Log.e("CoinbaseSample", "Access token autorefresh failed, logging out", cause)
            logout()
        }

        override fun onTokenRevoked() {
            logout()
        }
    }

    override fun onCreate() {
        super.onCreate()

        coinbase = buildCoinbase()
    }

    private fun buildCoinbase(): Coinbase {
        val builder = CoinbaseBuilder.withTokenAutoRefresh(this,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                accessToken.accessToken,
                accessToken.refreshToken,
                tokenUpdateListener
        )
        builder.withLoggingLevel(HttpLoggingInterceptor.Level.BODY)
        return builder.withLoggingLevel(HttpLoggingInterceptor.Level.BODY).build()
    }

    fun revokeTokenRx(): Single<RevokeTokenResponse> {
        val savedAccessToken = accessToken

        return coinbase.authResource
                .revokeTokenRx(savedAccessToken.accessToken)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    clearToken()
                }
    }

    fun refreshTokensRx(): Single<AccessToken> {
        return coinbase.authResource
                .refreshTokensRx(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, accessToken.refreshToken)
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun clearToken() {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
    }

    fun logout() {
        clearToken()
        coinbase.logout()

        startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    companion object {

        const val PREF_NAME = "Sample App"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_EXPIRE_DATE = "expireDate"
    }

}
