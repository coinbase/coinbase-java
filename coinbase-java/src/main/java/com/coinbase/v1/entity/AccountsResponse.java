package com.coinbase.v1.entity;

import java.util.List;

public class AccountsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -4054933671856737119L;
    private List<com.coinbase.v1.entity.Account> _accounts;
    
    public List<com.coinbase.v1.entity.Account> getAccounts() {
        return _accounts;
    }

    public void setAccounts(List<com.coinbase.v1.entity.Account> accounts) {
        _accounts = accounts;
    }
}
