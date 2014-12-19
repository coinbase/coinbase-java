package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Account implements Serializable {

    public enum Type {
        WALLET("wallet"),
        VAULT("vault"),
        MULTISIG_VAULT("multisig_vault"),
        MULTISIG_WALLET("multisig_wallet"),
        FIAT("fiat");

        private String _value;

        private Type(String value) {
            this._value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return this._value;
        }

        @JsonCreator
        public static Type create(String val) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }

    private static final long serialVersionUID = 8248810301818113960L;
    private String _id;
    private String _name;
    private Money _balance;
    private Money _nativeBalance;
    private DateTime _createdAt;
    private Boolean _primary;
    private Boolean _active;
    private Type _type;

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

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        _type = type;
    }
}
