package com.coinbase.coinbasesample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.coinbase.Coinbase;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.account.Data;
import com.coinbase.v2.models.transactions.Transactions;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TransactionsActivity extends AppCompatActivity {

    Data account;
    TextView accountsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        accountsTextView = (TextView) findViewById(R.id.accounts_tv);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Accounts", null);
        Coinbase.getInstance().getAccounts(new Callback<Accounts>() {
            @Override
            public void onResponse(Response<Accounts> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    Log.w("getAccounts", Utils.getErrorMessage(response, retrofit));
                    dialog.dismiss();
                    return;
                }

                Log.d("getAccount", "Got " + response.body().getData().size() + " accounts");
                dialog.dismiss();
                account = response.body().getData().get(0);
                accountsTextView.setText("Loaded account: " + account.getName());
                getTransactions();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void getTransactions() {
        Coinbase.getInstance().getTransactions(account.getId(), null, null, new Callback<Transactions>() {
            @Override
            public void onResponse(Response<Transactions> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    Log.w("getTransactions", Utils.getErrorMessage(response, retrofit));
                    return;
                }

                Log.d("getTransactions", "Got " + response.body().getData().size() + " transactions");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
