package com.coinbase.api.entity;


public class Request {

    private String _accountId;
    private Transaction _transaction;
    private Account _account;
    private Button _button;
    private Double _qty;
    private String _paymentMethodId;
    private User _user;
    private String _clientId;
    private String _scopes;
    private String _tokenId;

    public String getClientId() {
        return _clientId;
    }

    public void setClientId(String clientId) {
        _clientId = clientId;
    }

    public String getScopes() {
        return _scopes;
    }

    public void setScopes(String scopes) {
        _scopes = scopes;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

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

    public String getTokenId() {
	return _tokenId;
    }

    public void setTokenId(String tokenId) {
	_tokenId = tokenId;
    }

}
