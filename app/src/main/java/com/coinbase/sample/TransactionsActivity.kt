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
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coinbase.PagedResponse
import com.coinbase.PaginationParams
import com.coinbase.resources.transactions.Transaction
import com.coinbase.resources.transactions.TransactionsResource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_transactions.accountText
import kotlinx.android.synthetic.main.activity_transactions.recyclerView
import kotlinx.android.synthetic.main.activity_transactions.swipeRefreshLayout
import kotlinx.android.synthetic.main.item_transaction.view.amount
import kotlinx.android.synthetic.main.item_transaction.view.date
import kotlinx.android.synthetic.main.item_transaction.view.nativeAmount
import kotlinx.android.synthetic.main.item_transaction.view.status
import kotlinx.android.synthetic.main.item_transaction.view.subtitle
import kotlinx.android.synthetic.main.item_transaction.view.title
import kotlinx.android.synthetic.main.item_transaction.view.type
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class TransactionsActivity : AppCompatActivity() {

    private lateinit var transactionsResource: TransactionsResource
    private lateinit var adapter: TransactionsAdapter
    private lateinit var accountId: String
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var latestPagination: PagedResponse.Pagination? = null
    private val onDestroyDisposable = CompositeDisposable()

    private val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isLoading || !canLoadMore()) {
                return
            }
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItems = recyclerView.adapter.itemCount

            if (totalItems - lastVisibleItemPosition < LOAD_ITEM_THRESHOLD) {
                loadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        swipeRefreshLayout.setOnRefreshListener {
            latestPagination = null
            adapter.clear()
            loadMore()
        }

        accountId = intent.extras.getString(ACCOUNT_ID_KEY)
        accountText.text = intent.extras.getString(ACCOUNT_NAME_KEY)

        transactionsResource = coinbase.transactionsResource

        layoutManager = LinearLayoutManager(this)
        adapter = TransactionsAdapter(LayoutInflater.from(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.adapter = adapter

        loadMore()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.dispose()
    }

    private fun canLoadMore(): Boolean {
        return latestPagination == null || latestPagination?.nextUri != null
    }

    private fun loadMore() {
        val params = latestPagination?.nextPage()
                ?: PaginationParams().also { it.limit = PAGE_SIZE }

        transactionsResource.listTransactionsRx(accountId, params, Transaction.ExpandField.ALL)
                .observeOn(AndroidSchedulers.mainThread())
                .addToDisposable(onDestroyDisposable)
                .doOnSubscribe({ isLoading = true })
                .subscribe({ pagedResponse ->
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    latestPagination = pagedResponse.pagination
                    adapter.addTransactions(pagedResponse.data)
                }, this@TransactionsActivity::showError)
    }

    internal inner class TransactionsAdapter(private val inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val transactionList = ArrayList<Transaction>()

        fun addTransactions(transactions: List<Transaction>) {
            transactionList.addAll(transactions)
            notifyDataSetChanged()
        }

        fun clear() {
            transactionList.clear()
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int =
                if (position > transactionList.size - 1) PROGRESS_ITEM else TRANSACTION_ITEM

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
            return when (getItemViewType(i)) {
                TRANSACTION_ITEM -> TransactionViewHolder(inflater.inflate(R.layout.item_transaction, viewGroup, false))
                PROGRESS_ITEM -> object : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_progress, viewGroup, false)) {}
                else -> {
                    throw IllegalStateException("incorrect item view type")
                }
            }
        }

        override fun onBindViewHolder(transactionViewHolder: RecyclerView.ViewHolder, i: Int) {
            (transactionViewHolder as? TransactionViewHolder)?.bind(transactionList[i])
        }

        override fun getItemCount(): Int {
            return transactionList.size + if (canLoadMore()) 1 else 0
        }
    }

    internal class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val date: TextView = itemView.date
        private val type: TextView = itemView.type
        private val title: TextView = itemView.title
        private val subtitle: TextView = itemView.subtitle
        private val amount: TextView = itemView.amount
        private val nativeAmount: TextView = itemView.nativeAmount
        private val status: TextView = itemView.status

        private val dateFormat = SimpleDateFormat("MMM dd, YYYY", Locale.US)

        fun bind(transaction: Transaction) {
            this.date.text = dateFormat.format(transaction.createdAt)

            type.text = transaction.typeIcon
            title.text = transaction.details.title
            subtitle.text = transaction.details.subtitle

            amount.text = transaction.amount.formatAmount
            nativeAmount.text = transaction.nativeAmount.formatAmount

            val amountColorId = if (transaction.amount.amount.startsWith("-")) R.color.colorAccent else R.color.colorPrimary
            val resources = amount.resources
            amount.setTextColor(ResourcesCompat.getColor(resources, amountColorId, null))
            nativeAmount.setTextColor(ResourcesCompat.getColor(resources, amountColorId, null))

            status.text = transaction.statusWithIcon
        }
    }

    companion object {

        // For demo purpose.
        const val PAGE_SIZE = 5
        const val LOAD_ITEM_THRESHOLD = 3

        const val TRANSACTION_ITEM = 0
        const val PROGRESS_ITEM = 1

        const val ACCOUNT_ID_KEY = "id_key"
        const val ACCOUNT_NAME_KEY = "name_key"

        fun start(accountId: String, accountName: String, activity: Activity) {
            val intent = Intent(activity, TransactionsActivity::class.java)
                    .putExtra(ACCOUNT_ID_KEY, accountId)
                    .putExtra(ACCOUNT_NAME_KEY, accountName)
            activity.startActivity(intent)
        }
    }
}

//region Transaction binding extensions

private val Transaction.typeIcon: String
    get() {
        return when (type) {
            Transaction.TYPE_SEND -> if (amount.amount.startsWith("-")) "➖️" else "➕️"
            Transaction.TYPE_REQUEST -> "❓"
            Transaction.TYPE_TRANSFER -> "🔁"
            Transaction.TYPE_BUY, Transaction.TYPE_SELL -> "💰"
            Transaction.TYPE_FIAT_DEPOSIT, Transaction.TYPE_FIAT_WITHDRAWAL -> "💵"
            Transaction.TYPE_EXCHANGE_DEPOSIT, Transaction.TYPE_EXCHANGE_WITHDRAWAL -> "💱"
            Transaction.TYPE_VAULT_WITHDRAWAL -> "🏦"
            else -> "💸"
        }
    }

private val Transaction.statusWithIcon: String
    get() {

        val icon = when (status) {
            Transaction.STATUS_PENDING -> "⚫"
            Transaction.STATUS_COMPLETED -> "✅"
            Transaction.STATUS_FAILED -> "⚠️"
            Transaction.STATUS_EXPIRED -> "⌛️"
            Transaction.STATUS_CANCELED -> "❌"
            Transaction.STATUS_WAITING_FOR_SIGNATURE -> "🕰"
            Transaction.STATUS_WAITING_FOR_CLEARING -> "🕰"
            else -> "⚫"
        }

        return String.format(Locale.US, "%s %s", icon, status.replace("_", " "))
    }

//endregion