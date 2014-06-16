package com.coinbase.api.entity;


public class Request {

    private String _accountId;
    private Transaction _transaction;
    private Account _account;
    private Button _button;

    public String getAccountId() {
        return _accountId;
    }

    public void setAccountId(String accountId) {
        _accountId = accountId;
    }

    public Button getButton() {
        return _button;
    }

    public void setButton(Button button) {
        _button = button;
    }

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
