package com.coinbase.api.entity;

import java.util.List;

import org.joda.money.Money;

import com.coinbase.api.deserializer.ErrorsCollector;
import com.coinbase.api.entity.UserNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Response {
    
    private Boolean _success;
    private String _error;
    private String _errors;
    private Transaction _transaction;
    private List<UserNode> _users;
    private List<TransactionNode> _transactions;
    private User _currentUser;
    private Money _balance;
    private Money _nativeBalance;
    private int _totalCount;
    private int _numPages;
    private int _currentPage;
    private List<TransferNode> _transfers;
    private List<AddressNode> _addresses;
    private List<Account> _accounts;
    private Account _account;
    private Button _button;

    public Button getButton() {
        return _button;
    }

    public void setButton(Button button) {
        _button = button;
    }

    public void setError(String error) {
        _error = error;
    }

    public String getErrors() {
	if (_error != null) {
	    if (_errors != null) {
		return _error + ", " + _errors;
	    } else {
		return _error;
	    }
	} else {
	    return _errors;
	}
    }

    @JsonDeserialize(converter=ErrorsCollector.class)
    public void setErrors(String errors) {
        _errors = errors;
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public List<Account> getAccounts() {
        return _accounts;
    }

    public void setAccounts(List<Account> accounts) {
        _accounts = accounts;
    }

    public List<TransferNode> getTransfers() {
        return _transfers;
    }

    public void setTransfers(List<TransferNode> transfers) {
        _transfers = transfers;
    }

    public List<TransactionNode> getTransactions() {
        return _transactions;
    }

    public void setTransactions(List<TransactionNode> transactions) {
        _transactions = transactions;
    }

    public User getCurrentUser() {
        return _currentUser;
    }

    public void setCurrentUser(User currentUser) {
        _currentUser = currentUser;
    }

    public Money getBalance() {
        return _balance;
    }

    public void setBalance(Money balance) {
        _balance = balance;
    }

    public Money getNativeBalance() {
        return _nativeBalance;
    }

    public void setNativeBalance(Money nativeBalance) {
        _nativeBalance = nativeBalance;
    }

    public int getTotalCount() {
        return _totalCount;
    }

    public void setTotalCount(int totalCount) {
        _totalCount = totalCount;
    }

    public int getNumPages() {
        return _numPages;
    }

    public void setNumPages(int numPages) {
        _numPages = numPages;
    }

    public int getCurrentPage() {
        return _currentPage;
    }

    public void setCurrentPage(int currentPage) {
        _currentPage = currentPage;
    }

    public List<UserNode> getUsers() {
        return _users;
    }

    public void setUsers(List<UserNode> users) {
        _users = users;
    }

    public Boolean isSuccess() {
        return _success;
    }

    public void setSuccess(Boolean success) {
        this._success = success;
    }

    public Transaction getTransaction() {
        return _transaction;
    }

    public void setTransaction(Transaction transaction) {
        _transaction = transaction;
    }

    public List<AddressNode> getAddresses() {
	return _addresses;
    }

    public void setAddresses(List<AddressNode> addresses) {
	_addresses = addresses;
    }

    public boolean hasErrors() {
	return _error != null || _errors != null;
    }

}
