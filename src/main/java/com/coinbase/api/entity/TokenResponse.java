package com.coinbase.api.entity;

public class TokenResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -4744328000094047219L;
    private Token _token;

    public Token getToken() {
        return _token;
    }

    public void setToken(Token token) {
        _token = token;
    }
}
