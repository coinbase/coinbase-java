package com.coinbase.coinbasesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coinbase.Coinbase;
import com.coinbase.v2.models.OAuth;
import com.coinbase.v2.models.user.User;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    Button transactionsButton;
    Button loginButton;
    EditText emailField;
    EditText passwordField;
    TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionsButton = (Button) findViewById(R.id.transactions_button);
        loginButton = (Button) findViewById(R.id.login_button);
        emailField = (EditText) findViewById(R.id.email_et);
        passwordField = (EditText) findViewById(R.id.password_et);
        userTextView = (TextView) findViewById(R.id.user_tv);

        enableButtons(false);

        Coinbase.init("34183b03a3e1f0b74ee6aa8a6150e90125de2d6c1ee4ff7880c2b7e6e98b11f5",
                "2c481f46f9dc046b4b9a67e630041b9906c023d139fbc77a47053328b9d3122d");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coinbase.getInstance().getAuthCode(emailField.getText().toString(),
                        passwordField.getText().toString(),
                        new Callback<OAuth>() {
                            @Override
                            public void onResponse(Response<OAuth> response, Retrofit retrofit) {
                                if (response.isSuccess())
                                    getUser();
                                else
                                    handleLoginError(Utils.getErrorMessage(response, retrofit));
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                handleLoginError(null);
                            }
                        });
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
                    userTextView.setText(response.body().getData().getUsername());
                    enableButtons(true);
                }
                else
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
}
