package com.coinbase.coinbasesample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.coinbase.CallbackWithRetrofit;
import com.coinbase.Coinbase;
import com.coinbase.v2.models.account.Accounts;
import com.coinbase.v2.models.account.Data;
import com.coinbase.v2.models.transactions.Transactions;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionsActivity extends AppCompatActivity {

    Data account;

    @BindView(R.id.accounts_tv)
    TextView accountsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        ButterKnife.bind(this);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Accounts", null);
        Coinbase coinbase = ((MainApplication)getApplicationContext()).getClient();
        coinbase.getAccounts(null, new CallbackWithRetrofit<Accounts>() {
            @Override
            public void onResponse(Call<Accounts> call, Response<Accounts> response, Retrofit retrofit) {
                if (!response.isSuccessful()) {
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
            public void onFailure(Call<Accounts> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void getTransactions() {
        Coinbase coinbase = ((MainApplication)getApplicationContext()).getClient();
        coinbase.getTransactions(account.getId(), null, null, new CallbackWithRetrofit<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response, Retrofit retrofit) {
                if (!response.isSuccessful()) {
                    Log.w("getTransactions", Utils.getErrorMessage(response, retrofit));
                    return;
                }

                Log.d("getTransactions", "Got " + response.body().getData().size() + " transactions");
            }

            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {

            }
        });
    }
}
