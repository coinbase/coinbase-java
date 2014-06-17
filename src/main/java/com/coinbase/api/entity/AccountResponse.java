package com.coinbase.api.entity;

public class AccountResponse extends Response {
    private Account _account;
    
    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }
}

