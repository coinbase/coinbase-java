package com.coinbase.api;

import com.coinbase.api.exception.CoinbaseException;

import java.io.IOException;
import java.net.URL;

public interface CoinbaseConnection {
    String doHttp(URL url, String method, Object requestBody) throws IOException, CoinbaseException;
}
