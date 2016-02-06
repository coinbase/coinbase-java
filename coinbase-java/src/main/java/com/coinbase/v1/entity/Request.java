package com.coinbase.v1.entity;

import java.io.Serializable;


public class Request implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3130834102229546418L;
    private String _accountId;
    private Transaction _transaction;
    private Account _account;
    private com.coinbase.v1.entity.Button _button;
    private Double _qty;
    private String _paymentMethodId;
    private com.coinbase.v1.entity.User _user;
    private String _clientId;
    private String _scopes;
    private String _tokenId;
    private com.coinbase.v1.entity.Address _address;
    private com.coinbase.v1.entity.Application _application;
    private Report _report;
    private String _currency;
    private Boolean _commit;

    public Report getReport() {
        return _report;
    }

    public void setReport(Report report) {
        _report = report;
    }

    public com.coinbase.v1.entity.Address getAddress() {
        return _address;
    }

    public void setAddress(com.coinbase.v1.entity.Address address) {
        _address = address;
    }

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

    public com.coinbase.v1.entity.User getUser() {
        return _user;
    }

    public void setUser(com.coinbase.v1.entity.User user) {
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

    public com.coinbase.v1.entity.Button getButton() {
        return _button;
    }

    public void setButton(com.coinbase.v1.entity.Button button) {
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

    public com.coinbase.v1.entity.Application getApplication() {
        return _application;
    }

    public void setApplication(com.coinbase.v1.entity.Application application) {
        _application = application;
    }

    public String getCurrency() {
        return _currency;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }

    public Boolean getCommit() {
        return _commit;
    }

    public void setCommit(Boolean commit) {
        this._commit = commit;
    }
}
