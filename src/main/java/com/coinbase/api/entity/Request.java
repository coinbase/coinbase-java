package com.coinbase.api.entity;


public class Request {

    private String _accountId;
    private Transaction _transaction;
    private Account _account;
    private Button _button;
    private Double _qty;
    private String _paymentMethodId;

    public String getPaymentMethodId() {
        return _paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        _paymentMethodId = paymentMethodId;
    }

    public Double getQty() {
        return _qty;
    }

    public void setQty(Double qty) {
        _qty = qty;
    }

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
