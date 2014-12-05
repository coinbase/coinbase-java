package com.coinbase.api.entity;

public class UserResponse extends Response {
    /**
     *
     */
    private static final long serialVersionUID = 8847695815066590925L;
    private User _user;
    private OAuthTokensResponse _oauth;

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

    public OAuthTokensResponse getOauth() {
        return _oauth;
    }

    public void setOauth(OAuthTokensResponse oauth) {
        this._oauth = oauth;
    }
}
