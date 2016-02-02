package com.coinbase.coinbasesample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coinbase.Coinbase;
import com.coinbase.CoinbaseBuilder;
import com.coinbase.models.account.Accounts;
import com.coinbase.models.account.Data;
import com.coinbase.models.errors.Errors;
import com.coinbase.models.transactions.Transactions;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView accountsTextView;
    Button getTransactionsButton;
    Data account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountsTextView = (TextView)findViewById(R.id.accounts_textview);
        getTransactionsButton = (Button)findViewById(R.id.get_transactions_button);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Accounts", null);

        final Coinbase cb = new CoinbaseBuilder()
                .withApiKey("YOUR_API_KEY",
                        "YOUR_API_SECRET")
                .build();
        cb.getAccounts(new Callback<Accounts>() {
            @Override
            public void onResponse(Response<Accounts> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    Log.w("getAccounts", getErrorMessage(response, retrofit));
                    dialog.dismiss();
                    return;
                }

                Log.d("getAccount", "Got " + response.body().getData().size() + " accounts");
                dialog.dismiss();
                account = response.body().getData().get(0);
                accountsTextView.setText("Loaded account: " + account.getName());
                enableButtons(true);
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });

        getTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cb.getTransactions(account.getId(), null, null, new Callback<Transactions>() {
                    @Override
                    public void onResponse(Response<Transactions> response, Retrofit retrofit) {
                        if (!response.isSuccess()) {
                            Log.w("getTransactions", getErrorMessage(response, retrofit));
                            return;
                        }

                        Log.d("getTransactions", "Got " + response.body().getData().size() + " transactions");
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

        enableButtons(false);
    }

    private void enableButtons(boolean enable) {
        getTransactionsButton.setEnabled(enable);
    }

    public String getErrorMessage(retrofit.Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, Errors> converter =
                retrofit.responseConverter(Errors.class, new Annotation[0]);

        String message = null;
        Errors errors;

        try {
            errors = converter.convert(response.errorBody());
            if (errors.getErrors().size() > 0)
                message = errors.getErrors().get(0).getMessage();
        } catch (IOException e) {

        }

        return message;
    }
}
