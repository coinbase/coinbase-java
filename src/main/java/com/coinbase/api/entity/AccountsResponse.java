package com.coinbase.api.entity;

import java.util.List;

public class AccountsResponse extends Response {
    private List<Account> _accounts;
    
    public List<Account> getAccounts() {
        return _accounts;
    }

    public void setAccounts(List<Account> accounts) {
        _accounts = accounts;
    }
}
