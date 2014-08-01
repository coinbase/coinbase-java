package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;
import org.joda.time.DateTime;

public class Account implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8248810301818113960L;
    private String _id;
    private String _name;
    private Money _balance;
    private Money _nativeBalance;
    private DateTime _createdAt;
    private Boolean _primary;
    private Boolean _active;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
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

    public DateTime getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }

    public Boolean isPrimary() {
        return _primary;
    }

    public void setPrimary(boolean primary) {
        _primary = primary;
    }

    public Boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }

}
