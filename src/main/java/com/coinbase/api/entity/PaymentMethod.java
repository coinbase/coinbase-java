package com.coinbase.api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    public enum Type {
        BANK_ACCOUNT("bank_account"),
        BANK_WIRE("bank_wire"),
        COINBASE_ACCOUNT("coinbase_account"),
        COINBASE_FIAT_ACCOUNT("coinbase_fiat_account"),
        CREDIT_CARD("credit_card"),
        FUTURE_MERCHANT_PAYOUT("future_merchant_payout"),
        SEPA_PAYMENT_METHOD("sepa_payment_method");

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

    /**
     *
     */
    private static final long serialVersionUID = -3574818318535801143L;
    private String _id;
    private String _name;
    private Boolean _canBuy;
    private Boolean _canSell;
    private String _accountId;
    private String _currency;
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

    public Boolean canBuy() {
        return _canBuy;
    }

    public void setCanBuy(Boolean canBuy) {
        _canBuy = canBuy;
    }

    public Boolean canSell() {
        return _canSell;
    }

    public void setCanSell(Boolean canSell) {
        _canSell = canSell;
    }

    public String getAccountId() {
        return _accountId;
    }

    public void setAccountId(String accountId) {
        this._accountId = accountId;
    }

    public String getCurrency() {
        return _currency;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        this._type = type;
    }
}
