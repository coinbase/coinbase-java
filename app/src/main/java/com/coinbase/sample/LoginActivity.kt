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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.coinbase.AuthorizationRequest
import com.coinbase.AuthorizationRequest.AccountSetting
import com.coinbase.Scope.Wallet
import com.coinbase.resources.auth.AccessToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var onDestroyDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onDestroyDisposable = CompositeDisposable()

        if (coinbase.isAuthorized) {
            proceed()
            return
        }

        setContentView(R.layout.activity_login)
        bindData()
    }

    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.data == null) {
            return
        }

        coinbase.completeAuthorizationRx(intent)
                .observeOn(AndroidSchedulers.mainThread())
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .subscribe(this::onAccessTokenReceived, this::showError)
    }

    private fun bindData() {
        signUpButton.setOnClickListener { sigUpWithBrowser() }
        loginCustomTabButton.setOnClickListener { loginWithChromeTab() }
        showProgress(false)
    }

    private fun loginWithChromeTab() {
        val authRequest = getAuthRequest()

        CustomTabsIntent.Builder()
                .setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, theme))
                .setShowTitle(true)
                .build()
                .launchUrl(this, coinbase.buildAuthorizationUri(authRequest))
    }

    private fun sigUpWithBrowser() {
        val authorizationRequest = getAuthRequest()
        authorizationRequest.setShowSignUpPage(true)

        coinbase.beginAuthorization(this, authorizationRequest)
    }

    private fun getAuthRequest(): AuthorizationRequest {
        val authorizationRequest = AuthorizationRequest(Uri.parse(BuildConfig.REDIRECT_URI),
                Wallet.User.READ,
                Wallet.Accounts.READ,
                Wallet.Transactions.READ,
                Wallet.PaymentMethods.READ)

        // Set ALL to make sure the sample app can see all user's accounts.
        authorizationRequest.setAccount(AccountSetting.ALL)
        return authorizationRequest
    }

    private fun showError(throwable: Throwable) {
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        Log.e("Login", "error logging in", throwable)
    }

    private fun onAccessTokenReceived(accessToken: AccessToken) {
        app.accessToken = accessToken
        Toast.makeText(this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show()
        proceed()
    }

    private fun proceed() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showProgress(show: Boolean) {
        signUpButton.isEnabled = !show
        loginCustomTabButton.isEnabled = !show
        progressBar.isVisible = show
    }
}
