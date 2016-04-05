package com.coinbase.coinbasesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.coinbase.Coinbase;

public class MainActivity extends AppCompatActivity {
    Button getTransactionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Coinbase.init("34183b03a3e1f0b74ee6aa8a6150e90125de2d6c1ee4ff7880c2b7e6e98b11f5",
                "2c481f46f9dc046b4b9a67e630041b9906c023d139fbc77a47053328b9d3122d");

        getTransactionsButton = (Button) findViewById(R.id.transactions_button);
        getTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
            }
        });
    }
}
