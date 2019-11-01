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

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinbase.resources.accounts.Account
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_accounts.accountsRecyclerView
import kotlinx.android.synthetic.main.activity_accounts.progressBar
import kotlinx.android.synthetic.main.item_account.view.currencyAmount
import kotlinx.android.synthetic.main.item_account.view.name
import kotlinx.android.synthetic.main.item_account.view.primary
import java.util.ArrayList

class AccountsActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()
    private val accountsAdapter = AccountsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)
        accountsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        accountsRecyclerView.adapter = accountsAdapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        coinbase.accountResource
                .accountsRx
                .observeOn(AndroidSchedulers.mainThread())
                .addToDisposable(onDestroyDisposable)
                .withProgress(this::showProgress)
                .subscribe({ pagedResponse -> accountsAdapter.setAccounts(pagedResponse.data) })
                { throwable ->
                    showError(throwable)
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.dispose()
    }


    private fun showProgress(show: Boolean) {
        progressBar.isVisible = show
        accountsRecyclerView.isVisible = !show
    }

    internal inner class AccountsAdapter : RecyclerView.Adapter<AccountsActivity.AccountViewHolder>() {
        private val accountsList = ArrayList<Account>()

        fun setAccounts(transactions: List<Account>) {
            accountsList.clear()
            accountsList.addAll(transactions)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AccountsActivity.AccountViewHolder {
            val inflater = LayoutInflater.from(viewGroup.context)
            val view = inflater.inflate(R.layout.item_account, viewGroup, false)
            return this@AccountsActivity.AccountViewHolder(view)
        }

        override fun onBindViewHolder(accountViewHolder: AccountsActivity.AccountViewHolder, i: Int) {
            accountViewHolder.bind(accountsList[i])
        }

        override fun getItemCount(): Int {
            return accountsList.size
        }
    }

    internal inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.name
        val currencyAmount: TextView = itemView.currencyAmount
        val primary: TextView = itemView.primary

        @SuppressLint("SetTextI18n")
        fun bind(account: Account) {
            val accountNameWithIcon = account.nameWithIcon
            name.text = accountNameWithIcon
            name.setTextColor(Color.parseColor(account.currency.color))
            currencyAmount.text = account.balance.formatAmount
            primary.visibility = if (account.primary) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                TransactionsActivity.start(account.id, accountNameWithIcon, this@AccountsActivity)
            }
        }

    }
}