package com.coinbase.api;

import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.User;

public interface Coinbase {

    public User getUser();
    
    public Transaction getTransaction(String id);
    
    public Transaction requestMoney(Transaction transaction);

    public Transaction sendMoney(Transaction transaction);

}
