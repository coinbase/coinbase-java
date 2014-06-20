package com.coinbase.api.entity;

public class UserResponse extends Response {
    private User _user;

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

}
