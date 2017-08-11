package com.coinbase.coinbasesample;

import android.app.Application;

import com.coinbase.OAuth;
import com.coinbase.Coinbase;

public class MainApplication extends Application {

    private Coinbase mCoinbase;

    private OAuth mOAuth;

    public Coinbase getClient() {
        return mCoinbase;
    }

    public OAuth getOAuth() {
        return mOAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mCoinbase = new Coinbase();
        mOAuth = new OAuth(mCoinbase);
    }
}
