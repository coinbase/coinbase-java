package com.coinbase.api.entity;

public class TokenResponse extends Response {
    private Token _token;

    public Token getToken() {
        return _token;
    }

    public void setToken(Token token) {
        _token = token;
    }
}
