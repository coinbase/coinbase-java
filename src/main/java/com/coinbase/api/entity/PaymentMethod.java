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
    
}
