package com.coinbase.api.entity;

import java.util.List;

import org.joda.money.Money;

import com.coinbase.api.deserializer.TransactionsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TransactionsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 1738758156595044771L;
    private User _currentUser;
    private Money _balance;
    private Money _nativeBalance;
    private List<Transaction> _transactions;
    
    public List<Transaction> getTransactions() {
        return _transactions;
    }

    @JsonDeserialize(converter=TransactionsLifter.class)
    public void setTransactions(List<Transaction> transactions) {
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
