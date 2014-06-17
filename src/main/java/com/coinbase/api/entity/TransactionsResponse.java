package com.coinbase.api.entity;

import java.util.List;

import org.joda.money.Money;

public class TransactionsResponse extends Response {
    private User _currentUser;
    private Money _balance;
    private Money _nativeBalance;
    private List<TransactionNode> _transactions;
    
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
}
