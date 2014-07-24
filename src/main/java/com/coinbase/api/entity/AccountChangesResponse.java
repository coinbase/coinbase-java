package com.coinbase.api.entity;

import java.util.List;

import org.joda.money.Money;

public class AccountChangesResponse extends Response {
    private User currentUser;
    private Money balance;
    private Money nativeBalance;
    private List<AccountChange> accountChanges;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Money getNativeBalance() {
        return nativeBalance;
    }

    public void setNativeBalance(Money nativeBalance) {
        this.nativeBalance = nativeBalance;
    }

    public List<AccountChange> getAccountChanges() {
        return accountChanges;
    }

    public void setAccountChanges(List<AccountChange> accountChanges) {
        this.accountChanges = accountChanges;
    }
}
