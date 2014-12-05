package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;
import org.joda.time.DateTime;

import com.coinbase.api.deserializer.MoneyDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AccountChange implements Serializable {

    public static class Cache implements Serializable {
        
        /**
         * 
         */
        private static final long serialVersionUID = 4007178459010851945L;

        public enum Category {
            TRANSFER("transfer"),
            TRANSACTION("tx"),
            REQUEST("request"),
            INVOICE("invoice"),
            ORDER("order"),
            DEPOSIT_WITHDRAWAL("deposit_withdraw"),
            TRANSFER_MONEY("transfer_money"),
            PAYMENT_REQUEST("payment_request"),
            ORDER_REFUND("order_refund"),
            CANCELED("canceled"),
            MISPAYMENT_REFUND("mispayment_refund"),
            VAULT_WITHDRAWAL("vault_withdrawal");
            
            private String _value;
            private Category(String value) { this._value = value; }
            
            @JsonValue
            public String toString() { return this._value; }
            
            @JsonCreator
            public static Category create(String val) {
                for (Category category : Category.values()) {
                    if (category.toString().equalsIgnoreCase(val)) {
                        return category;
                    }
                }
                return null;
            }
        }
        
        private Category _category;
        private Boolean _fiat;
        private Boolean _notes_present;
        private User _other_user;
        private PaymentMethod _payment_method;
        private String _status;
        private Account _other_account;
        
        public Category getCategory() {
            return _category;
        }

        public void setCategory(Category category) {
            _category = category;
        }

        public Boolean isFiat() {
            return _fiat;
        }

        public void setFiat(Boolean fiat) {
            _fiat = fiat;
        }

        public Boolean isNotesPresent() {
            return _notes_present;
        }

        public void setNotesPresent(Boolean notes_present) {
            _notes_present = notes_present;
        }

        public User getOtherUser() {
            return _other_user;
        }

        public void setOtherUser(User other_user) {
            _other_user = other_user;
        }

        public PaymentMethod getPaymentMethod() {
            return _payment_method;
        }

        public void setPaymentMethod(PaymentMethod payment_method) {
            this._payment_method = payment_method;
        }

        public String getStatus() {
            return _status;
        }

        public void setStatus(String status) {
            this._status = status;
        }

        public Account getOtherAccount() {
            return _other_account;
        }

        public void setOtherAccount(Account other_account) {
            this._other_account = other_account;
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 8367935513400871799L;
    private String _id;
    private String _transaction_id;
    private DateTime _createdAt;
    private Money _amount;
    private Boolean _confirmed;
    private Cache _cache;

    public Cache getCache() {
        return _cache;
    }

    public void setCache(Cache cache) {
        _cache = cache;
    }

    public Boolean isConfirmed() {
        return _confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        _confirmed = confirmed;
    }

    public String getId() {
        return _id;
    }
    
    public void setId(String id) {
        _id = id;
    }
    
    public DateTime getCreatedAt() {
        return _createdAt;
    }
    
    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }
    
    public Money getAmount() {
        return _amount;
    }
    
    @JsonDeserialize(using=MoneyDeserializer.class)
    public void setAmount(Money amount) {
        _amount = amount;
    }
    
    public String getTransactionId() {
        return _transaction_id;
    }

    public void setTransactionId(String transaction_id) {
        _transaction_id = transaction_id;
    }
}
