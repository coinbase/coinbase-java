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

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.coinbase.Coinbase
import com.coinbase.errors.CoinbaseException
import com.coinbase.network.ApiCall
import com.coinbase.network.Callback
import com.coinbase.resources.accounts.Account
import com.coinbase.resources.transactions.MoneyHash
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Locale


//region SDK Classes extension

/**
 * Show or hide progress depending on this Single state.
 */
internal inline fun <T> Single<T>.withProgress(crossinline showProgress: (Boolean) -> Unit): Single<T> =
        doOnSubscribe { showProgress(true) }.doFinally { showProgress(false) }

/**
 * Add subscription to disposable specified when subscribed.
 */
internal fun <T> Single<T>.addToDisposable(disposable: CompositeDisposable): Single<T> =
        doOnSubscribe { disposable.add(it) }


internal fun <T> ApiCall<T>.enqueue(activity: Activity, callback: Callback<T>) {
    enqueue(object : Callback<T> {
        override fun onSuccess(result: T) {
            if (activity.isDestroyed) {
                return
            }
            callback.onSuccess(result)
        }

        override fun onFailure(t: Throwable?) {
            if (activity.isDestroyed) {
                return
            }
            callback.onFailure(t)
        }
    })
}

internal val MoneyHash.formatAmount: String
    get() = String.format(Locale.US, "%s %s", this.amount, this.currency)

internal val Account.nameWithIcon: String
    get() = when (currency.type) {
        "fiat" -> "💵 "
        "crypto" -> "🔐 "
        else -> "🗃 "
    } + name

//endregion

//region Activity Extensions

internal val AppCompatActivity.app: MainApplication get() = application as MainApplication

internal val AppCompatActivity.coinbase: Coinbase get() = this.app.coinbase

internal fun AppCompatActivity.showError(throwable: Throwable) {
    Log.e("SampleApp", "Error!", throwable)

    val message = if (throwable is CoinbaseException) {
        throwable.serverError.message
    } else {
        throwable.message
    }
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//endregion

internal var View.isVisible
    get() = this.visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

internal val Boolean.icon: String get() = if (this) "✅" else "❌"