package com.coinbase.coinbasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coinbase.CallbackWithRetrofit;
import com.coinbase.Coinbase;
import com.coinbase.v2.models.price.Price;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataActivity extends AppCompatActivity {

    private static final String BTC = "BTC";
    private static final String USD = "USD";
    private static final String ERROR_MESSAGE = "Error occurred, please try again";

    @BindView(R.id.price_tv)
    TextView priceTextView;

    @BindView(R.id.sell_price_btn)
    Button sellPriceButton;

    @BindView(R.id.buy_price_btn)
    Button buyPriceButton;

    @BindView(R.id.spot_price_btn)
    Button spotPriceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ButterKnife.bind(this);

        final Coinbase coinbase = ((MainApplication)getApplicationContext()).getClient();

        sellPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinbase.getSellPrice(BTC, USD, null, new CallbackWithRetrofit<Price>() {
                    @Override
                    public void onResponse(Call<Price> call, Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        showError();
                    }
                });
            }
        });

        buyPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinbase.getBuyPrice(BTC, USD, null, new CallbackWithRetrofit<Price>() {
                    @Override
                    public void onResponse(Call<Price> call, Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call<Price> call, Throwable t) {
                        showError();
                    }
                });
            }
        });

        spotPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinbase.getSpotPrice(BTC, USD, null, new CallbackWithRetrofit<Price>() {
                    @Override
                    public void onResponse(Call<Price> call, Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call<Price> call, Throwable t) {
                        showError();
                    }
                });
            }
        });
    }

    private void handleResponse(Response<Price> response) {
        if (!response.isSuccessful()) {
            showError();
            return;
        }

        priceTextView.setText(response.body().getData().getAmount());
    }

    private void showError() {
        priceTextView.setText(ERROR_MESSAGE);
    }
}
