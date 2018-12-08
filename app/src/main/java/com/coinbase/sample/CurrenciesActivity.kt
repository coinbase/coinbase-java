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
import android.widget.Toast
import com.coinbase.resources.currencies.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_currencies.*
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrenciesActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_currencies)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager = LinearLayoutManager(this)

        coinbase.currenciesResource
                .supportedCurrenciesRx
                .observeOn(AndroidSchedulers.mainThread())
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .subscribe({
                    recyclerView.adapter = RecyclerAdapter(this@CurrenciesActivity, it.data)
                }) { t ->
                    Toast.makeText(this@CurrenciesActivity, t.message, Toast.LENGTH_SHORT).show()
                }
    }

    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

    private fun showProgress(show: Boolean) {
        recyclerView.isVisible = !show
        progressBar.isVisible = show
    }

    private class RecyclerAdapter(private val context: Context,
                                  private val currencies: List<Currency>) : RecyclerView.Adapter<ViewHolder>() {

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                ViewHolder(layoutInflater.inflate(R.layout.item_currency, parent, false))

        override fun getItemCount(): Int = currencies.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currency = currencies[position]

            holder.bind(currency)
            holder.itemView.setOnClickListener { PricesActivity.start(context, currency.id) }
        }

    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val baseCurrency: TextView = itemView.baseCurrency
        val currencyName: TextView = itemView.currencyName

        fun bind(currency: Currency) {
            baseCurrency.text = currency.id
            currencyName.text = currency.name
        }
    }
}
