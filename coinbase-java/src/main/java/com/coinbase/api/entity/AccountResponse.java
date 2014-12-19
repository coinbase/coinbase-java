package com.coinbase.api.entity;

public class AccountResponse extends Response {
    
    /**
     * 
     */
    private static final long serialVersionUID = -874896173060788591L;
    private Account _account;
    
    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }
}

