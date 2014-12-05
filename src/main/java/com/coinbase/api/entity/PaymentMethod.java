package com.coinbase.api.entity;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

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
    private String _type;
    
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
        this._accountId = _accountId;
    }

    public String getCurrency() {
        return _currency;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }
}
