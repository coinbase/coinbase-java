package com.coinbase.v1.entity;

public class UserResponse extends Response {
    /**
     *
     */
    private static final long serialVersionUID = 8847695815066590925L;
    private com.coinbase.v1.entity.User _user;
    private com.coinbase.v1.entity.OAuthTokensResponse _oauth;

    public com.coinbase.v1.entity.User getUser() {
        return _user;
    }

    public void setUser(com.coinbase.v1.entity.User user) {
        _user = user;
    }

    public com.coinbase.v1.entity.OAuthTokensResponse getOauth() {
        return _oauth;
    }

    public void setOauth(com.coinbase.v1.entity.OAuthTokensResponse oauth) {
        this._oauth = oauth;
    }
}
