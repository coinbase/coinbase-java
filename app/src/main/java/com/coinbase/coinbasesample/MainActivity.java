package com.coinbase.coinbasesample;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coinbase.CallbackWithRetrofit;
import com.coinbase.Coinbase;
import com.coinbase.OAuth;
import com.coinbase.v1.entity.OAuthTokensResponse;
import com.coinbase.v2.models.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    // NOTE: Go to https://www.coinbase.com/oauth/applications/new
    // to create an application and generate your own keys
    private static final String API_KEY = "f89d5f52d5bf6678b6449e0b6feb5100bf8e0ed3dc45f5f2be51fcea1232111c";
    private static final String API_SECRET = "9fbb237f4bc9c977f5e88895882b5677c4de395fe0996a6eedf912d4fee2b415";

    @BindView(R.id.transactions_btn)
    Button transactionsButton;

    @BindView(R.id.auth_btn)
    Button authButton;

    @BindView(R.id.data_btn)
    Button dataButton;

    @BindView(R.id.user_tv)
    TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // In the Activity we set up to listen to our redirect URI
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW")) {
            new CompleteAuthorizationTask(intent).execute();
        }

        enableButtons(false);

        final OAuth oauth = ((MainApplication)getApplicationContext()).getOAuth();
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    oauth.beginAuthorization(MainActivity.this,
                            API_KEY,
                            "wallet:user:read,wallet:accounts:read",
                            "coinbase-sample-app://coinbase-oauth",
                            null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
            }
        });

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getUser() {
        Coinbase coinbase = ((MainApplication)getApplicationContext()).getClient();
        coinbase.getUser(new CallbackWithRetrofit<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response, Retrofit retrofit) {
                if (response.isSuccessful()) {
                    userTextView.setText("User: " + response.body().getData().getName());
                    enableButtons(true);
                } else
                    handleLoginError(Utils.getErrorMessage(response, retrofit));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                handleLoginError(null);
            }
        });
    }

    private void enableButtons(boolean enabled) {
        transactionsButton.setEnabled(enabled);
    }

    private void handleLoginError(String message) {
        if (message == null)
            message = "Login error occurred";

        userTextView.setText(message);
        enableButtons(false);
    }

    public class CompleteAuthorizationTask extends AsyncTask<Void, Void, OAuthTokensResponse> {
        private Intent mIntent;

        public CompleteAuthorizationTask(Intent intent) {
            mIntent = intent;
        }

        @Override
        public OAuthTokensResponse doInBackground(Void... params) {
            try {
                final OAuth oauth = ((MainApplication)getApplicationContext()).getOAuth();
                return oauth.completeAuthorization(MainActivity.this,
                        API_KEY,
                        API_SECRET,
                        mIntent.getData());
            } catch (Exception e) {
                handleLoginError("Authorization failed");
                return null;
            }
        }

        @Override
        public void onPostExecute(OAuthTokensResponse tokens) {
            Coinbase coinbase = ((MainApplication)getApplicationContext()).getClient();
            coinbase.init(MainActivity.this, tokens.getAccessToken());
            getUser();
        }
    }

}
