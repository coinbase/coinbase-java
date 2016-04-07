package com.coinbase.coinbasesample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coinbase.Coinbase;
import com.coinbase.OAuth;
import com.coinbase.v1.entity.OAuthTokensResponse;
import com.coinbase.v2.models.user.User;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    // NOTE: Go to https://www.coinbase.com/oauth/applications/new
    // to create an application and generate your own keys
    private static final String API_KEY = "f89d5f52d5bf6678b6449e0b6feb5100bf8e0ed3dc45f5f2be51fcea1232111c";
    private static final String API_SECRET = "9fbb237f4bc9c977f5e88895882b5677c4de395fe0996a6eedf912d4fee2b415";

    Button transactionsButton;
    Button authButton;
    TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // In the Activity we set up to listen to our redirect URI
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW")) {
            new CompleteAuthorizationTask(intent).execute();
        }

        transactionsButton = (Button) findViewById(R.id.transactions_button);
        authButton = (Button) findViewById(R.id.login_button);
        userTextView = (TextView) findViewById(R.id.user_tv);

        enableButtons(false);

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OAuth.beginAuthorization(MainActivity.this,
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
    }

    private void getUser() {
        Coinbase.getInstance().getUser(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    userTextView.setText("User: " + response.body().getData().getName());
                    enableButtons(true);
                } else
                    handleLoginError(Utils.getErrorMessage(response, retrofit));
            }

            @Override
            public void onFailure(Throwable t) {
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
                return OAuth.completeAuthorization(MainActivity.this,
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
            Coinbase.init(tokens.getAccessToken());
            getUser();
        }
    }

}
