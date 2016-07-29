package com.coinbase.coinbasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coinbase.Coinbase;
import com.coinbase.v2.models.price.Price;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

        sellPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coinbase.getInstance().getSellPrice(BTC, USD, null, new Callback<Price>() {
                    @Override
                    public void onResponse(Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showError();
                    }
                });
            }
        });

        buyPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coinbase.getInstance().getBuyPrice(BTC, USD, null, new Callback<Price>() {
                    @Override
                    public void onResponse(Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showError();
                    }
                });
            }
        });

        spotPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coinbase.getInstance().getSpotPrice(BTC, USD, null, new Callback<Price>() {
                    @Override
                    public void onResponse(Response<Price> response, Retrofit retrofit) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showError();
                    }
                });
            }
        });
    }

    private void handleResponse(Response<Price> response) {
        if (!response.isSuccess()) {
            showError();
            return;
        }

        priceTextView.setText(response.body().getData().getAmount());
    }

    private void showError() {
        priceTextView.setText(ERROR_MESSAGE);
    }
}
