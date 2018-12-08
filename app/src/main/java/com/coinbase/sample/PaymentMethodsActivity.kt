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

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinbase.resources.paymentmethods.models.PaymentMethod
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_payment_methods.*
import kotlinx.android.synthetic.main.item_payment_method.view.*
import java.util.*

class PaymentMethodsActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager = LinearLayoutManager(this)

        coinbase.paymentMethodResource
                .getPaymentMethodsRx()
                .observeOn(AndroidSchedulers.mainThread())
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .subscribe({
                    recyclerView.adapter = RecyclerAdapter(this@PaymentMethodsActivity, it.data)
                }, this::showError
                )
    }

    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

    private fun showProgress(show: Boolean) {
        recyclerView.isVisible = !show
        progressBar.isVisible = show
    }

    private class RecyclerAdapter(context: Context,
                                  private val paymentMethods: List<PaymentMethod>) : RecyclerView.Adapter<ViewHolder>() {

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                ViewHolder(layoutInflater.inflate(R.layout.item_payment_method, parent, false))

        override fun getItemCount(): Int =
                paymentMethods.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
                holder.bind(paymentMethods[position])
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val currency: TextView = itemView.currency
        val paymentMethodName: TextView = itemView.paymentMethodName
        val buyInfo: TextView = itemView.buyInfo
        val sellInfo: TextView = itemView.sellInfo
        val depositInfo: TextView = itemView.depositInfo
        val withdrawalInfo: TextView = itemView.withdrawalInfo
        val primaryInfo: TextView = itemView.primaryInfo

        fun bind(paymentMethod: PaymentMethod) {
            val context = itemView.context

            currency.text = paymentMethod.currency

            paymentMethodName.text = paymentMethod.typeAndName
            buyInfo.text = context.getString(R.string.format_buy_info, paymentMethod.allowBuy.icon, paymentMethod.instantBuy.icon)
            sellInfo.text = context.getString(R.string.format_sell_info, paymentMethod.allowSell.icon, paymentMethod.instantSell.icon)
            depositInfo.text = context.getString(R.string.format_deposit_info, paymentMethod.allowDeposit.icon)
            withdrawalInfo.text = context.getString(R.string.format_withdraw_info, paymentMethod.allowWithdraw.icon)

            val primaryInfoText = paymentMethod.primaryInfo(context)

            primaryInfo.text = context.getString(R.string.format_primary_info, primaryInfoText)
            primaryInfo.isVisible = primaryInfoText.isNotEmpty()
        }
    }
}

private val PaymentMethod.typeAndName: String
    get() {
        val icon = when (this.type) {
            PaymentMethod.TYPE_ACH_BANK_ACCOUNT -> "🏦"
            PaymentMethod.TYPE_FIAT_ACCOUNT -> "💵"
            PaymentMethod.TYPE_CREDIT_CARD, PaymentMethod.TYPE_DEBIT_CARD -> "💳"
            else -> "💰"
        }
        return String.format(Locale.US, "%s %s", icon, name)
    }

private fun PaymentMethod.primaryInfo(context: Context): String {
    val values = mutableListOf<String>()

    if (primaryBuy) values.add(context.getString(R.string.buy))
    if (primarySell) values.add(context.getString(R.string.sell))

    return if (values.isEmpty()) {
        ""
    } else {
        values.joinToString(",")
    }
}
