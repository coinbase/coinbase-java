package com.coinbase.api.entity;


public class Request {

    private Transaction _transaction;
    private Account _account;

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public Transaction getTransaction() {
        return _transaction;
    }

    public void setTransaction(Transaction transaction) {
        _transaction = transaction;
    }

}
